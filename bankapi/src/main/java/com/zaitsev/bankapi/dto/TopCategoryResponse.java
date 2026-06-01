package com.zaitsev.bankapi.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TopCategoryResponse {

    private String categoryName;
    private BigDecimal total;
}