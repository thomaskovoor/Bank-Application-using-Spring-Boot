package com.thomas.Bank.Application.service.impl;

import com.thomas.Bank.Application.dto.EmailDetails;

public interface EmailService {

    void sendEmailAlert(EmailDetails emailDetails);
}
