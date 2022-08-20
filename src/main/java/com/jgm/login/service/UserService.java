package com.jgm.login.service;

import com.jgm.login.repository.UserRepository;
import com.jgm.login.user.AuthInfo;
import com.jgm.login.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(@Autowired UserRepository userRepository,
                       @Autowired BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void join(AuthInfo authInfo) {
        User user = userRepository.findByUsername(authInfo.getUsername());
        if (user == null) {
            user = User.builder()
                    .email(authInfo.getEmail())
                    .username(authInfo.getUsername())
                    .password(passwordEncoder.encode(authInfo.getPassword()))
                    .role("ROLE_USER")
                    .build();

            userRepository.save(user);
        }
    }

}
