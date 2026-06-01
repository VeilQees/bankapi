package com.zaitsev.common.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferEvent {

    private String fromUser;

    private String toUser;

    private BigDecimal amount;
}