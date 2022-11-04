package hu.bme.aut.viauma06.language_learning.controller;

import hu.bme.aut.viauma06.language_learning.model.dto.request.CourseDetailsRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.request.CourseRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.request.SubmissionRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.response.CourseDetailsResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.CourseResponse;
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

    @GetMapping("/all")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<CourseResponse>> getAllCoursesForTeacher() {
        List<CourseResponse> allCoursesForTeacher = courseService.getAllCoursesForTeacher();

        return new ResponseEntity(allCoursesForTeacher, HttpStatus.OK);
    }

    @GetMapping("/teacher/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<CourseDetailsResponse> getCourseByIdForTeacher(@PathVariable("id") Integer id) {
        CourseDetailsResponse courseResponse = courseService.getCourseByIdForTeacher(id);

        return new ResponseEntity(courseResponse, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CourseRequest courseRequest) {
        CourseResponse course = courseService.createCourse(courseRequest);

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
    public ResponseEntity deleteCourse(@PathVariable("id") Integer id) {
        courseService.deleteCourse(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<CourseResponse> submit(@RequestBody SubmissionRequest submissionRequest) {
        courseService.submit(submissionRequest);

        return new ResponseEntity(HttpStatus.CREATED);
    }
}
