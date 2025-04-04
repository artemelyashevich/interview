package com.elyashevich.user_service.api.controller;

import com.elyashevich.user_service.api.dto.UserRequestDto;
import com.elyashevich.user_service.api.dto.UserResponseDto;
import com.elyashevich.user_service.api.mapper.UserMapper;
import com.elyashevich.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserResponseDto> findAll(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "5", required = false) int size
    ) {
        var users = userService.findAll(page, size);
        return users.get().map(this.userMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public UserResponseDto findById(@PathVariable("id") long id) {
        var user = this.userService.findById(id);
        return this.userMapper.toDto(user);
    }

    @GetMapping("/{email}")
    public UserResponseDto findByEmail(@PathVariable("email") String email) {
        var user = this.userService.findByEmail(email);
        return this.userMapper.toDto(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto save(@RequestBody UserRequestDto userRequestDto) {
        var user = this.userService.save(this.userMapper.toUser(userRequestDto));
        return this.userMapper.toDto(user);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto update(@PathVariable("id") long id, @RequestBody UserRequestDto userRequestDto) {
        var user = this.userService.update(id, this.userMapper.toUser(userRequestDto));
        return this.userMapper.toDto(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long id) {
        this.userService.delete(id);
    }
}
