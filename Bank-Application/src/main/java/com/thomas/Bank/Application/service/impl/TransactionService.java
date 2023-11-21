package com.thomas.Bank.Application.service.impl;

import com.thomas.Bank.Application.dto.TransactionDetails;
import com.thomas.Bank.Application.entity.Transaction;

public interface TransactionService {
    void saveTransaction(TransactionDetails transactionDetails);
}
