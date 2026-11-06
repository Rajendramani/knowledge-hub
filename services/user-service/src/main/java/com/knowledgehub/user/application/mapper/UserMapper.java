package com.knowledgehub.user.application.mapper;


import com.knowledgehub.user.application.dto.*;
import com.knowledgehub.user.domain.model.User;
import org.mapstruct.*;
import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(target = "email", expression = "java(user.getEmail().value())")
    @Mapping(target = "firstName", expression = "java(user.getName().firstName())")
    @Mapping(target = "lastName", expression = "java(user.getName().lastName())")
    @Mapping(target = "fullName", expression = "java(user.getName().fullName())")
    @Mapping(target = "role", expression = "java(user.getRole().name())")
    @Mapping(target = "status", expression = "java(user.getStatus().name())")
    UserResponse toResponse(User user);


    List<UserResponse> toResponseList(List<User> users);
}
