package com.saig.transactionmanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("transactions")
public class Transaction {

    @Id
    @Column("transactionId")
    private Integer transactionId;

    @Column("userId")
    private Integer userId;

    @Column("amount")
    private Double amount;

    @Column("transactionType")
    private String transactionType;
    
    @Column("status")
    private String status;

    @Column("transactionDate")
    private LocalDateTime transactionDate;
}
