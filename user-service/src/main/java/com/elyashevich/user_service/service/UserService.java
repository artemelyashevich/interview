package com.elyashevich.user_service.service;

import com.elyashevich.user_service.domain.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    Page<User> findAll(Integer page, Integer size);

    User findById(Long id);

    User findByEmail(String email);

    User save(User user);

    User update(Long id, User user);

    void delete(Long id);
}
