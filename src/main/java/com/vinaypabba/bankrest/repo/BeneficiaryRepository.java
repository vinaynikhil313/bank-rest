package com.vinaypabba.bankrest.repo;

import com.vinaypabba.bankrest.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

    List<Beneficiary> findAllByAccount_Number(String accountNumber);

}
