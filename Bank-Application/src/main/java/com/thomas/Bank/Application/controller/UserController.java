package com.thomas.Bank.Application.controller;

import com.thomas.Bank.Application.dto.*;
import com.thomas.Bank.Application.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@Tag(
       name= "Account Management APIs",
        description = "Summary about each api endpoint"
)
public class UserController {

    @Autowired
    UserService userService;
    @Operation(
            method = "POST",
            summary = "Create a new user account",
            description = "Creating a new user account and assigning a unique auto generated account number"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP status 201 created"
    )
    @PostMapping("user")
    public BankResponse createAccount(@RequestBody UserReq userReq){
        return userService.createAccount(userReq);
    }

    @Operation(
            method = "GET",
            summary = "Balance Enquiry",
            description = "Returning the requested account numbers current balance"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP status 200"
    )
    @GetMapping("balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest){ return userService.balanceEnquiry(enquiryRequest);}

    @Operation(
            method = "GET",
            summary = "Name Enquiry",
            description = "Returning the requested account numbers owner name"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP status 200"
    )
    @GetMapping("nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){ return userService.nameEnquiry(enquiryRequest);}

    @Operation(
            method = "POST",
            summary = "Credit",
            description = "Adding the specified amount into the specified account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP status 200"
    )
    @PostMapping("credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest creditRequest){return userService.creditAccount(creditRequest);}
    @Operation(
            method = "POST",
            summary = "Debit",
            description = "Withdrawing the specified amount from the specified account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP status 200"
    )
    @PostMapping("debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest debitRequest){return userService.debitAccount(debitRequest);}

    @Operation(
            method = "POST",
            summary = "Transfer",
            description = "Transferring the specified amount from current account to specified account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP status 200"
    )
    @PostMapping("transfer")
    public BankResponse accountTransfer(@RequestBody TransferRequest transferRequest){return userService.accountTransfer(transferRequest);}

    @PostMapping("login")
    public BankResponse accountLogin(@RequestBody LoginRequest loginRequest){
        return userService.accountLogin(loginRequest);
    }
}
