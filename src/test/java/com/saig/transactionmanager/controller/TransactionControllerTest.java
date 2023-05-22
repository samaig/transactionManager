package com.saig.transactionmanager.controller;

import com.saig.transactionmanager.exception.TransactionNotFoundException;
import com.saig.transactionmanager.model.Transaction;
import com.saig.transactionmanager.model.TransactionStatus;
import com.saig.transactionmanager.model.TransactionType;
import com.saig.transactionmanager.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@WebFluxTest(TransactionController.class)
@Import(TransactionService.class)
class TransactionControllerTest {

    public static final int TRANSACTION_ID = 1;
    public static final int USER_ID = 2;
    public static final double AMOUNT = 100.0;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final LocalDateTime TRANSACTION_DATE = LocalDateTime.of(2023, 4, 4, 0, 0, 0).withNano(0);
    public static final String TRANSACTION_STATUS = TransactionStatus.COMPLETED.name();
    public static final String TRANSACTION_TYPE = TransactionType.DEPOSIT.name();

    @MockBean
    TransactionService transactionService;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        given(transactionService.findTransactionById(TRANSACTION_ID))
                .willReturn(getTransaction());
        given(transactionService.findAllTransactions(0, 10))
                .willReturn(getAllTransactions());
        given(transactionService.findTransactionById(999))
                .willThrow(new TransactionNotFoundException("Transaction not found"));
    }

    @Test
    void testFindTransactionById() {
        webTestClient.get()
                .uri("/api/v1/transactions/{transactionId}", TRANSACTION_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.transactionId").isEqualTo(TRANSACTION_ID)
                .jsonPath("$.userId").isEqualTo(USER_ID)
                .jsonPath("$.amount").isEqualTo(AMOUNT)
                .jsonPath("$.transactionType").isEqualTo(TRANSACTION_TYPE)
                .jsonPath("$.status").isEqualTo(TRANSACTION_STATUS)
                .jsonPath("$.transactionDate").isEqualTo(TRANSACTION_DATE.format(DATE_FORMATTER));

        verify(transactionService, times(1)).findTransactionById(TRANSACTION_ID);
    }

    @Test
    void testGetAllTransactions() {
        webTestClient.get()
                .uri("/api/v1/transactions")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Transaction.class)
                .isEqualTo(getAllTransactionsList());

        verify(transactionService, times(1)).findAllTransactions(0, 10);
    }

    @Test
    void testFindTransactionById_WhenTransactionNotFound() {
        webTestClient.get()
                .uri("/api/v1/transactions/{transactionId}", 999)
                .exchange()
                .expectStatus().isNotFound()
                .expectStatus().isNotFound();

        verify(transactionService, times(1)).findTransactionById(999);
    }

    private Mono<Transaction> getTransaction() {
        return Mono.just(Transaction.builder()
                .transactionId(TRANSACTION_ID)
                .userId(USER_ID)
                .amount(AMOUNT)
                .transactionType(TRANSACTION_TYPE)
                .status(TRANSACTION_STATUS)
                .transactionDate(TRANSACTION_DATE)
                .build());
    }

    private Flux<Transaction> getAllTransactions() {
        return Flux.fromIterable(getAllTransactionsList());
    }

    private List<Transaction> getAllTransactionsList() {
        Transaction transaction1 = Transaction.builder()
                .transactionId(1)
                .userId(2)
                .amount(100.0)
                .transactionType(TransactionType.DEPOSIT.name())
                .status(TransactionStatus.COMPLETED.name())
                .transactionDate(TRANSACTION_DATE)
                .build();

        Transaction transaction2 = Transaction.builder()
                .transactionId(2)
                .userId(2)
                .amount(200.0)
                .transactionType(TransactionType.WITHDRAWAL.name())
                .status(TransactionStatus.PENDING.name())
                .transactionDate(TRANSACTION_DATE)
                .build();

        return Arrays.asList(transaction1, transaction2);
    }
}