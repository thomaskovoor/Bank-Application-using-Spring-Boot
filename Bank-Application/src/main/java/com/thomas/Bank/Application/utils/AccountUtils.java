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
