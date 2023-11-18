package com.thomas.Bank.Application.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequest {

    private BigDecimal amount;
    private String fromAccountNumber;
    private String toAccountNumber;
}
