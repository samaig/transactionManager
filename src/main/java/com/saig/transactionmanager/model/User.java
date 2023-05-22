package com.saig.transactionmanager.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("users")
public class User {

    @Id
    @Column("userId")
    private Integer userId;

    @Column("firstName")
    private String firstName;

    @Column("lastName")
    private String lastName;

    @Column("email")
    private String email;

    @Column("passwordHash")
    private String passwordHash;

    @Column("createdAt")
    private LocalDateTime createdAt;
}
