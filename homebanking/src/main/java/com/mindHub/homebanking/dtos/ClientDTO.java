package com.mindHub.homebanking.dtos;

import com.mindHub.homebanking.models.Account;
import com.mindHub.homebanking.models.Client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    private long id;
    private String firstName, lastName, email;

    private Set<AccountDTO> accounts = new HashSet<>();
    private Set<ClientLoanDTO> loans = new HashSet<>();
    private Set<CardDTO> cards = new HashSet<>();

    public ClientDTO(Client client){
        this.id=client.getId();
        this.firstName= client.getFirstName();
        this.lastName=client.getLastName();
        this.email =client.getEmail();

        this.cards = client.getCards().stream().map(CardDTO::new).collect(Collectors.toSet());
        this.accounts= client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());
        this.loans=client.getClientLoans().stream().map(ClientLoanDTO::new).collect(Collectors.toSet());
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public Set<ClientLoanDTO> getLoans(){return loans;}

    public Set<CardDTO> getCards() {return cards;}

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
