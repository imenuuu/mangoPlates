package com.example.demo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UsersDto implements Serializable {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final String phoneNumber;
}
