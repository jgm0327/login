package com.jgm.login.user;

import lombok.Data;

@Data
public class AuthInfo {
    private String email;
    private String name;
    private String username;
    private String password;
}
