CREATE TABLE customers
(
    id    VARCHAR(36) PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE accounts
(
    id                     VARCHAR(36) PRIMARY KEY,
    account_number         VARCHAR(20)    NOT NULL UNIQUE,
    owner_id               VARCHAR(36)    NOT NULL REFERENCES customers (id),
    account_type           VARCHAR(10)    NOT NULL,
    balance                NUMERIC(15, 2) NOT NULL DEFAULT 0,
    created_at             TIMESTAMP      NOT NULL,
    daily_withdrawal_count INT,
    last_withdrawal_date   DATE
);

CREATE TABLE transactions
(
    id                    VARCHAR(36) PRIMARY KEY,
    type                  VARCHAR(15)    NOT NULL,
    amount                NUMERIC(15, 2) NOT NULL,
    source_account_number VARCHAR(20)    NOT NULL,
    target_account_number VARCHAR(20),
    created_at            TIMESTAMP      NOT NULL,
    description           TEXT
);
