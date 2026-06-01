package com.zaitsev.bankapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PhoneTransferRequest {

    @NotBlank(message = "Телефон обязателен")
    private String phone;

    @NotNull(message = "Сумма обязательна")
    @DecimalMin(
            value = "0.01",
            message = "Минимальная сумма 0.01"
    )
    @Digits(
            integer = 10,
            fraction = 2,
            message = "Максимум 2 знака после запятой"
    )
    private BigDecimal amount;

    private Long categoryId;
}