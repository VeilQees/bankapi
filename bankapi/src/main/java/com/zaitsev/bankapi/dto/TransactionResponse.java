package com.zaitsev.bankapi.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TransactionResponse(

        Long id,

        String fromUser,

        String toUser,

        BigDecimal amount,

        String category,

        LocalDateTime createdAt
) {}