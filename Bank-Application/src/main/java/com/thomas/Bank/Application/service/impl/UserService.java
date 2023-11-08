package com.thomas.Bank.Application.service.impl;

import com.thomas.Bank.Application.dto.BankResponse;
import com.thomas.Bank.Application.dto.UserReq;

public interface UserService {

    BankResponse createAccount(UserReq userReq);
}
