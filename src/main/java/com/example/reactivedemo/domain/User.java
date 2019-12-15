package com.example.reactivedemo.domain;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private Organization organization;
}
