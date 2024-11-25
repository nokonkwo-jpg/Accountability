package com.example.plaidbankapp.repo;

import com.example.plaidbankapp.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {}


