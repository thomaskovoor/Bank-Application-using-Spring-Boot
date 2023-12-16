package com.thomas.Bank.Application.utils;

import java.time.Year;

public class AccountUtils {

    public static final String account_already_exists_code="001";
    public static final String account_already_exists_message="User already exists";
    public static final String account_created_code="000";
    public static final String account_created_message="Account has been successfully created";
    public static final String account_does_not_exist_code="002";
    public static final String account_does_not_exist_message ="Account does not exist";
    public static final String account_found_code = "003";
    public static final String account_found_message = "Account found";
    public static final String account_credited_code="004";
    public static final String account_credited_message="Account has been successfully credited";
    public static final String insufficient_balance_code="005";
    public static final String insufficient_balance_message="Insufficient balance";
    public static final String account_debited_code="006";
    public static final String account_debited_message="Account has been successfully debited";
    public static final String credit_account_does_not_exist_code="007";
    public static final String credit_account_does_not_exist_message ="Credit account does not exist";
    public static final String debit_account_does_not_exist_code="008";
    public static final String debit_account_does_not_exist_message ="Debit account does not exist";
    public static final String transfer_successful_code="009";
    public static final String transfer_successful_message="Transfer successful";

    public static final String login_code="010";
    public static final String login_message="Successfully logged in";




    public static String accountNumberGenerator() {
        //11-digit account number
        //current year + random 7 digits
        Year currentYear = Year.now();
        int min = 1000000;
        int max = 9999999;

        int randomNum = (int) Math.floor(Math.random() * (max - min + 1) + min);

        //concatenate year and random number
        String year = String.valueOf(currentYear);
        String acctNum = String.valueOf(randomNum);
        return year+acctNum;
    }
}
