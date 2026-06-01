package com.zaitsev.auth.controller;

import com.zaitsev.auth.dto.*;
import com.zaitsev.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public UserResponse register(
            @Valid @RequestBody UserRegisterRequest request
    ) {

        return userService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request
    ) {

        return userService.login(request);
    }

    @GetMapping("/username-by-phone")
    public String findUsernameByPhone(
            @RequestParam String phone
    ) {

        return userService.findUsernameByPhone(phone);
    }
    @PostMapping("/refresh")
    public LoginResponse refresh(
            @RequestBody RefreshTokenRequest request
    ) {

        return userService.refreshToken(request);
    }



}
