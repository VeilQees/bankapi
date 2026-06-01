package com.zaitsev.bankapi.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionRequest {

    private String type;
    private BigDecimal amount;
    private Long fromAccountId;
    private Long toAccountId;
    private Long categoryId;
}