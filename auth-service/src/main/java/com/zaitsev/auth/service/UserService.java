package com.zaitsev.auth.service;

import com.zaitsev.auth.dto.*;
import com.zaitsev.auth.entity.Role;
import com.zaitsev.auth.entity.User;
import com.zaitsev.auth.exception.BadRequestException;
import com.zaitsev.auth.exception.UnauthorizedException;
import com.zaitsev.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserResponse register(UserRegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BadRequestException("Phone already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .birthDate(request.getBirthDate())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .middleName(savedUser.getMiddleName())
                .birthDate(savedUser.getBirthDate())
                .username(savedUser.getUsername())
                .build();
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new UnauthorizedException("Неверный логин или пароль"));

        boolean matches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!matches) {
            throw new UnauthorizedException("Неверный логин или пароль");
        }

        String accessToken = jwtService.generateAccessToken(
                user.getUsername(),
                user.getRole().name()
        );

        String refreshToken =
                jwtService.generateRefreshToken(
                        user.getUsername()
                );

        return new LoginResponse(
                accessToken,
                refreshToken
        );
    }

    public String findUsernameByPhone(String phone) {

        User user = userRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new BadRequestException("User not found"));

        return user.getUsername();
    }

    public LoginResponse refreshToken(
            RefreshTokenRequest request
    ) {

        String refreshToken = request.getRefreshToken();

        if (!jwtService.isValid(refreshToken)) {
            throw new UnauthorizedException(
                    "Invalid refresh token"
            );
        }

        String username =
                jwtService.extractUsername(refreshToken);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UnauthorizedException("User not found"));

        String newAccessToken =
                jwtService.generateAccessToken(
                        user.getUsername(),
                        user.getRole().name()
                );

        String newRefreshToken =
                jwtService.generateRefreshToken(
                        user.getUsername()
                );

        return new LoginResponse(
                newAccessToken,
                newRefreshToken
        );
    }


}