package com.zaitsev.bankapi.service;

import com.zaitsev.bankapi.entity.Transaction;
import com.zaitsev.bankapi.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.zaitsev.bankapi.dto.TransactionResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<TransactionResponse> getMyTransactions(
            String start,
            String end
    ) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        List<Transaction> transactions;

        if (start != null && end != null) {

            LocalDateTime startDate = LocalDateTime.parse(start);

            LocalDateTime endDate = LocalDateTime.parse(end);

            transactions = transactionRepository.findAllByUserAndDateBetween(
                    username,
                    startDate,
                    endDate
            );

        } else {

            transactions = transactionRepository.findByFromUserOrToUser(
                    username,
                    username
            );
        }

        return transactions.stream()

                .map(t -> TransactionResponse.builder()

                        .id(t.getId())

                        .fromUser(t.getFromUser())

                        .toUser(t.getToUser())

                        .amount(t.getAmount())

                        .category(
                                t.getCategory() != null
                                        ? t.getCategory().getName()
                                        : null
                        )

                        .createdAt(t.getCreatedAt())

                        .build())

                .toList();
    }
}