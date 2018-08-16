package com.vinaypabba.bankrest.repo;

import com.vinaypabba.bankrest.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findAccountByNumber(String accountNumber);

}
