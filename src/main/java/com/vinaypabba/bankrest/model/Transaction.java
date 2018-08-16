package com.vinaypabba.bankrest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vinaypabba.bankrest.util.Constants;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "transactions")
@JsonIgnoreProperties({"updatedAt", "createdAt", "account"})
public class Transaction implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private String type;
    private BigDecimal amount;
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiary_id")
    private Beneficiary beneficiary;*/

    private Date createdAt;
    private Date updatedAt;

    public static Transaction createTargetTransaction(Transaction source, Account targetAccount) {
        Transaction target = new Transaction();
        target.setTransactionId(source.getTransactionId());
        target.setAccount(targetAccount);
        target.setAmount(source.getAmount());
        target.setCreatedAt(source.getCreatedAt());
        target.setUpdatedAt(source.getUpdatedAt());
        target.setDate(source.getDate());
        target.setType(Constants.TXN_TYPE_CREDIT.equals(source.getType()) ? Constants.TXN_TYPE_DEBIT : Constants.TXN_TYPE_CREDIT);
        return target;
    }

}
