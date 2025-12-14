package com.jpmc.midascore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class TransactionRecord {
    @Id
    @GeneratedValue
    public Long id;

    private  float amount;
    @ManyToOne
    private UserRecord sender;
    @ManyToOne
    private UserRecord recipient;

    private float incentive;

    protected TransactionRecord(){};

    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount, float incentive) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = incentive;
    }


}
