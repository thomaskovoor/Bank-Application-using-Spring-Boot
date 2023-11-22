package com.thomas.Bank.Application.service.impl;


import com.thomas.Bank.Application.entity.Transaction;
import com.thomas.Bank.Application.repository.TransactionRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BankStatement {

    private TransactionRepository transactionRepo;
    public List<Transaction> generateBankStatement(String accountNumber, String fromDate, String toDate){

        LocalDate startDate = LocalDate.parse(fromDate, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate endDate = LocalDate.parse(toDate,DateTimeFormatter.ISO_LOCAL_DATE);

        return transactionRepo.findAll().stream().filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().isEqual(startDate))
                .filter(transaction -> transaction.getCreatedAt().isEqual(endDate)).toList();
    }

}
