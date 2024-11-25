package com.example.plaidbankapp.repo;

import com.example.plaidbankapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}

