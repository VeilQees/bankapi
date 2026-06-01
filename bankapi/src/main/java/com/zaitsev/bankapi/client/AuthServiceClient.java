package com.zaitsev.bankapi.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AuthServiceClient {

    private final RestTemplate restTemplate;

    public String getUsernameByPhone(String phone) {

        String url =
                "http://auth-service:8082/auth/username-by-phone?phone="
                        + phone;

        return restTemplate.getForObject(url, String.class);
    }
}