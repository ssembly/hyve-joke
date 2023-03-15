package com.hyve.fun.service;

import com.hyve.fun.domain.User;
import com.hyve.fun.errors.UserNotAuthorizedException;
import com.hyve.fun.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public abstract class BaseService {

    private final UserRepository userRepository;

    public Long getUserId(String user) {
        if (NumberUtils.isCreatable(user)) {
            final Long id = NumberUtils.createLong(user);
            final Optional<User> u = userRepository.findById(id);
            if (u.isEmpty()) throw new UserNotAuthorizedException();
            return id;
        } else {
            final Optional<User> u = userRepository.findByUsername(user);
            if (u.isEmpty()) throw new UserNotAuthorizedException();
            return u.get().getId();
        }
    }
}
