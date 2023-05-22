package com.saig.transactionmanager.repository;

import com.saig.transactionmanager.model.Transaction;
import com.saig.transactionmanager.model.TransactionStatus;
import com.saig.transactionmanager.model.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class TransactionRepositoryTest {

    private static final double AMOUNT_ONE = 100.0;
    private static final double AMOUNT_TWO = 200.0;
    private static final int USER_ID_ONE = 1;
    private static final int USER_ID_TWO = 2;
    private static final int TRANSACTION_ID_ONE = 1;
    private static final int TRANSACTION_ID_TWO = 2;

    @Mock
    private TransactionRepository transactionRepository;

    private Transaction transaction1;
    private Transaction transaction2;

    @BeforeEach
    public void setUp() {
        transaction1 = new Transaction(TRANSACTION_ID_ONE, USER_ID_ONE, AMOUNT_ONE, TransactionType.DEPOSIT.name(), TransactionStatus.COMPLETED.name(), LocalDateTime.now());
        transaction2 = new Transaction(TRANSACTION_ID_TWO, USER_ID_TWO, AMOUNT_TWO, TransactionType.WITHDRAWAL.name(), TransactionStatus.COMPLETED.name(), LocalDateTime.now());
    }

    @Test
    public void testFindTransactionById() {
        Mockito.when(transactionRepository.findTransactionByTransactionId(transaction1.getTransactionId()))
               .thenReturn(Mono.just(transaction1));

        StepVerifier.create(transactionRepository.findTransactionByTransactionId(transaction1.getTransactionId()))
                    .expectNext(transaction1)
                    .verifyComplete();
    }

    @Test
    public void testFindTransactionByUserId() {
        Mockito.when(transactionRepository.findTransactionByUserId(transaction1.getUserId(), 0, 1))
               .thenReturn(Flux.just(transaction1));

        StepVerifier.create(transactionRepository.findTransactionByUserId(transaction1.getUserId(), 0, 1))
                    .expectNext(transaction1)
                    .verifyComplete();
    }

    @Test
    public void testFindAllTransactions() {
        Mockito.when(transactionRepository.findAllTransactions(0, 2))
               .thenReturn(Flux.just(transaction1, transaction2));

        StepVerifier.create(transactionRepository.findAllTransactions(0, 2))
                    .expectNext(transaction1, transaction2)
                    .verifyComplete();
    }
}
