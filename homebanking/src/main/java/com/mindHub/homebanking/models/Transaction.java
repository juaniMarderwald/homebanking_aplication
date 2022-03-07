package com.mindHub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;
    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime date;

    // Declaro la relacion Muchos a uno, quiere decir que una cuenta puede de una a muchas transacciones
    @ManyToOne(fetch = FetchType.EAGER)
    //Le agrego una fila a la base de datos de transacciones, que se va a llamar accounnt_id, el cual es un identificador unico para la cuenta
    @JoinColumn(name = "account_owner_id")
    private Account account;

    public Transaction() {
    }

    public Transaction(long id, TransactionType type, double amount, String description, LocalDateTime date, Account account) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.account = account;
    }


    public Transaction(TransactionType type, double amount, String description, LocalDateTime date, Account account) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.account = account;
    }

    public Transaction(TransactionType type, double amount, String description, LocalDateTime date) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
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

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
