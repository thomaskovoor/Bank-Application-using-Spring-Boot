package com.thomas.Bank.Application.service.impl;

import com.thomas.Bank.Application.dto.*;
import com.thomas.Bank.Application.entity.User;
import com.thomas.Bank.Application.repository.UserRepository;
import com.thomas.Bank.Application.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepo;
    @Autowired
    EmailService emailService;
    @Override
    public BankResponse createAccount(UserReq userReq) {

        //checking if user already exists

       if(userRepo.existsByEmail(userReq.getEmail())){
           return BankResponse.builder()
                   .responseCode(AccountUtils.account_already_exists_code)
                   .responseMessage(AccountUtils.account_already_exists_message)
                   .accountInfo(null)
                   .build();

       }
       else {

           //creating a new user into the db
           User newUser = User.builder()
                   .firstName(userReq.getFirstName())
                   .lastName(userReq.getLastName())
                   .otherName(userReq.getOtherName())
                   .gender(userReq.getGender())
                   .address(userReq.getAddress())
                   .email(userReq.getEmail())
                   .location(userReq.getLocation())
                   .accountNumber(AccountUtils.accountNumberGenerator())
                   .alternativePhoneNumber(userReq.getAlternativePhoneNumber())
                   .phoneNumber(userReq.getPhoneNumber())
                   .accountBalance(BigDecimal.ZERO)
                   .status("ACTIVE")
                   .build();

           User savedUser = userRepo.save(newUser);

           //sending email alert
           EmailDetails emailDetails = EmailDetails.builder()
                   .recipient(savedUser.getEmail())
                   .subject("Account Creation of "+savedUser.getFirstName()+" "+savedUser.getLastName())
                   .messageBody("Thank you for creating an account with us.\n" +
                           "Your account details are : \n" +
                           "Account Holder Name :"+savedUser.getFirstName()+" "+savedUser.getLastName()+"\n"+
                           "Account Email Id :"+savedUser.getEmail()+"\n"+
                           "Account Number :"+savedUser.getAccountNumber()+"\n"+
                           "Phone Number :"+savedUser.getPhoneNumber()+"\n"+
                           "Account Balance :"+savedUser.getAccountBalance())
                   .attachment(null)
                   .build();

           emailService.sendEmailAlert(emailDetails);

           return BankResponse.builder()
                   .accountInfo(AccountInfo.builder()
                           .accountBalance(savedUser.getAccountBalance())
                           .accountName(savedUser.getFirstName()+" "+savedUser.getLastName()+" "+savedUser.getOtherName())
                           .accountNumber(savedUser.getAccountNumber())
                           .build())
                   .responseMessage(AccountUtils.account_created_message)
                   .responseCode(AccountUtils.account_created_code)
                   .build();
       }
    }
    //balance enquiry
    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {

        //checking if account exists
        boolean isAccountExists = userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.account_does_not_exist_code)
                    .responseMessage(AccountUtils.account_does_not_exist_message)
                    .accountInfo(null)
                    .build();
        }
        else{
            User userExists = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());
            return BankResponse.builder()
                    .responseCode(AccountUtils.account_found_code)
                    .responseMessage(AccountUtils.account_found_message)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userExists.getFirstName()+" "+userExists.getLastName())
                            .accountNumber(userExists.getAccountNumber())
                            .accountBalance(userExists.getAccountBalance())
                            .build())
                    .build();
        }
    }
//name enquiry
    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {

        //checking if account exists
        boolean isAccountExists = userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExists){
            return AccountUtils.account_does_not_exist_message;
        }
        else{
            User userExists = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());
            return userExists.getFirstName()+" "+userExists.getLastName()+" "+userExists.getOtherName();
        }

    }


    //credit,debit and transfer


}
