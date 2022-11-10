package hu.bme.aut.viauma06.language_learning.mapper;

import hu.bme.aut.viauma06.language_learning.model.Course;
import hu.bme.aut.viauma06.language_learning.model.dto.response.CourseDetailsResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.CourseResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.StudentCourseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    CourseResponse courseToCourseResponse(Course course);

    CourseDetailsResponse courseToCourseDetailsResponse(Course course);

    StudentCourseResponse courseToStudentCourseResponse(Course course);
}
