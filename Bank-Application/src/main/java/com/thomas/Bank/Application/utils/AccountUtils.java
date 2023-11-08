package com.thomas.Bank.Application.utils;

import java.time.Year;

public class AccountUtils {

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
