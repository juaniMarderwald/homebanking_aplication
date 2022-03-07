package com.mindHub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;

    private LocalDateTime  creationDate;
    private double balance;
    private String number;
    private AccountType accountType;

    // Declaro la relacion Uno a Muchos, quiere decir que una cuenta va a tener una o muchas transacciones
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    Set<Transaction> transactions=new HashSet<>();

    // Declaro la relacion Muchos a uno, quiere decir que un cliente puede tener más de una cuenta
    @ManyToOne(fetch = FetchType.EAGER)
    //Le agrego una fila a la base de datos de cuentas, que se va a llamar owner_id, el cual es un identificador unico para el dueño de la cuenta
    @JoinColumn(name = "owner_id")
    private Client client;

    //Constructores
    public Account() {}

    public Account(long id, LocalDateTime creationDate, double balance, String number) {
        this.id = id;
        this.creationDate = creationDate;
        this.balance = balance;
        this.number = number;
    }

    public Account(LocalDateTime creationDate, double balance, String number) {
        this.creationDate = creationDate;
        this.balance = balance;
        this.number = number;
    }

    public Account(LocalDateTime creationDate,double balance, String number, Client client){
        this.creationDate = creationDate;
        this.balance = balance;
        this.number = number;
        this.client=client;
    }

    public Account(LocalDateTime creationDate,double balance, String number, Client client, AccountType accountType){
        this.creationDate = creationDate;
        this.balance = balance;
        this.number = number;
        this.client=client;
        this.accountType=accountType;
    }


    //Metodos gets y Sets

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public String getNumber() {
        return number;
    }

    public Client getClient() { return client; }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    // Hago un método para agregar una transaccion a la lista de transacciones de la cuenta, si es CREDITO aumento el balance, si es DEBITO resto al balance.
    public void addTransaction(Transaction transaction){
        if(transaction.getType().equals(TransactionType.DEBITO)){
            if (transaction.getAmount()<this.balance){
                this.balance=this.balance-transaction.getAmount();
                this.transactions.add(transaction);
                transaction.setAccount(this);
            }else{
                System.out.println("NO SE PUEDE REALIZAR LA TRANSACCION POR FALTA DE FONDOS");
            }
        }

        if (transaction.getType().equals(TransactionType.CREDITO)){
            this.balance = this.balance+transaction.getAmount();
            this.transactions.add(transaction);
            transaction.setAccount(this);
        }

    }
}
