package io.github.stackpan.examia.server.service.impl;

import io.github.stackpan.examia.server.repository.UserRepository;
import io.github.stackpan.examia.server.service.ExamiaUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamiaUserServiceImpl implements ExamiaUserService, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        return user.get();
    }

}
