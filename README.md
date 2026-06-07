# Java Banking System

A feature-rich command-line banking application built with Java, demonstrating core software engineering principles and object-oriented design patterns.

## Features

- **Customer Management**
    - Register new customers
    - View all registered customers
    - Maintain customer profiles

- **Account Management**
    - Create checking and savings accounts
    - View all customer accounts
    - Close accounts (with zero balance requirement)

- **Account Types**
    - **Checking Accounts**: Standard accounts with unlimited withdrawals
    - **Savings Accounts**: Special accounts with daily withdrawal limits (max 3 per day)

- **Transaction Processing**
    - Deposit funds
    - Withdraw funds (with appropriate account constraints)
    - Transfer funds between accounts
    - View complete transaction history

- **Error Handling**
    - Insufficient funds validation
    - Daily withdrawal limit enforcement
    - Account and customer existence verification
    - Graceful exception handling with custom exceptions

## Tech Stack

- **Language**: Java 21
- **Build Tool**: Maven
- **Database**: PostgreSQL
- **Data Access**: Raw JDBC (no ORM)
- **Architecture Pattern**: Repository Pattern with Service Layer

## Project Structure

```
banking-cli/
├── src/main/
│   ├── java/
│   │   ├── Main.java                          # Application entry point
│   │   ├── exception/                         # Custom exceptions
│   │   │   ├── AccountNotFoundException
│   │   │   ├── CustomerNotFoundException
│   │   │   ├── DailyWithdrawalLimitException
│   │   │   └── InsufficientFundsException
│   │   ├── model/                             # Domain models
│   │   │   ├── Account.java
│   │   │   ├── CheckingAccount.java
│   │   │   ├── SavingsAccount.java
│   │   │   ├── Customer.java
│   │   │   ├── Transaction.java
│   │   │   └── enums/
│   │   │       ├── AccountType.java
│   │   │       └── TransactionType.java
│   │   ├── repository/                        # Data access layer
│   │   │   ├── AccountRepository.java
│   │   │   ├── AccountRepositoryImpl.java
│   │   │   ├── CustomerRepository.java
│   │   │   ├── CustomerRepositoryImpl.java
│   │   │   ├── TransactionRepository.java
│   │   │   └── TransactionRepositoryImpl.java
│   │   ├── service/                           # Business logic layer
│   │   │   ├── AccountService.java
│   │   │   ├── CustomerService.java
│   │   │   └── TransactionService.java
│   │   └── util/                              # Utility classes
│   │       ├── DatabaseConnection.java
│   │       └── MenuHandler.java
│   └── resources/
│       ├── schema.sql                         # Database schema
│       └── db.properties                      # DB credentials (not committed)
└── pom.xml
```

## Architecture

### Repository Pattern

The application implements the Repository pattern to abstract data access logic, making the code more maintainable and testable.

### Service Layer

Business logic is separated into dedicated service classes (`AccountService`, `CustomerService`, `TransactionService`) that coordinate between the repository and presentation layers.

### Abstract Base Classes

The `Account` class uses inheritance to create specialized account types (Checking, Savings) with different behavior while sharing common functionality.

## Database Setup

### Prerequisites

- PostgreSQL installed and running

### Create the database

```sql
CREATE DATABASE banking_cli;
```

### Run the schema

```bash
psql -U postgres -d banking_cli -f src/main/resources/schema.sql
```

### Configure credentials

Create `src/main/resources/db.properties` (this file is excluded from version control):

```properties
db.url=jdbc:postgresql://localhost:5432/banking_cli
db.user=your_username
db.password=your_password
```

## Usage

Once the application starts, you'll be presented with a menu:

```
Welcome to Banking CLI
1. Register Customer
2. List All Customers
3. Open Account
4. View Customer Accounts
5. Deposit
6. Withdraw
7. Transfer
8. View Transaction History
9. Check Balance
10. Close Account
0. Exit
```

### Example Workflow

1. **Register a customer**: Select option 1
2. **Open an account**: Select option 3 (choose between Checking or Savings)
3. **Deposit funds**: Select option 5
4. **Make a withdrawal**: Select option 6 (Savings accounts limited to 3/day)
5. **View balance**: Select option 9
6. **Check history**: Select option 8

## Key Implementation Details

### Custom Exceptions

- `InsufficientFundsException`: Thrown when withdrawal/transfer exceeds balance
- `DailyWithdrawalLimitException`: Thrown when savings account withdrawal limit exceeded
- `AccountNotFoundException`: Thrown when account doesn't exist
- `CustomerNotFoundException`: Thrown when customer doesn't exist

### Savings Account Constraints

Savings accounts implement a daily withdrawal limit of 3 transactions per calendar day, resetting at midnight. Attempting to exceed this limit throws a `DailyWithdrawalLimitException`.

### Data Persistence

All data is persisted in a PostgreSQL database using raw JDBC via `PreparedStatement`. Each repository implementation maps SQL result sets directly to Java objects with no ORM involved.
