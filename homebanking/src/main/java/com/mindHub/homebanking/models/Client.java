package com.mindHub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;
    private String firstName, lastName, email, password;

    // Defino la relacion uno a muchos, quiere decir que un cliente puede tener más de una cuenta(1 o muchas), pero una cuenta solo puede pertenecer a un cliente
    // Esto se declara especificamente para la BD
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client_owner", fetch = FetchType.EAGER)
    Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    Set<Card> cards = new HashSet<>();

    //Constructores de la clase
    public Client() {}

    public Client(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email =email;
    }

    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public List<Loan> getLoans(){
    //recorro el Set de clientLoans y lo mapeo a una lista de elementos de tipo Loan
        return this.clientLoans.stream().map(cLoan -> cLoan.getLoan()).collect(toList());
    }

    public List<ClientLoan> getClientLoans(){
        return new ArrayList<>(this.clientLoans);
    }

    // Devuelvo un Set de las tarjetas del cliente
    public Set<Card> getCards(){
        return this.cards;
    }

    //Devuelvo la lista de accounts
    public Set<Account> getAccounts() {
        return accounts;
    }

    //Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //Agrego una nueva cuenta al cliente
    public void addAccount(Account account){
        account.setClient(this);
        accounts.add(account);
    }
    //Agrego un ClientLoan al Client
    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        this.clientLoans.add(clientLoan);
    }

    //Agrego una tarjeta a la lista de tarjetas del cliente
    public void addCard(Card card){
        card.setClient(this);
        this.cards.add(card);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
