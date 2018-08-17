package com.vinaypabba.bankrest.service;

import com.vinaypabba.bankrest.model.Account;
import com.vinaypabba.bankrest.model.Beneficiary;
import com.vinaypabba.bankrest.model.Transaction;
import com.vinaypabba.bankrest.repo.AccountRepository;
import com.vinaypabba.bankrest.repo.BeneficiaryRepository;
import com.vinaypabba.bankrest.repo.TransactionRepository;
import com.vinaypabba.bankrest.util.BusinessException;
import com.vinaypabba.bankrest.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.lang.Math.min;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, BeneficiaryRepository beneficiaryRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.beneficiaryRepository = beneficiaryRepository;
        this.transactionRepository = transactionRepository;
    }

    public Account getAccountDetails(String accountNumber, boolean txnSummary) {
        Account account = accountRepository.findAccountByNumber(accountNumber);
        if(txnSummary) {
            List<Transaction> transactions = account.getTransactions();
            transactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
            account.setTransactions(transactions.subList(0, min(account.getTransactions().size(), 5)));
        }
        return account;
    }

    public List<Beneficiary> getBeneficiariesByAccountNumber(String accountNumber) {
        return beneficiaryRepository.findAllByAccount_Number(accountNumber);
    }

    public Account addAccount(Account account) {
        Account acc = getAccountDetails(account.getNumber(), false);
        if(null != acc) {
            throw new BusinessException("Account number already exists");
        }
        return accountRepository.save(account);
    }

    public Account addBeneficiaryToAccount(String accountNumber, Beneficiary beneficiary) {
        Account beneficiaryAccount = getAccountDetails(beneficiary.getAccountNumber(), false);
        Account account = getAccountDetails(accountNumber, false);
        account.getBeneficiaries()
                .stream()
                .filter(ben -> ben.getAccountNumber().equals(beneficiary.getAccountNumber()))
                .findAny()
                .ifPresent(ben -> {
                    throw new BusinessException("Beneficiary already exists");
                });
        if(null == beneficiaryAccount) {
            throw new BusinessException("Unable to find beneficiary account");
        }
        beneficiary.setAccount(account);
        Beneficiary b = beneficiaryRepository.save(beneficiary);
        account.addBeneficiary(b);
        return accountRepository.save(account);
    }

    public Account removeBeneficiaryFromAccount(String accountNumber, Long benId) {
        Beneficiary beneficiary = beneficiaryRepository.getOne(benId);
        Account account = getAccountDetails(accountNumber, false);
        account.removeBeneficiary(beneficiary);
        beneficiary.setAccount(null);
        beneficiaryRepository.delete(beneficiary);
        return accountRepository.save(account);
    }

    public boolean newTransaction(String accountNumber, Transaction transaction, Long benId) {
        Account sourceAccount = getAccountDetails(accountNumber, false);
        Beneficiary beneficiary = beneficiaryRepository.getOne(benId);
        if(Constants.TXN_TYPE_DEBIT.equals(transaction.getType()) && sourceAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new BusinessException("Insufficient Funds");
        }
        if(transaction.getAmount().compareTo(beneficiary.getTransferLimit()) > 0) {
            throw new BusinessException("Transfer limit for this beneficiary exceeded");
        }
        boolean beneficiaryNotPresent = sourceAccount.getBeneficiaries()
                .stream()
                .noneMatch(ben -> ben.getId().equals(benId));
        if(beneficiaryNotPresent) {
            throw new BusinessException("Beneficiary is not mapped to this Account");
        }
        Account targetAccount = getAccountDetails(beneficiary.getAccountNumber(), false);
        String transactionId = UUID.randomUUID().toString();
        transaction.setTransactionId(transactionId);
        transaction.setAccount(sourceAccount);
        Transaction sourceTransaction = transactionRepository.save(transaction);
        Transaction targetTransaction = transactionRepository.save(Transaction.createTargetTransaction(sourceTransaction, targetAccount));
        sourceAccount.updateBalance(sourceTransaction.getAmount(), sourceTransaction.getType());
        targetAccount.updateBalance(targetTransaction.getAmount(), targetTransaction.getType());
        sourceAccount.addTransaction(sourceTransaction);
        targetAccount.addTransaction(targetTransaction);
        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);
        return true;
    }

}
