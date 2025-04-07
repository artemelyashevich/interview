package com.elyashevich.user_service.api.mapper.impl;

import com.elyashevich.user_service.api.dto.UserRequestDto;
import com.elyashevich.user_service.api.dto.UserResponseDto;
import com.elyashevich.user_service.api.mapper.UserMapper;
import com.elyashevich.user_service.domain.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public List<UserResponseDto> toDto(List<User> users) {
        return users.stream()
            .map(this::toDto)
            .toList();
    }

    @Override
    public UserResponseDto toDto(User user) {
        return new UserResponseDto(user.getId(), user.getEmail(), user.getRole());
    }

    @Override
    public User toUser(UserRequestDto userRequestDto) {
        return User.builder()
            .email(userRequestDto.email())
            .password(userRequestDto.password())
            .build();
    }

    @Override
    public User toUser(UserResponseDto userResponseDto) {
        return User.builder()
            .id(userResponseDto.id())
            .email(userResponseDto.email())
            .role(userResponseDto.role())
            .build();
    }
}
