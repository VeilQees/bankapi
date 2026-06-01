package com.zaitsev.bankapi.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class DepositRequest {
    private Long accountId;
    private BigDecimal amount;
    private Long categoryId;
}
