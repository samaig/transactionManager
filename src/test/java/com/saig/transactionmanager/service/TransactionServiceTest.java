package com.saig.transactionmanager.service;

import com.saig.transactionmanager.exception.TransactionNotFoundException;
import com.saig.transactionmanager.model.Transaction;
import com.saig.transactionmanager.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

    public static final String OFFSET_AND_LIMIT_CANNOT_BE_NEGATIVE = "Offset and limit cannot be negative";
    public static final String TRANSACTION_ID_CANNOT_BE_NULL = "Transaction ID cannot be null";
    public static final String NO_TRANSACTION_FOUND_WITH_ID_1 = "No transaction found with id: 1";

    private TransactionRepository transactionRepository;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionRepository = Mockito.mock(TransactionRepository.class);
        transactionService = new TransactionService(transactionRepository);
    }

    @Test
    void findTransactionById() {
        Transaction transaction = new Transaction();
        when(transactionRepository.findTransactionByTransactionId(anyInt()))
                .thenReturn(Mono.just(transaction));

        StepVerifier.create(transactionService.findTransactionById(1))
                .expectNext(transaction)
                .verifyComplete();
    }

    @Test
    void findTransactionById_notFound() {
        when(transactionRepository.findTransactionByTransactionId(anyInt()))
                .thenReturn(Mono.empty());

        StepVerifier.create(transactionService.findTransactionById(1))
                .expectError(EmptyResultDataAccessException.class)
                .verify();
    }

    @Test
    void findTransactionById_errorMessageDetails() {
        when(transactionRepository.findTransactionByTransactionId(1)).thenReturn(Mono.empty());

        StepVerifier.create(transactionService.findTransactionById(1))
                .expectErrorMatches(throwable -> throwable instanceof EmptyResultDataAccessException &&
                        Objects.requireNonNull(throwable.getMessage()).contains(NO_TRANSACTION_FOUND_WITH_ID_1))
                .verify();
    }

    @Test
    void findTransactionById_validId() {
        Transaction transaction = new Transaction();
        when(transactionRepository.findTransactionByTransactionId(1)).thenReturn(Mono.just(transaction));

        StepVerifier.create(transactionService.findTransactionById(1))
                .expectNext(transaction)
                .verifyComplete();
    }

    @Test
    void findTransactionById_nullId() {
        assertThatThrownBy(() -> transactionService.findTransactionById(null).block())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(TRANSACTION_ID_CANNOT_BE_NULL);
    }

    @Test
    void findTransactionsByUserId() {
        Transaction transaction = new Transaction();
        when(transactionRepository.findTransactionByUserId(anyInt(), anyInt(), anyInt()))
                .thenReturn(Flux.just(transaction));

        StepVerifier.create(transactionService.findTransactionsByUserId(1, 0, 10))
                .expectNext(transaction)
                .verifyComplete();
    }

    @Test
    void findTransactionsByUserId_notFound() {
        when(transactionRepository.findTransactionByUserId(anyInt(), anyInt(), anyInt()))
                .thenReturn(Flux.empty());

        StepVerifier.create(transactionService.findTransactionsByUserId(1, 0, 10))
                .expectError(TransactionNotFoundException.class)
                .verify();
    }

    @Test
    void findTransactionsByUserId_validUserId() {
        Transaction transaction = new Transaction();
        when(transactionRepository.findTransactionByUserId(1, 0, 10)).thenReturn(Flux.just(transaction));

        StepVerifier.create(transactionService.findTransactionsByUserId(1, 0, 10))
                .expectNext(transaction)
                .verifyComplete();
    }

    @Test
    void findTransactionById_repositoryThrowsException() {
        when(transactionRepository.findTransactionByTransactionId(1)).thenReturn(Mono.error(new RuntimeException()));

        StepVerifier.create(transactionService.findTransactionById(1))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void findTransactionsByUserId_negativeOffset() {
        assertThatThrownBy(() -> transactionService.findTransactionsByUserId(1, -1, 10).blockLast())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OFFSET_AND_LIMIT_CANNOT_BE_NEGATIVE);
    }

    @Test
    void findTransactionsByUserId_negativeLimit() {
        assertThatThrownBy(() -> transactionService.findTransactionsByUserId(1, 0, -10).blockLast())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OFFSET_AND_LIMIT_CANNOT_BE_NEGATIVE);
    }

    @Test
    void findAllTransactions() {
        Transaction transaction = new Transaction();
        when(transactionRepository.findAllTransactions(anyInt(), anyInt()))
                .thenReturn(Flux.just(transaction));

        StepVerifier.create(transactionService.findAllTransactions(0, 10))
                .expectNext(transaction)
                .verifyComplete();
    }

    @Test
    void findAllTransactions_notFound() {
        when(transactionRepository.findAllTransactions(anyInt(), anyInt()))
                .thenReturn(Flux.empty());

        StepVerifier.create(transactionService.findAllTransactions(0, 10))
                .expectError(TransactionNotFoundException.class)
                .verify();
    }

    @Test
    void findAllTransactions_validInputs() {
        Transaction transaction = new Transaction();
        when(transactionRepository.findAllTransactions(0, 10)).thenReturn(Flux.just(transaction));

        StepVerifier.create(transactionService.findAllTransactions(0, 10))
                .expectNext(transaction)
                .verifyComplete();
    }

    @Test
    void findAllTransactions_negativeOffset() {
        assertThatThrownBy(() -> transactionService.findAllTransactions(-1, 10).blockLast())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OFFSET_AND_LIMIT_CANNOT_BE_NEGATIVE);
    }

    @Test
    void findAllTransactions_negativeLimit() {
        assertThatThrownBy(() -> transactionService.findAllTransactions(0, -10).blockLast())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OFFSET_AND_LIMIT_CANNOT_BE_NEGATIVE);
    }

}
