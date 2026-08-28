package com.roh.jwtApplication.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public String getUsers() {
        return "Users retrieved successfully";
    }

    @PostMapping
    public String createUser() {
        return "User created successfully";
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id) {
        return "User " + id + " updated successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        return "User " + id + " deleted successfully";
    }
}
