package com.zaitsev.bankapi.service;

import com.zaitsev.bankapi.dto.TransactionStatsResponse;
import com.zaitsev.bankapi.entity.Transaction;
import com.zaitsev.bankapi.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final TransactionRepository transactionRepository;

    @Cacheable("stats")
    public TransactionStatsResponse getStats(){

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        List<Transaction> transactions =
                transactionRepository
                        .findByFromUserOrToUser(
                                username,
                                username
                        );

        BigDecimal income = BigDecimal.ZERO;

        BigDecimal expense = BigDecimal.ZERO;

        for(Transaction t : transactions){

            if(username.equals(t.getToUser())){
                income = income.add(t.getAmount());
            }

            if(username.equals(t.getFromUser())){
                expense = expense.add(t.getAmount());
            }
        }

        return TransactionStatsResponse.builder()

                .totalIncome(income)

                .totalExpense(expense)

                .net(income.subtract(expense))

                .build();
    }
}