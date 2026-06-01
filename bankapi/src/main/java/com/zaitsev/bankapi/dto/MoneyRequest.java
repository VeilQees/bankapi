package com.zaitsev.bankapi.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MoneyRequest {

    @DecimalMin(
            value = "0.01",
            message = "Минимальная сумма 0.01"
    )

    @DecimalMax(
            value = "10000000.00",
            message = "Слишком большая сумма"
    )

    @Digits(
            integer = 10,
            fraction = 2,
            message = "Максимум 2 знака после запятой"
    )

    private BigDecimal amount;
}