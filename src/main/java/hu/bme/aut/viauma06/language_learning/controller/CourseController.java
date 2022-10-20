package hu.bme.aut.viauma06.language_learning.controller;

import hu.bme.aut.viauma06.language_learning.model.dto.request.CourseDetailsRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.request.CourseRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.request.WordPairRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.response.CourseDetailsResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.CourseResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.WordPairResponse;
import hu.bme.aut.viauma06.language_learning.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/teacher/all")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<CourseResponse>> getAllCoursesForTeacher() {
        List<CourseResponse> allCoursesForTeacher = courseService.getAllCoursesForTeacher();

        return new ResponseEntity(allCoursesForTeacher, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CourseRequest courseRequest) {
        CourseResponse course = courseService.createCourse(courseRequest.getName());

        return new ResponseEntity(course, HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<CourseDetailsResponse> editCourse(@RequestBody CourseDetailsRequest courseDetailsRequest) {
        CourseDetailsResponse course = courseService.editCourse(courseDetailsRequest);

        return new ResponseEntity(course, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity editCourse(@PathVariable("id") Integer id) {
        courseService.deleteCourse(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/words")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<List<WordPairResponse>> getWordPairsByCourseId(@PathVariable("id") Integer id) {
        List<WordPairResponse> wordPairs = courseService.getWordPairsByCourseId(id);

        return new ResponseEntity(wordPairs, HttpStatus.OK);
    }

    @PutMapping("/{id}/words")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<WordPairResponse>> updateWordPairsByCourseId(@PathVariable("id") Integer id, @RequestBody List<WordPairRequest> wordPairsRequest) {
        List<WordPairResponse> wordPairs = courseService.updateWordPairs(id, wordPairsRequest);

        return new ResponseEntity(wordPairs, HttpStatus.OK);
    }
}
