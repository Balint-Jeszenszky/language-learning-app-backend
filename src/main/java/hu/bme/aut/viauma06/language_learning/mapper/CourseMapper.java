package hu.bme.aut.viauma06.language_learning.mapper;

import hu.bme.aut.viauma06.language_learning.model.Course;
import hu.bme.aut.viauma06.language_learning.model.User;
import hu.bme.aut.viauma06.language_learning.model.dto.response.CourseDetailsResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.CourseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    CourseResponse courseToCourseResponse(Course course);

    CourseDetailsResponse courseToCourseDetailsResponse(Course course);

    default List<String> mapUserToEmail(List<User> users) {
        return users.stream().map(User::getEmail).collect(Collectors.toList());
    }
}
