package hu.bme.aut.viauma06.language_learning.service;

import hu.bme.aut.viauma06.language_learning.controller.exceptions.BadRequestException;
import hu.bme.aut.viauma06.language_learning.controller.exceptions.InternalServerErrorException;
import hu.bme.aut.viauma06.language_learning.controller.exceptions.NotFoundException;
import hu.bme.aut.viauma06.language_learning.mapper.CourseMapper;
import hu.bme.aut.viauma06.language_learning.model.*;
import hu.bme.aut.viauma06.language_learning.model.dto.StudentDto;
import hu.bme.aut.viauma06.language_learning.model.dto.request.CourseDetailsRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.request.CourseRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.request.SubmissionRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.response.CourseDetailsResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.CourseResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.StudentCourseResponse;
import hu.bme.aut.viauma06.language_learning.repository.*;
import hu.bme.aut.viauma06.language_learning.security.service.LoggedInUserService;
import hu.bme.aut.viauma06.language_learning.service.util.EmailValidator;
import hu.bme.aut.viauma06.language_learning.service.util.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Autowired
    private SendMail sendMail;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<CourseResponse> getAllCoursesForTeacher() {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        List<Course> coursesByTeacher = courseRepository.findCoursesByTeacher(loggedInUser);

        return coursesByTeacher
                .stream()
                .map(c -> CourseMapper.INSTANCE.courseToCourseResponse(c))
                .toList();
    }

    public CourseDetailsResponse getCourseByIdForTeacher(Integer id) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        Optional<Course> course = courseRepository.findByIdAndTeacher(id, loggedInUser);

        if (course.isEmpty()) {
            throw new NotFoundException("Course not found");
        }

        CourseDetailsResponse courseDetailsResponse = CourseMapper.INSTANCE.courseToCourseDetailsResponse(course.get());
        Map<User, Integer> studentScores = new HashMap();
        course.get().getSubmissions().forEach(s -> {
            studentScores.put(s.getStudent(), s.getScore());
        });

        List<StudentDto> students = course.get().getStudents()
                .stream()
                .map(s -> new StudentDto(s.getId(), s.getName(), s.getEmail(), studentScores.get(s)))
                .toList();

        courseDetailsResponse.setStudents(students);

        return courseDetailsResponse;
    }

    public CourseResponse createCourse(CourseRequest courseRequest) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        validateCourseName(courseRequest.getName());

        Course course = new Course(courseRequest.getName(), courseRequest.getDescription(), courseRequest.getDeadline(), loggedInUser);

        courseRepository.save(course);

        return CourseMapper.INSTANCE.courseToCourseResponse(course);
    }

    @Transactional
    public CourseDetailsResponse editCourse(CourseDetailsRequest courseDetailsRequest) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        validateCourseName(courseDetailsRequest.getName());

        Optional<Course> course = courseRepository.findByIdAndTeacher(courseDetailsRequest.getId(), loggedInUser);

        if (course.isEmpty()) {
            throw new NotFoundException("Course not found");
        }

        List<User> existingUsers = userRepository.findAllByEmailIn(courseDetailsRequest.getStudentEmails());
        List<String> existingEmails = existingUsers.stream().map(User::getEmail).toList();
        courseDetailsRequest.getStudentEmails().removeAll(existingEmails);

        Optional<Role> studentRole = roleRepository.findByName(ERole.ROLE_STUDENT);

        if (studentRole.isEmpty()) {
            throw new InternalServerErrorException("User role not found");
        }

        List<String> invalidEmails = courseDetailsRequest.getStudentEmails()
                .stream()
                .filter(e -> !EmailValidator.validateEmail(e))
                .toList();

        if (!invalidEmails.isEmpty()) {
            throw new BadRequestException("Invalid email(s): " + String.join(", ", invalidEmails));
        }

        Course storedCourse = course.get();

        Map<String, String> newUserEmailAndPassword = new HashMap();
        List<User> newStudents = courseDetailsRequest.getStudentEmails()
                .stream()
                .map(e -> {
                    String password = RandomStringGenerator.generate(10);
                    newUserEmailAndPassword.put(e, password);
                    return new User(null, e, passwordEncoder.encode(password), Set.of(studentRole.get()));
                })
                .toList();

        userRepository.saveAll(newStudents);

        existingUsers.addAll(newStudents);

        storedCourse.setDeadline(courseDetailsRequest.getDeadline());
        storedCourse.setName(courseDetailsRequest.getName());
        storedCourse.setDescription(courseDetailsRequest.getDescription());
        storedCourse.setStudents(existingUsers);
        courseRepository.save(storedCourse);

        newUserEmailAndPassword.entrySet().forEach(e -> {
            sendMail.sendSimpleMessage(
                    e.getKey(),
                    "Registration to " + storedCourse.getName() + " language course",
                    "Hi! "
                            + storedCourse.getTeacher().getName()
                            + "registered you to "
                            + storedCourse.getName()
                            + " language course! You can log in with your email and this password: "
                            + e.getValue()
            );
        });

        return CourseMapper.INSTANCE.courseToCourseDetailsResponse(storedCourse);
    }

    private void validateCourseName(String name) {
        if (name.length() < 3) {
            throw new BadRequestException("Error: Course name should be at least 3 characters");
        }
    }

    public void deleteCourse(Integer id) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        Optional<Course> course = courseRepository.findByIdAndTeacher(id, loggedInUser);

        if (course.isEmpty()) {
            throw new NotFoundException("Course not found");
        }

        courseRepository.delete(course.get());
    }

    public void submit(SubmissionRequest submissionRequest) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        Optional<User> user = userRepository.findById(loggedInUser.getId());

        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        Optional<Course> course = courseRepository.findByIdAndStudents(submissionRequest.getCourseId(), loggedInUser);

        if (course.isEmpty()) {
            throw new NotFoundException("Course not found");
        }

        Course storedCourse = course.get();

        storedCourse.getSubmissions().add(new Submission(user.get(), submissionRequest.getScore()));

        courseRepository.save(storedCourse);
    }

    public List<StudentCourseResponse> getAllCoursesForStudent() {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        List<Course> coursesByStudent = courseRepository.findCoursesByStudents(loggedInUser);

        return coursesByStudent
                .stream()
                .map(c -> CourseMapper.INSTANCE.courseToStudentCourseResponse(c))
                .toList();
    }
}
