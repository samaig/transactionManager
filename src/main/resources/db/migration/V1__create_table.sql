CREATE TABLE users
(
    userId       INT PRIMARY KEY AUTO_INCREMENT,
    firstName     VARCHAR(50),
    lastName      VARCHAR(50),
    email         VARCHAR(100),
    passwordHash VARCHAR(64),
    createdAt    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transactions
(
    transactionId   INT PRIMARY KEY AUTO_INCREMENT,
    userId          INT,
    amount           DECIMAL(10, 2),
    transactionType VARCHAR(50),
    status           ENUM ('PENDING', 'COMPLETED', 'CANCELLED') NOT NULL,
    transactionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES users (userId)
);