package com.mindHub.homebanking.dtos;

import com.mindHub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private long id, idLoan;
    private String name;
    private double amount;
    private Integer payments;

    public ClientLoanDTO() {
    }

    public ClientLoanDTO(ClientLoan clientLoan){
        id=clientLoan.getClient().getId();
        idLoan=clientLoan.getLoan().getId();
        name=clientLoan.getLoan().getName();
        amount=clientLoan.getAmount();
        payments=clientLoan.getPayments();
    }

    public long getId() {
        return id;
    }

    public long getIdLoan() {
        return idLoan;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }
}
