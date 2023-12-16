package com.thomas.Bank.Application.service.impl;
import com.thomas.Bank.Application.config.JwtTokenProvider;
import com.thomas.Bank.Application.dto.*;
import com.thomas.Bank.Application.entity.Role;
import com.thomas.Bank.Application.entity.User;
import com.thomas.Bank.Application.repository.UserRepository;
import com.thomas.Bank.Application.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepo;
    @Autowired
    EmailService emailService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    TransactionService transactionService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
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
                   .password(passwordEncoder.encode(userReq.getPassword()))
                   .location(userReq.getLocation())
                   .accountNumber(AccountUtils.accountNumberGenerator())
                   .alternativePhoneNumber(userReq.getAlternativePhoneNumber())
                   .phoneNumber(userReq.getPhoneNumber())
                   .accountBalance(BigDecimal.ZERO)
                   .status("ACTIVE")
                   .role(Role.valueOf(userReq.getRole()))
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


    public BankResponse accountLogin(LoginRequest loginRequest){
        Authentication authentication;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
        );
        return BankResponse.builder()
                .responseCode(AccountUtils.login_code)
                .responseMessage(jwtTokenProvider.generateToken(authentication))
                .build();

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
    //credit
    @Override
    public BankResponse creditAccount(CreditDebitRequest creditRequest) {
        boolean isAccountExists = userRepo.existsByAccountNumber(creditRequest.getAccountNumber());
        if(!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.account_does_not_exist_code)
                    .responseMessage(AccountUtils.account_does_not_exist_message)
                    .accountInfo(null)
                    .build();
        }
            User userToCredit = userRepo.findByAccountNumber(creditRequest.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditRequest.getAmount()));
        userRepo.save(userToCredit);

        //saving the transaction
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(creditRequest.getAmount())
                .status("Success")
                .build();
        transactionService.saveTransaction(transactionDetails);

            return BankResponse.builder()
                    .responseCode(AccountUtils.account_credited_code)
                    .responseMessage(AccountUtils.account_credited_message)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userToCredit.getFirstName()+" "+userToCredit.getLastName())
                            .accountNumber(userToCredit.getAccountNumber())
                            .accountBalance(userToCredit.getAccountBalance())
                            .build())
                    .build();

    }
//debit
    @Override
    public BankResponse debitAccount(CreditDebitRequest debitRequest) {
        //check if the account exists
        boolean isAccountExists = userRepo.existsByAccountNumber(debitRequest.getAccountNumber());
        if(!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.account_does_not_exist_code)
                    .responseMessage(AccountUtils.account_does_not_exist_message)
                    .accountInfo(null)
                    .build();
        }
        User userToDebit = userRepo.findByAccountNumber(debitRequest.getAccountNumber());
        if(debitRequest.getAmount().compareTo(userToDebit.getAccountBalance()) == 1){
            return BankResponse.builder()
                    .responseCode(AccountUtils.insufficient_balance_code)
                    .responseMessage(AccountUtils.insufficient_balance_message)
                    .accountInfo(null)
                    .build();
        }
        else {

            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(debitRequest.getAmount()));
            userRepo.save(userToDebit);

            //saving the transaction
            TransactionDetails transactionDetails = TransactionDetails.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType("DEBIT")
                    .amount(debitRequest.getAmount())
                    .status("Success")
                    .build();
            transactionService.saveTransaction(transactionDetails);

            return BankResponse.builder()
                    .responseCode(AccountUtils.account_debited_code)
                    .responseMessage(AccountUtils.account_debited_message)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userToDebit.getFirstName()+" "+userToDebit.getLastName())
                            .accountNumber(userToDebit.getAccountNumber())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
        }
    }
    // transfer
    @Override
    public BankResponse accountTransfer(TransferRequest transferRequest) {
        //check if from account exists
        boolean isFromAccountExists = userRepo.existsByAccountNumber(transferRequest.getFromAccountNumber());
        //check if account to be credited exists
        boolean isToAccountExists = userRepo.existsByAccountNumber(transferRequest.getToAccountNumber());

        if(!isFromAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.credit_account_does_not_exist_code)
                    .responseMessage(AccountUtils.credit_account_does_not_exist_message)
                    .accountInfo(null)
                    .build();
        }
        if(!isToAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.debit_account_does_not_exist_code)
                    .responseMessage(AccountUtils.debit_account_does_not_exist_message)
                    .accountInfo(null)
                    .build();
        }

        //check if amount to be debited is greater than account balance

        User userToDebit = userRepo.findByAccountNumber(transferRequest.getFromAccountNumber());
        if(transferRequest.getAmount().compareTo(userToDebit.getAccountBalance()) == 1){
            return BankResponse.builder()
                    .responseCode(AccountUtils.insufficient_balance_code)
                    .responseMessage(AccountUtils.insufficient_balance_message)
                    .accountInfo(null)
                    .build();
        }

        else {
            //debit from account
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(transferRequest.getAmount()));
            userRepo.save(userToDebit);
            emailService.sendEmailAlert(EmailDetails.builder()
                    .recipient(userToDebit.getEmail())
                    .subject("Debit Alert")
                    .messageBody("The following account :\n" +
                            "Account Number :"+userToDebit.getAccountNumber()+"\n"+
                             "Account Name :"+userToDebit.getFirstName()+" "+userToDebit.getLastName()+"\n"+
                            "has been debited by the amount :"+transferRequest.getAmount()+"\n"+
                             "The current balance is : "+userToDebit.getAccountBalance())
                    .attachment(null)
                    .build());
            TransactionDetails transactionDetailsDebit = TransactionDetails.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType("DEBIT TRANSFER")
                    .amount(transferRequest.getAmount())
                    .status("Success")
                    .build();
            transactionService.saveTransaction(transactionDetailsDebit);


            //credit account
            User userToCredit = userRepo.findByAccountNumber(transferRequest.getToAccountNumber());
            userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(transferRequest.getAmount()));
            userRepo.save(userToCredit);

            emailService.sendEmailAlert(EmailDetails.builder()
                            .recipient(userToCredit.getEmail())
                            .subject("Credit Alert")
                            .messageBody("The following account :\n" +
                                    "Account Number :"+userToCredit.getAccountNumber()+"\n"+
                                    "Account Name :"+userToCredit.getFirstName()+" "+userToCredit.getLastName()+"\n"+
                                    "has been credited by the amount :"+transferRequest.getAmount()+"\n"+
                                    "The current balance is : "+userToCredit.getAccountBalance())
                            .attachment(null)
                    .build());
         //saving the transaction
            TransactionDetails transactionDetailsCredit = TransactionDetails.builder()
                    .accountNumber(userToCredit.getAccountNumber())
                    .transactionType("CREDIT TRANSFER")
                    .amount(transferRequest.getAmount())
                    .status("Success")
                    .build();
            transactionService.saveTransaction(transactionDetailsCredit);

            return BankResponse.builder()
                    .responseCode(AccountUtils.transfer_successful_code)
                    .responseMessage(AccountUtils.transfer_successful_message)
                    .accountInfo(null)
                    .build();
        }
    }





}
