package com.thomas.Bank.Application.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReq {
    private String firstName;
    private String lastName;
    private String otherName;
    private String gender;
    private String address;
    private String email;
    private String location;
    private String phoneNumber;
    private String alternativePhoneNumber;
}
