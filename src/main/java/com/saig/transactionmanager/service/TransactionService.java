package com.saig.transactionmanager.service;

import com.saig.transactionmanager.exception.TransactionNotFoundException;
import com.saig.transactionmanager.model.Transaction;
import com.saig.transactionmanager.repository.TransactionRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Cacheable("findTransactionById")
    public Mono<Transaction> findTransactionById(Integer transactionId) {
        validateTransactionId(transactionId);
        return transactionRepository.findTransactionByTransactionId(transactionId)
                .switchIfEmpty(Mono.error(new EmptyResultDataAccessException("No transaction found with id: " + transactionId, 1)));
    }

    @Cacheable("findTransactionsByUserId")
    public Flux<Transaction> findTransactionsByUserId(Integer userId, int offset, int limit) {
        validateOffsetAndLimit(offset, limit);
        return transactionRepository.findTransactionByUserId(userId, offset, limit)
                .switchIfEmpty(Flux.error(new TransactionNotFoundException("No transactions found for user id: " + userId)));
    }

    public Flux<Transaction> findAllTransactions(int offset, int limit) {
        validateOffsetAndLimit(offset, limit);
        return transactionRepository.findAllTransactions(offset, limit)
                .switchIfEmpty(Flux.error(new TransactionNotFoundException("No transactions found")));
    }

    private void validateTransactionId(Integer transactionId) {
        if (transactionId == null) {
            throw new IllegalArgumentException("Transaction ID cannot be null");
        }
    }

    private void validateOffsetAndLimit(int offset, int limit) {
        if (offset < 0 || limit < 0) {
            throw new IllegalArgumentException("Offset and limit cannot be negative");
        }
    }
}
