package com.thomas.Bank.Application.controller;

import com.thomas.Bank.Application.entity.Transaction;
import com.thomas.Bank.Application.service.impl.BankStatement;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bankStatement/")
@AllArgsConstructor
public class BankStatementController {
    private BankStatement bankStatement;
    @GetMapping("generate")
    public List<Transaction> getBankStatement(@RequestParam String accountNumber,@RequestParam String fromDate,@RequestParam String toDate){
        return bankStatement.generateBankStatement(accountNumber, fromDate, toDate);
    }
}
