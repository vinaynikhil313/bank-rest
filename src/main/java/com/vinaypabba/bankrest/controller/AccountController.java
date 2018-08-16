package com.vinaypabba.bankrest.controller;

import com.vinaypabba.bankrest.model.Account;
import com.vinaypabba.bankrest.model.Beneficiary;
import com.vinaypabba.bankrest.model.Transaction;
import com.vinaypabba.bankrest.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity getAccountDetails(@PathVariable @NonNull String accountNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccountDetails(accountNumber));
    }

    @GetMapping("/{accountNumber}/getBeneficiaries")
    public ResponseEntity getBeneficiariesForAccount(@PathVariable @NonNull String accountNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getBeneficiariesByAccountNumber(accountNumber));
    }

    @PostMapping(value = "/{accountNumber}/addBeneficiary", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity addBeneficiaryForAccount(@PathVariable @NonNull String accountNumber, @RequestBody @NonNull Beneficiary beneficiary) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.addBeneficiaryToAccount(accountNumber, beneficiary));
    }

    @DeleteMapping(value = "/{accountNumber}/removeBeneficiary/{benId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity removeBeneficiaryForAccount(@PathVariable @NonNull String accountNumber, @PathVariable @NonNull Long benId) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.removeBeneficiaryFromAccount(accountNumber, benId));
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity addAccount(@RequestBody @NonNull Account account) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.addAccount(account));
    }

    @PostMapping(value = "/{accountNumber}/toBeneficiary/{benId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity newTransaction(@RequestBody @NonNull Transaction transaction, @PathVariable @NonNull String accountNumber, @PathVariable @NonNull Long benId) {
        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("success", accountService.newTransaction(accountNumber, transaction, benId)));
    }

}
