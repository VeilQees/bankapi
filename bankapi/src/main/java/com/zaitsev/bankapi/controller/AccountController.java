package com.zaitsev.bankapi.controller;

import com.zaitsev.bankapi.dto.*;
import com.zaitsev.bankapi.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public AccountResponse createAccount(
            @Valid @RequestBody CreateAccountRequest request
    ) {

        return accountService.createAccount(request);
    }

    // 🔥 ПОЛУЧЕНИЕ СЧЕТОВ
    @GetMapping
    public List<AccountResponse> getAccounts() {

        return accountService.getAccounts();
    }

    @PostMapping("/deposit")
    public void deposit(
            @Valid @RequestBody DepositRequest request
    ) {

        accountService.deposit(
                request.getAccountId(),
                request.getAmount()
        );
    }

    @PostMapping("/transfer")
    public void transferBetweenAccounts(
            @RequestParam Long fromId,
            @RequestParam Long toId,
            @RequestParam BigDecimal amount
    ) {

        accountService.transferBetweenAccounts(
                fromId,
                toId,
                amount
        );
    }

    @PostMapping("/transfer/by-name")
    public void transferByName(
            @RequestBody TransferRequest request
    ) {

        accountService.transferBetweenAccountsByName(
                request.getFromName(),
                request.getToName(),
                request.getAmount(),
                request.getCategoryId()
        );
    }

    @PostMapping("/transfer/phone")
    public void transferByPhone(
            @Valid @RequestBody PhoneTransferRequest request
    ) {

        accountService.transferByPhone(
                request.getPhone(),
                request.getAmount(),
                request.getCategoryId()
        );
    }

}