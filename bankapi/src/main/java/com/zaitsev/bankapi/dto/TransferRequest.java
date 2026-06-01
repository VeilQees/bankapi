package com.zaitsev.bankapi.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferRequest {

    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private Long categoryId;
    private String phone;
    private String toName;
    private String fromName;


}