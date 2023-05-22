package com.saig.transactionmanager.controller;

import com.saig.transactionmanager.model.Transaction;
import com.saig.transactionmanager.service.TransactionService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{transactionId}")
    public Mono<Transaction> getTransaction(@PathVariable Integer transactionId) {
        return transactionService.findTransactionById(transactionId);
    }

    @GetMapping("/users/{userId}")
    public Flux<Transaction> getTransactionsForUser(@PathVariable Integer userId,
                                                    @RequestParam(defaultValue = "0") int offset,
                                                    @RequestParam(defaultValue = "10") int limit) {
        return transactionService.findTransactionsByUserId(userId, offset, limit);
    }

    @GetMapping
    public Flux<Transaction> getAllTransactions(@RequestParam(defaultValue = "0") int offset,
                                                @RequestParam(defaultValue = "10") int limit) {
        return transactionService.findAllTransactions(offset, limit);
    }
}
