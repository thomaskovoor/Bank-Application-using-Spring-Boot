package com.thomas.Bank.Application.service.impl;

import com.thomas.Bank.Application.dto.TransactionDetails;
import com.thomas.Bank.Application.entity.Transaction;
import com.thomas.Bank.Application.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepo;

    @Override
    public void saveTransaction(TransactionDetails transactionDetails) {
     Transaction transaction = Transaction.builder()
             .transactionType(transactionDetails.getTransactionType())
             .accountNumber(transactionDetails.getAccountNumber())
             .amount(transactionDetails.getAmount())
             .status(transactionDetails.getStatus())
             .build();
     transactionRepo.save(transaction);
        System.out.println("Transaction saved");
    }
}
