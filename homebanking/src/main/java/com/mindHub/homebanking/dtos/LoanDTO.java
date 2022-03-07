package com.mindHub.homebanking.dtos;

import com.mindHub.homebanking.models.Loan;
import java.util.ArrayList;
import java.util.List;

public class LoanDTO {
    //Esta clase DTO funciona para mandar los prestamos disponibles para mostrar por pantalla
    private long id;
    private String name;
    private double maxAmount;
    private List<Integer> payments = new ArrayList<>();
    private double interest;

    public LoanDTO(Loan loan) {
        this.id= loan.getId();
        this.name= loan.getName();
        this.maxAmount= loan.getMaxAmount();
        this.payments=loan.getPayments();
        this.interest=loan.getInterest();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public double getInterest() {
        return interest;
    }
}
