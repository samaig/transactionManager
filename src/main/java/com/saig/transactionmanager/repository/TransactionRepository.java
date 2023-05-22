package com.saig.transactionmanager.repository;

import com.saig.transactionmanager.model.Transaction;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionRepository extends R2dbcRepository<Transaction, Integer> {

    Mono<Transaction> findTransactionByTransactionId(Integer transactionId);

    @Query("SELECT * FROM transactions WHERE userId = :userId LIMIT :limit OFFSET :offset")
    Flux<Transaction> findTransactionByUserId(int userId, int offset, int limit);

    @Query("SELECT * FROM transactions LIMIT :limit OFFSET :offset")
    Flux<Transaction> findAllTransactions(int offset, int limit);
}
