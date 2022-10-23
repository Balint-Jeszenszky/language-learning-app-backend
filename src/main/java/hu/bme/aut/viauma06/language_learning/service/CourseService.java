package hu.bme.aut.viauma06.language_learning.service;

import hu.bme.aut.viauma06.language_learning.controller.exceptions.BadRequestException;
import hu.bme.aut.viauma06.language_learning.controller.exceptions.InternalServerErrorException;
import hu.bme.aut.viauma06.language_learning.controller.exceptions.NotFoundException;
import hu.bme.aut.viauma06.language_learning.mapper.CourseMapper;
import hu.bme.aut.viauma06.language_learning.mapper.WordPairMapper;
import hu.bme.aut.viauma06.language_learning.model.*;
import hu.bme.aut.viauma06.language_learning.model.dto.request.CourseDetailsRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.request.WordPairRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.response.CourseDetailsResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.CourseResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.WordPairResponse;
import hu.bme.aut.viauma06.language_learning.repository.*;
import hu.bme.aut.viauma06.language_learning.security.service.LoggedInUserService;
import hu.bme.aut.viauma06.language_learning.service.util.EmailValidator;
import hu.bme.aut.viauma06.language_learning.service.util.RandomStringGenerator;
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
    private WordPairRepository wordPairRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CourseStudentAccessRepository courseStudentAccessRepository;

    @Autowired
    private LoggedInUserService loggedInUserService;

    @Autowired
    private SendMail sendMail;

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
        List<String> existingEmails = existingUsers.stream().map(User::getEmail).toList();
        courseDetailsRequest.getStudentEmails().removeAll(existingEmails);

        Optional<Role> studentRole = roleRepository.findByName(ERole.ROLE_STUDENT);

        if (studentRole.isEmpty()) {
            throw new InternalServerErrorException("User role not found");
        }

        List<String> invalidEmails = courseDetailsRequest.getStudentEmails()
                .stream()
                .filter(e -> !EmailValidator.validateEmail(e))
                .collect(Collectors.toList());

        if (!invalidEmails.isEmpty()) {
            throw new BadRequestException("Invalid email(s): " + String.join(", ", invalidEmails));
        }

        Course storedCourse = course.get();

        List<User> newStudents = courseDetailsRequest.getStudentEmails()
                .stream()
                .map(e -> new User(null, e, null, Set.of(studentRole.get())))
                .collect(Collectors.toList());

        newStudents.forEach(s -> {
            s.setCourses(List.of(storedCourse));
            s.setCourseAccess(List.of(new CourseStudentAccess(s, storedCourse, RandomStringGenerator.generate(10))));
        });

        userRepository.saveAllAndFlush(newStudents);

        newStudents.forEach(s -> {
            s.getCourseAccess().get(0).setId(new CourseStudentAccessKey(s.getId(), storedCourse.getId()));
        });

        existingUsers.addAll(newStudents);

        storedCourse.setDeadline(courseDetailsRequest.getDeadline());
        storedCourse.setName(courseDetailsRequest.getName());
        storedCourse.setStudents(existingUsers);
        courseRepository.save(storedCourse);

        List<CourseStudentAccess> courseStudentAccessList = existingUsers.stream().map(u -> {
            CourseStudentAccess courseStudentAccess = u.getCourseAccess().stream().filter(
                    c -> c.getCourse().equals(storedCourse)
            ).findFirst().orElseThrow(() -> new NotFoundException("Course not found"));
            if (courseStudentAccess.getAccessKey() == null) {
                courseStudentAccess.setAccessKey(RandomStringGenerator.generate(10));
            }
            return courseStudentAccess;
        }).collect(Collectors.toList());

        courseStudentAccessRepository.saveAll(courseStudentAccessList);

        // TODO separate thread
//        newStudents.forEach(s -> {
//            sendMail.sendSimpleMessage(
//                    s.getEmail(),
//                    "Registration to " + storedCourse.getName() + " language course",
//                    "Hi! "
//                            + storedCourse.getTeacher().getName()
//                            + "registered you to "
//                            + storedCourse.getName()
//                            + " language course! You can log in with your email and this password: "
//                            + "TODO" // TODO password
//            );
//        });

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

    public List<WordPairResponse> getWordPairsByCourseId(Integer id) {
        User loggedInUser = loggedInUserService.getLoggedInUser();
        Optional<Course> course = courseRepository.findByIdAndTeacher(id, loggedInUser);
        if (course.isEmpty()) {
            throw new NotFoundException("Course not found");
        }

        List<WordPair> wordPairs = wordPairRepository.findAllByCourseId(id);

        return wordPairs.stream().map(w -> WordPairMapper.INSTANCE.wordPairToWordPairResponse(w)).collect(Collectors.toList());
    }

    public List<WordPairResponse> updateWordPairs(Integer id, List<WordPairRequest> wordPairsRequest) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        Optional<Course> course = courseRepository.findByIdAndTeacher(id, loggedInUser);

        if (course.isEmpty()) {
            throw new NotFoundException("Course not found");
        }

        Course storedCourse = course.get();

        List<WordPair> newWords = wordPairsRequest.stream().map(w -> new WordPair(w.getWord(), w.getTranslation(), w.getMetadata())).collect(Collectors.toList());
        newWords.forEach(w -> w.setCourse(storedCourse));

        wordPairRepository.deleteAllInBatch(storedCourse.getWords());
        storedCourse.setWords(newWords);
        courseRepository.save(storedCourse);

        return newWords.stream().map(w -> WordPairMapper.INSTANCE.wordPairToWordPairResponse(w)).collect(Collectors.toList());
    }
}
