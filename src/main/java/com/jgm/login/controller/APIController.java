package com.jgm.login.controller;

import com.jgm.login.service.UserService;
import com.jgm.login.token.TokenServiceImpl;
import com.jgm.login.user.AuthInfo;
import com.jgm.login.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class APIController {

    private final TokenServiceImpl tokenService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("hello");
    }

    @PostMapping("/join")
    public void join(@RequestBody AuthInfo authInfo) {
        userService.join(authInfo);
    }

}
