package com.zaitsev.bankapi.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class TransactionStatsResponse implements Serializable {

    private BigDecimal totalIncome;   // пополнения
    private BigDecimal totalExpense;  // расходы
    private BigDecimal net;           // итог (доход - расход)
    private static final long serialVersionUID = 1L;
}