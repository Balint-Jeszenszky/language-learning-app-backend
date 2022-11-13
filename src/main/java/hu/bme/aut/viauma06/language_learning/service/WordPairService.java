package hu.bme.aut.viauma06.language_learning.service;

import hu.bme.aut.viauma06.language_learning.controller.exceptions.BadRequestException;
import hu.bme.aut.viauma06.language_learning.controller.exceptions.NotFoundException;
import hu.bme.aut.viauma06.language_learning.mapper.WordPairMapper;
import hu.bme.aut.viauma06.language_learning.model.Course;
import hu.bme.aut.viauma06.language_learning.model.User;
import hu.bme.aut.viauma06.language_learning.model.WordPair;
import hu.bme.aut.viauma06.language_learning.model.dto.request.WordPairRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.response.StudentWordPairResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.WordPairResponse;
import hu.bme.aut.viauma06.language_learning.repository.CourseRepository;
import hu.bme.aut.viauma06.language_learning.repository.UserRepository;
import hu.bme.aut.viauma06.language_learning.repository.WordPairRepository;
import hu.bme.aut.viauma06.language_learning.security.service.LoggedInUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WordPairService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private WordPairRepository wordPairRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoggedInUserService loggedInUserService;

    public List<WordPairResponse> getWordPairsByCourseId(Integer id) {
        User loggedInUser = loggedInUserService.getLoggedInUser();
        Optional<Course> course = courseRepository.findByIdAndTeacher(id, loggedInUser);
        if (course.isEmpty()) {
            throw new NotFoundException("Course not found");
        }

        List<WordPair> wordPairs = wordPairRepository.findAllByCourseId(id);

        return wordPairs.stream().map(WordPairMapper.INSTANCE::wordPairToWordPairResponse).toList();
    }

    @Transactional
    public List<WordPairResponse> updateWordPairs(Integer id, List<WordPairRequest> wordPairsRequest) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        if (wordPairsRequest.stream().anyMatch(w -> w.getWord().isEmpty() || w.getTranslation().isEmpty())) {
            throw new BadRequestException("Error: Word should not be empty");
        }

        Optional<Course> course = courseRepository.findByIdAndTeacher(id, loggedInUser);

        if (course.isEmpty()) {
            throw new NotFoundException("Course not found");
        }

        Course storedCourse = course.get();

        List<WordPair> words = wordPairsRequest.stream().map(w -> {
            WordPair wordPair = storedCourse.getWords()
                    .stream()
                    .filter(sw -> sw.getId().equals(w.getId()))
                    .findFirst()
                    .orElse(new WordPair());
            wordPair.setCourse(storedCourse);
            wordPair.setWord(w.getWord());
            wordPair.setTranslation(w.getTranslation());

            return wordPair;
        }).toList();

        storedCourse.getWords().removeAll(words);
        wordPairRepository.deleteAllInBatch(storedCourse.getWords());

        storedCourse.getWords().clear();
        storedCourse.getWords().addAll(words);
        wordPairRepository.saveAll(words);

        return words.stream().map(WordPairMapper.INSTANCE::wordPairToWordPairResponse).toList();
    }

    public void saveWordPair(Integer id) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        List<Course> coursesByStudent = courseRepository.findCoursesByStudents(loggedInUser);

        Optional<WordPair> wordPair = wordPairRepository.findByCourseInAndId(coursesByStudent, id);

        if (wordPair.isEmpty()) {
            throw new NotFoundException("Word pair not found");
        }

        Optional<User> user = userRepository.findById(loggedInUser.getId());

        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        User storedUser = user.get();
        storedUser.getSavedWordPairs().add(wordPair.get());

        userRepository.save(storedUser);
    }

    public void deleteWordPair(Integer id) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        Optional<User> user = userRepository.findById(loggedInUser.getId());

        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        User storedUser = user.get();
        storedUser.getSavedWordPairs().removeIf(w -> w.getId().equals(id));

        userRepository.save(storedUser);
    }

    @Transactional
    public List<StudentWordPairResponse> getWordPairsAndSavedStatusByCourseId(Integer id){
        User loggedInUser = loggedInUserService.getLoggedInUser();
        Optional<Course> course = courseRepository.findByIdAndStudents(id, loggedInUser);
        if (course.isEmpty()) {
            throw new NotFoundException("Course not found");
        }

        List<WordPair> wordPairs = wordPairRepository.findAllByCourseId(id);

        List<WordPair> savedWordPairs = userRepository.findById(loggedInUser.getId()).get().getSavedWordPairs();

        return wordPairs.stream().map(w -> WordPairMapper.INSTANCE.wordPairToStudentWordPairResponse(w, savedWordPairs.contains(w))).toList();
    }
}
