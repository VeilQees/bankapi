package com.zaitsev.bankapi.controller;

import com.zaitsev.bankapi.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.zaitsev.bankapi.dto.TransactionResponse;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<TransactionResponse> getTransactions(
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end
    ) {
        return transactionService.getMyTransactions(start, end);
    }
}