package com.thomas.Bank.Application.service.impl;

import com.thomas.Bank.Application.dto.BankResponse;
import com.thomas.Bank.Application.dto.EnquiryRequest;
import com.thomas.Bank.Application.dto.UserReq;

public interface UserService {

    BankResponse createAccount(UserReq userReq);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);


}

