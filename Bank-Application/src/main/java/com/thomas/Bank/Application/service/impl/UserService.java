package com.thomas.Bank.Application.service.impl;

import com.thomas.Bank.Application.dto.*;

public interface UserService {

    BankResponse createAccount(UserReq userReq);
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);
    BankResponse creditAccount(CreditDebitRequest creditRequest);
    BankResponse debitAccount(CreditDebitRequest debitRequest);








}

