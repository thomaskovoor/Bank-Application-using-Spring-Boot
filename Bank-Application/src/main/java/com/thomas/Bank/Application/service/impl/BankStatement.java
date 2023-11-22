package com.thomas.Bank.Application.service.impl;


import com.thomas.Bank.Application.entity.Transaction;
import com.thomas.Bank.Application.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BankStatement {

    private TransactionRepository transactionRepo;
    public List<Transaction> generateBankStatement(String accountNumber, String fromDate, String toDate){

        LocalDate startDate = LocalDate.parse(fromDate, DateTimeFormatter.ISO_DATE);
        LocalDate endDate = LocalDate.parse(toDate,DateTimeFormatter.ISO_DATE);

        return transactionRepo.findAll().stream().filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().isAfter(startDate) && transaction.getCreatedAt().isBefore(endDate))
                .toList();

    }

}
