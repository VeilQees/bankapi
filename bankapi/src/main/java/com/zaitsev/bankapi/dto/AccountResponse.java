package com.zaitsev.bankapi.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class AccountResponse {
    private Long id;
    private String name;
    private BigDecimal balance;
    private boolean primary; // 🔥 ДОБАВЬ

}