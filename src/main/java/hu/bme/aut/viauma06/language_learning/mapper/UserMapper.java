package hu.bme.aut.viauma06.language_learning.mapper;

import hu.bme.aut.viauma06.language_learning.model.Role;
import hu.bme.aut.viauma06.language_learning.model.User;
import hu.bme.aut.viauma06.language_learning.model.dto.response.UserDetailsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDetailsResponse userToUserDetailsResponse(User user);

    default List<String> mapRoles(Set<Role> roles) {
        return roles.stream().map(role -> role.getName().name()).toList();
    }
}