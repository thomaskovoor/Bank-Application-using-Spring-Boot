package com.thomas.Bank.Application.repository;

import com.thomas.Bank.Application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

   Boolean existsByEmail(String email);

   Boolean existsByAccountNumber(String accountNumber);

   User findByAccountNumber(String accountNumber);
}
