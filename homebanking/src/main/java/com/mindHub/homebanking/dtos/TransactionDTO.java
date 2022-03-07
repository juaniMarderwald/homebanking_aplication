package com.mindHub.homebanking.dtos;

import com.mindHub.homebanking.models.Transaction;
import com.mindHub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {


    private long id;
    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime date;

    public TransactionDTO(Transaction transaction) {
        this.id=transaction.getId();
        this.type=transaction.getType();
        this.amount=transaction.getAmount();
        this.description= transaction.getDescription();
        this.date=transaction.getDate();
    }

    public long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
