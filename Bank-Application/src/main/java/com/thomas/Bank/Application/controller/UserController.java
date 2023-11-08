package com.thomas.Bank.Application.controller;

import com.thomas.Bank.Application.dto.BankResponse;
import com.thomas.Bank.Application.dto.UserReq;
import com.thomas.Bank.Application.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @PostMapping("/api/user")
    public BankResponse createAccount(@RequestBody UserReq userReq){
        return userService.createAccount(userReq);
    }

}
