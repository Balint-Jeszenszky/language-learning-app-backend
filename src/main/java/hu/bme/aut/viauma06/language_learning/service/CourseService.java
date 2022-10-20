package hu.bme.aut.viauma06.language_learning.service;

import hu.bme.aut.viauma06.language_learning.controller.exceptions.InternalServerErrorException;
import hu.bme.aut.viauma06.language_learning.controller.exceptions.NotFoundException;
import hu.bme.aut.viauma06.language_learning.mapper.CourseMapper;
import hu.bme.aut.viauma06.language_learning.model.Course;
import hu.bme.aut.viauma06.language_learning.model.ERole;
import hu.bme.aut.viauma06.language_learning.model.Role;
import hu.bme.aut.viauma06.language_learning.model.User;
import hu.bme.aut.viauma06.language_learning.model.dto.request.CourseDetailsRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.response.CourseDetailsResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.CourseResponse;
import hu.bme.aut.viauma06.language_learning.repository.CourseRepository;
import hu.bme.aut.viauma06.language_learning.repository.RoleRepository;
import hu.bme.aut.viauma06.language_learning.repository.UserRepository;
import hu.bme.aut.viauma06.language_learning.security.service.LoggedInUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LoggedInUserService loggedInUserService;

    public List<CourseResponse> getAllCoursesForTeacher() {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        List<Course> coursesByTeacher = courseRepository.findCoursesByTeacher(loggedInUser);

        return coursesByTeacher
                .stream()
                .map(c -> CourseMapper.INSTANCE.courseToCourseResponse(c))
                .collect(Collectors.toList());
    }

    public CourseResponse createCourse(String name) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        Course course = new Course(name, null, loggedInUser);

        courseRepository.save(course);

        return CourseMapper.INSTANCE.courseToCourseResponse(course);
    }

    @Transactional
    public CourseDetailsResponse editCourse(CourseDetailsRequest courseDetailsRequest) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        Optional<Course> course = courseRepository.findByIdAndTeacher(courseDetailsRequest.getId(), loggedInUser);

        if (course.isEmpty()) {
            throw new NotFoundException("Course not found");
        }

        List<User> existingUsers = userRepository.findAllByEmailIn(courseDetailsRequest.getStudentEmails());
        List<String> existingEmails = existingUsers.stream().map(User::getEmail).collect(Collectors.toList());
        courseDetailsRequest.getStudentEmails().removeAll(existingEmails);

        // TODO validate emails

        Optional<Role> studentRole = roleRepository.findByName(ERole.ROLE_STUDENT);

        if (studentRole.isEmpty()) {
            throw new InternalServerErrorException("User role not found");
        }

        List<User> newStudents = courseDetailsRequest.getStudentEmails()
                .stream()
                .map(e -> new User(null, e, null, Set.of(studentRole.get())))
                .collect(Collectors.toList());

        userRepository.saveAll(newStudents);

        existingUsers.addAll(newStudents);

        Course storedCourse = course.get();
        storedCourse.setDeadline(courseDetailsRequest.getDeadline());
        storedCourse.setName(courseDetailsRequest.getName());
        storedCourse.setStudents(existingUsers);

        courseRepository.save(storedCourse);

        // TODO send mail

        return CourseMapper.INSTANCE.courseToCourseDetailsResponse(storedCourse);
    }

    public void deleteCourse(Integer id) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        Optional<Course> course = courseRepository.findByIdAndTeacher(id, loggedInUser);

        if (course.isEmpty()) {
            throw new NotFoundException("Course not found");
        }

        courseRepository.delete(course.get());
    }
}
