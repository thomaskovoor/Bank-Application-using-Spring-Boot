package com.thomas.Bank.Application.service.impl;


import com.thomas.Bank.Application.entity.Transaction;
import com.thomas.Bank.Application.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Component
@AllArgsConstructor
public class BankStatement {

    private TransactionRepository transactionRepo;
    public List<Transaction> generateBankStatement(String accountNumber, String fromDate, String toDate){

        //parsing string date to local date format yy-mm-dd and
        //converting it to local date time yy-mm-dd-h:m:s
        LocalDateTime startDate = LocalDate.parse(fromDate,DateTimeFormatter.ISO_DATE).atTime(LocalTime.MIN);//00:00:00
        LocalDateTime endDate = LocalDate.parse(toDate,DateTimeFormatter.ISO_DATE).atTime(LocalTime.MAX);//23:59:59

        //returning all transactions of specified account number from start date to end date
        // including both start and end date
        return transactionRepo.findAll().stream().filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction ->
                        (transaction.getCreatedAt().isEqual(startDate) || transaction.getCreatedAt().isAfter(startDate))
                                && (transaction.getCreatedAt().isEqual(endDate) || transaction.getCreatedAt().isBefore(endDate))
                       )
                .sorted(Comparator.comparing(Transaction::getCreatedAt))
                .toList();

    }

}
