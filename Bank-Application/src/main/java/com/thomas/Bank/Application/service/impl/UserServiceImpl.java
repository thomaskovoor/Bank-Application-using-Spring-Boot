package com.thomas.Bank.Application.service.impl;

import com.thomas.Bank.Application.dto.BankResponse;
import com.thomas.Bank.Application.dto.UserReq;
import com.thomas.Bank.Application.entity.User;
import com.thomas.Bank.Application.utils.AccountUtils;

import java.math.BigDecimal;

public class UserServiceImpl implements UserService {
    @Override
    public BankResponse createAccount(UserReq userReq) {
        //creating a new user into the db

        User newUser = User.builder()
                .firstName(userReq.getFirstName())
                .lastName(userReq.getLastName())
                .otherName(userReq.getOtherName())
                .gender(userReq.getGender())
                .address(userReq.getAddress())
                .emailId(userReq.getEmailId())
                .location(userReq.getLocation())
                .accountNumber(AccountUtils.accountNumberGenerator())
                .alternativePhoneNumber(userReq.getAlternativePhoneNumber())
                .phoneNumber(userReq.getPhoneNumber())
                .accountBalance(BigDecimal.ZERO)
                .status("ACTIVE")
                .build();


    }
}
