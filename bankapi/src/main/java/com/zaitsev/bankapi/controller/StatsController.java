package com.zaitsev.bankapi.controller;

import com.zaitsev.bankapi.dto.TransactionStatsResponse;
import com.zaitsev.bankapi.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/transactions")
    public TransactionStatsResponse getStats(){

        return statsService.getStats();
    }
}