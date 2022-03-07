package com.mindHub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
public class ClientLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;
    private double amount;
    Integer payments;

    // Declaro la relacion Muchos a uno, quiere decir que un tipo de prestamo puede lo puede tener mas de un cliente
    @ManyToOne(fetch = FetchType.EAGER)
    //Le agrego una Columna a la base de datos de ClientLoan
    @JoinColumn(name = "client_id")
    private Client client_owner;

    // Declaro la relacion Muchos a uno, muchos clientes pueden tener el mismo tipo de prestamo
    @ManyToOne(fetch = FetchType.EAGER)
    //Le agrego una columna a la base de datos de ClientLoan, que se va a llamar loan_id, el cual es un identificador unico para el dueño del prestamo
    @JoinColumn(name = "loan_id")
    private Loan loan_owner;

    public ClientLoan() {
    }

    public ClientLoan(double amount, Integer payments) {
        this.amount = amount;
        this.payments=payments;
    }

    public ClientLoan(double amount, Integer payments, Client client, Loan loan){
        this.amount = amount;
        this.payments=payments;
        this.client_owner=client;
        this.loan_owner=loan;

    }

    public void setClient(Client client_owner) {
        this.client_owner = client_owner;
    }

    public void setLoan(Loan loan_owner) {
        this.loan_owner = loan_owner;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public Client getClient() {
        return client_owner;
    }

    public Loan getLoan() {
        return loan_owner;
    }

    public Integer getPayments() {
        return payments;
    }


}
