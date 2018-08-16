package com.vinaypabba.bankrest.repo;

import com.vinaypabba.bankrest.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
