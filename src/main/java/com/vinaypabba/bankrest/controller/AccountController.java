package com.vinaypabba.bankrest.controller;

import com.vinaypabba.bankrest.model.Account;
import com.vinaypabba.bankrest.model.Beneficiary;
import com.vinaypabba.bankrest.model.Transaction;
import com.vinaypabba.bankrest.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/account")
@Api(value = "Account Controller", description = "API for account related activities")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountNumber}")
    @ApiOperation(value = "Get the account information of an account", response = Account.class)
    public ResponseEntity getAccountDetails(@PathVariable @NonNull String accountNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccountDetails(accountNumber, true));
    }

    @GetMapping("/{accountNumber}/getBeneficiaries")
    @ApiOperation(value = "Get the list of beneficiaries for an account", response = Beneficiary.class)
    public ResponseEntity getBeneficiariesForAccount(@PathVariable @NonNull String accountNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getBeneficiariesByAccountNumber(accountNumber));
    }

    @GetMapping("/{accountNumber}/getTransactions")
    @ApiOperation(value = "Get the detailed summary of account including all transactions", response = Account.class)
    public ResponseEntity getTransactionsForAccount(@PathVariable @NonNull String accountNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccountDetails(accountNumber, false));
    }

    @PostMapping(value = "/{accountNumber}/addBeneficiary", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Add a beneficiary to an account", response = Account.class)
    public ResponseEntity addBeneficiaryForAccount(@PathVariable @NonNull String accountNumber, @RequestBody @NonNull Beneficiary beneficiary) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.addBeneficiaryToAccount(accountNumber, beneficiary));
    }

    @DeleteMapping(value = "/{accountNumber}/removeBeneficiary/{benId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Remove a beneficiary from an account", response = Account.class)
    public ResponseEntity removeBeneficiaryForAccount(@PathVariable @NonNull String accountNumber, @PathVariable @NonNull Long benId) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.removeBeneficiaryFromAccount(accountNumber, benId));
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Add an account", response = Map.class)
    public ResponseEntity addAccount(@RequestBody @NonNull Account account) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.addAccount(account));
    }

    @PostMapping(value = "/transferFrom/{accountNumber}/toBeneficiary/{benId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Perform a transaction between an account and a beneficiary", response = Map.class)
    public ResponseEntity newTransaction(@RequestBody @NonNull Transaction transaction, @PathVariable @NonNull String accountNumber, @PathVariable @NonNull Long benId) {
        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("success", accountService.newTransaction(accountNumber, transaction, benId)));
    }

}
