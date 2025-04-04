package com.elyashevich.user_service.api.mapper;

import com.elyashevich.user_service.api.dto.UserRequestDto;
import com.elyashevich.user_service.api.dto.UserResponseDto;
import com.elyashevich.user_service.domain.entity.User;

import java.util.List;

public interface UserMapper {

    List<UserResponseDto> toDto(List<User> users);

    UserResponseDto toDto(User user);

    User toUser(UserRequestDto userRequestDto);
}
