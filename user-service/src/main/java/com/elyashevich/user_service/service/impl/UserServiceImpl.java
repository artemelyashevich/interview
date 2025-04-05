package com.elyashevich.user_service.service.impl;

import com.elyashevich.user_service.domain.entity.User;
import com.elyashevich.user_service.domain.entity.UserRole;
import com.elyashevich.user_service.exception.ResourceNotFoundException;
import com.elyashevich.user_service.repository.UserRepository;
import com.elyashevich.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_WAS_NOT_FOUND_BY_ID_TEMPLATE = "User with id '%s' was not found";
    private static final String USER_WAS_NOT_FOUND_BY_EMAIL_TEMPLATE = "User with email '%s' was not found";

    private final UserRepository userRepository;
    private final ObjectFactory<UserServiceImpl> objectFactory;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value="UserService::findAll")
    public Page<User> findAll(Integer page, Integer size) {
        log.debug("Attempting to find all users");

        var pageable = PageRequest.of(page, size);

        var users = this.userRepository.findAll(pageable);

        log.info("Found {} users", users.getTotalElements());
        return users;
    }

    @Override
    @Cacheable(value = "UserService::findById", key = "#id")
    public User findById(Long id) {
        log.debug("Attempting to find user by id");

        var user = this.userRepository.findById(id).orElseThrow(
            () -> {
                var message = String.format(USER_WAS_NOT_FOUND_BY_ID_TEMPLATE, id);

                log.error(message);
                return new ResourceNotFoundException(message);
            }
        );

        log.info("User found: {}", user);
        return user;
    }

    @Override
    @Cacheable(value = "UserService::findByEmail", key = "#email")
    public User findByEmail(String email) {
        log.debug("Attempting to find user by email");

        var user = this.userRepository.findByEmail(email).orElseThrow(
            () -> {
                var message = String.format(USER_WAS_NOT_FOUND_BY_EMAIL_TEMPLATE, email);

                log.error(message);
                return new ResourceNotFoundException(message);
            }
        );

        log.info("User found: {}", user);
        return user;
    }

    @Override
    @Caching(
        put = {
            @CachePut(value = "UserService::findById", key="#result.id"),
            @CachePut(value = "UserService::findByEmail", key = "#result.email"),
            @CachePut(value = "UserService::findAll")
        }
    )
    public User save(User user) {
        log.debug("Attempting to save user");

        user.setRole(UserRole.GUEST);
        var result = this.userRepository.save(user);

        log.info("User saved: {}", result);
        return result;
    }

    @Override
    @Transactional
    @Caching(
        put = {
            @CachePut(value = "UserService::findById", key="#result.id"),
            @CachePut(value = "UserService::findByEmail", key = "#result.email"),
            @CachePut(value = "UserService::findAll")
        }
    )
    public User update(Long id, User user) {
        log.debug("Attempting to update user by id");

        var oldUser = this.objectFactory.getObject().findById(id);
        oldUser.setEmail(user.getEmail());

        if (user.getRole() != null) {
            oldUser.setRole(user.getRole());
        }

        var updatedUser = this.userRepository.save(oldUser);

        log.info("User updated: {}", updatedUser);
        return updatedUser;
    }

    @Override
    @Transactional
    @CacheEvict(value = "UserService::findById", key="#id")
    public void delete(Long id) {
        log.debug("Attempting to delete user by id");

        var user = this.objectFactory.getObject().findById(id);
        this.userRepository.delete(user);

        log.info("User deleted: {}", user);
    }
}
