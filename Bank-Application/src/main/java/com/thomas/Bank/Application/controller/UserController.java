package com.thomas.Bank.Application.controller;

import com.thomas.Bank.Application.dto.*;
import com.thomas.Bank.Application.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class UserController {

    @Autowired
    UserService userService;
    @PostMapping("user")
    public BankResponse createAccount(@RequestBody UserReq userReq){
        return userService.createAccount(userReq);
    }

    @GetMapping("balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest){ return userService.balanceEnquiry(enquiryRequest);}

    @GetMapping("nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){ return userService.nameEnquiry(enquiryRequest);}

    @PostMapping("credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest creditRequest){return userService.creditAccount(creditRequest);}
    @PostMapping("debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest debitRequest){return userService.debitAccount(debitRequest);}

    @PostMapping("transfer")
    public BankResponse accountTransfer(@RequestBody TransferRequest transferRequest){return userService.accountTransfer(transferRequest);}
}
