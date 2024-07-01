package io.github.stackpan.examia.server.service.impl;

import io.github.stackpan.examia.server.entity.User;
import io.github.stackpan.examia.server.exception.ResourceNotFoundException;
import io.github.stackpan.examia.server.repository.UserRepository;
import io.github.stackpan.examia.server.service.ExamiaUserService;
import io.github.stackpan.examia.server.util.UUIDs;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExamiaUserServiceImpl implements ExamiaUserService, UserDetailsService {

    private final String resourceContextName = User.class.getSimpleName();

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        return user.get();
    }

    @Override
    public User getById(String id) {
        return userRepository.findById(validateUuid(id))
                .orElseThrow(() -> new ResourceNotFoundException(resourceContextName, id));
    }

    private UUID validateUuid(String id) {
        return UUIDs.fromString(id).orElseThrows(() -> new ResourceNotFoundException(resourceContextName, id));
    }
}
