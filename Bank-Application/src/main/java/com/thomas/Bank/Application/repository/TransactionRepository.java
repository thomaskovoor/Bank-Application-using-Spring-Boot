package com.thomas.Bank.Application.repository;

import com.thomas.Bank.Application.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,String> {
}
