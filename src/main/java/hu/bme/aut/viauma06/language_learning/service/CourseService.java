package hu.bme.aut.viauma06.language_learning.service;

import hu.bme.aut.viauma06.language_learning.controller.exceptions.BadRequestException;
import hu.bme.aut.viauma06.language_learning.controller.exceptions.InternalServerErrorException;
import hu.bme.aut.viauma06.language_learning.controller.exceptions.NotFoundException;
import hu.bme.aut.viauma06.language_learning.mapper.CourseMapper;
import hu.bme.aut.viauma06.language_learning.mapper.WordPairMapper;
import hu.bme.aut.viauma06.language_learning.model.*;
import hu.bme.aut.viauma06.language_learning.model.dto.request.CourseDetailsRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.request.CourseRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.request.WordPairRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.response.CourseDetailsResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.CourseResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.WordPairResponse;
import hu.bme.aut.viauma06.language_learning.repository.*;
import hu.bme.aut.viauma06.language_learning.security.service.LoggedInUserService;
import hu.bme.aut.viauma06.language_learning.service.util.EmailValidator;
import hu.bme.aut.viauma06.language_learning.service.util.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
                .collect(Collectors.toList());
    }

    public CourseResponse getCourseByIdForTeacher(Integer id) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        Optional<Course> course = courseRepository.findByIdAndTeacher(id, loggedInUser);

        if (course.isEmpty()) {
            throw new NotFoundException("Course not found");
        }

        return CourseMapper.INSTANCE.courseToCourseResponse(course.get());
    }

    public CourseResponse createCourse(CourseRequest courseRequest) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        Course course = new Course(courseRequest.getName(), courseRequest.getDeadline(), loggedInUser);

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

        Map<String, String> newUserEmailAndPassword = new HashMap();
        List<User> newStudents = courseDetailsRequest.getStudentEmails()
                .stream()
                .map(e -> {
                    String password = RandomStringGenerator.generate(10);
                    newUserEmailAndPassword.put(e, password);
                    return new User(null, e, passwordEncoder.encode(password), Set.of(studentRole.get()));
                })
                .collect(Collectors.toList());

        userRepository.saveAll(newStudents);

        existingUsers.addAll(newStudents);

        storedCourse.setDeadline(courseDetailsRequest.getDeadline());
        storedCourse.setName(courseDetailsRequest.getName());
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
