package com.thomas.Bank.Application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDetails {
    private String transactionType;
    private String amount;
    private String accountNumber;
    private String status;
}
