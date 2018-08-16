package com.vinaypabba.bankrest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vinaypabba.bankrest.util.Constants;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "accounts")
@JsonIgnoreProperties({"updatedAt", "createdAt", "hibernateLazyInitializer", "handler"})
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String number;
    private BigDecimal balance = BigDecimal.ZERO;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    private List<Beneficiary> beneficiaries = new ArrayList<>();

    private Date createdAt;
    private Date updatedAt;

    public void addBeneficiary(Beneficiary beneficiary) {
        beneficiaries.add(beneficiary);
        beneficiary.setAccount(this);
    }

    public void removeBeneficiary(Beneficiary beneficiary) {
        beneficiaries.remove(beneficiary);
        beneficiary.setAccount(null);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setAccount(this);
    }

    public void updateBalance(BigDecimal amount, String txnType) {
        if(Constants.TXN_TYPE_CREDIT.equals(txnType)) {
            this.balance = this.balance.add(amount);
        } else if (Constants.TXN_TYPE_DEBIT.equals(txnType)) {
            this.balance = this.balance.subtract(amount);
        }
    }

}
