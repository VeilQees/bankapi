package com.zaitsev.bankapi.service;

import com.zaitsev.bankapi.dto.AccountResponse;
import com.zaitsev.bankapi.dto.CreateAccountRequest;
import com.zaitsev.bankapi.entity.Account;
import com.zaitsev.bankapi.entity.Category;
import com.zaitsev.bankapi.entity.Transaction;
import com.zaitsev.bankapi.exception.BadRequestException;
import com.zaitsev.bankapi.exception.ForbiddenException;
import com.zaitsev.bankapi.exception.NotFoundException;
import com.zaitsev.bankapi.repository.AccountRepository;
import com.zaitsev.bankapi.repository.CategoryRepository;
import com.zaitsev.bankapi.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.zaitsev.bankapi.client.AuthServiceClient;
import com.zaitsev.common.dto.TransferEvent;
import com.zaitsev.bankapi.kafka.TransferProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final AuthServiceClient authServiceClient;
    private final TransferProducer transferProducer;


    // 🔥 перевод по имени
    @Transactional
    public void transferBetweenAccountsByName(
            String fromName,
            String toName,
            BigDecimal amount,
            Long categoryId
    ) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException(
                    "Сумма должна быть больше 0"
            );
        }

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Account from = accountRepository
                .findByNameAndUsernameForUpdate(fromName, username)
                .orElseThrow(() -> new NotFoundException("Счет отправителя не найден"));

        Account to = accountRepository
                .findByNameAndUsernameForUpdate(toName, username)
                .orElseThrow(() -> new NotFoundException("Счет получателя не найден"));

        if (from.getBalance().compareTo(amount) < 0) {
            throw new BadRequestException("Недостаточно средств");
        }

        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));

        from.setBalance(from.getBalance().subtract(amount));

        to.setBalance(to.getBalance().add(amount));

        accountRepository.save(from);
        accountRepository.save(to);

        Transaction transaction = new Transaction();

        transaction.setAmount(amount);
        transaction.setFromUser(username);
        transaction.setToUser(username);
        transaction.setCreatedAt(LocalDateTime.now());

        transaction.setCategory(category);

        transactionRepository.save(transaction);

        log.info(
                "Internal transfer completed: user={}, from={}, to={}, amount={}",
                username,
                fromName,
                toName,
                amount
        );
    }

    // 🔥 перевод по телефону
    @Transactional
    public void transferByPhone(
            String phone,
            BigDecimal amount,
            Long categoryId
    ) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException(
                    "Сумма должна быть больше 0"
            );
        }

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Account from = accountRepository
                .findPrimaryAccountForUpdate(username)
                .orElseThrow(() ->
                        new NotFoundException("Основной счет не найден"));

        String targetUsername = authServiceClient
                .getUsernameByPhone(phone);

        Account to = accountRepository
                .findPrimaryAccountForUpdate(targetUsername)
                .orElseThrow(() ->
                        new NotFoundException("У получателя нет основного счета"));

        if (from.getBalance().compareTo(amount) < 0) {
            throw new BadRequestException("Недостаточно средств");
        }

        Category category = null;

        if (categoryId != null) {

            category = categoryRepository
                    .findByIdForUpdate(categoryId)
                    .orElseThrow(() ->
                            new NotFoundException("Категория не найдена"));
        }

        from.setBalance(from.getBalance().subtract(amount));

        to.setBalance(to.getBalance().add(amount));

        accountRepository.save(from);
        accountRepository.save(to);

        Transaction transaction = Transaction.builder()
                .fromUser(username)
                .toUser(targetUsername)
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .category(category)
                .build();

        transactionRepository.save(transaction);

        log.info(
                "Transfer completed: from={}, to={}, amount={}",
                username,
                targetUsername,
                amount
        );
    }

    // история переводоы
    public List<Transaction> getTransactions(String start, String end) {

        String username = getCurrentUsername();

        if (start != null && end != null) {
            return transactionRepository
                    .findAllByUserAndDateBetween(
                            username,
                            LocalDateTime.parse(start),
                            LocalDateTime.parse(end)
                    );
        }

        return transactionRepository.findByFromUserOrToUser(username, username);
    }

    @Transactional
    public void deposit(
            Long accountId,
            BigDecimal amount
    ) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException(
                    "Сумма должна быть больше 0"
            );
        }

        String username = getCurrentUsername();

        Account account = accountRepository
                .findByIdForUpdate(accountId)
                .orElseThrow(() ->
                        new NotFoundException("Счет не найден"));

        if (!account.getUsername().equals(username)) {
            throw new ForbiddenException(
                    "Это не ваш счет"
            );
        }

        account.setBalance(
                account.getBalance().add(amount)
        );

        accountRepository.save(account);

        log.info(
                "Deposit completed: user={}, accountId={}, amount={}",
                username,
                accountId,
                amount
        );
    }

    @Transactional
    public AccountResponse createAccount(
            CreateAccountRequest request
    ) {

        String username = getCurrentUsername();

        boolean hasPrimary =
                accountRepository.findByUsernameAndPrimaryTrue(username)
                        .isPresent();

        if(accountRepository.existsByUsernameAndNameIgnoreCase(
                username,
                request.getName()
        )){
            throw new BadRequestException(
                    "Счет с таким названием уже существует"
            );
        }

        Account account = Account.builder()
                .name(request.getName())
                .balance(BigDecimal.ZERO)
                .username(username)
                .primary(!hasPrimary)
                .build();

        Account saved = accountRepository.save(account);

        log.info(
                "Account created: user={}, accountName={}",
                username,
                saved.getName()
        );

        return AccountResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .balance(saved.getBalance())
                .primary(saved.getPrimary())
                .build();
    }

    @Transactional
    public void transferBetweenAccounts(
            Long fromId,
            Long toId,
            BigDecimal amount
    ) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException(
                    "Сумма должна быть больше 0"
            );
        }

        if (fromId.equals(toId)) {
            throw new BadRequestException(
                    "Нельзя переводить на тот же счет"
            );
        }

        String username = getCurrentUsername();

        Long firstLockId =
                Math.min(fromId, toId);

        Long secondLockId =
                Math.max(fromId, toId);

        Account first =
                accountRepository.findByIdForUpdate(firstLockId)
                        .orElseThrow(() ->
                                new NotFoundException("Счет не найден"));

        Account second =
                accountRepository.findByIdForUpdate(secondLockId)
                        .orElseThrow(() ->
                                new NotFoundException("Счет не найден"));

        Account from =
                fromId.equals(firstLockId)
                        ? first
                        : second;

        Account to =
                toId.equals(secondLockId)
                        ? second
                        : first;

        if (!from.getUsername().equals(username)
                || !to.getUsername().equals(username)) {

            throw new ForbiddenException(
                    "Можно переводить только между своими счетами"
            );
        }

        if (from.getBalance().compareTo(amount) < 0) {

            throw new BadRequestException(
                    "Недостаточно средств"
            );
        }

        from.setBalance(
                from.getBalance().subtract(amount)
        );

        to.setBalance(
                to.getBalance().add(amount)
        );

        accountRepository.save(from);

        accountRepository.save(to);

        log.info(
                "Transfer between accounts: user={}, fromId={}, toId={}, amount={}",
                username,
                fromId,
                toId,
                amount
        );
    }

    private String getCurrentUsername() {

        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAccounts() {

        String username = getCurrentUsername();

        return accountRepository.findAllByUsername(username)

                .stream()

                .map(acc -> AccountResponse.builder()

                        .id(acc.getId())

                        .name(acc.getName())

                        .balance(acc.getBalance())

                        .primary(acc.getPrimary())

                        .build())

                .toList();
    }

}