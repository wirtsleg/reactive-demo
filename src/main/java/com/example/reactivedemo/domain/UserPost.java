package com.example.reactivedemo.domain;

import lombok.Data;

@Data
public class UserPost {
    private Long id;
    private Long userId;
    private String subject;
    private String body;
}
