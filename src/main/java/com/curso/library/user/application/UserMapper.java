package com.curso.library.user.application;

import com.curso.library.user.api.dto.UserResponse;
import com.curso.library.user.domain.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getName());
    }
}
