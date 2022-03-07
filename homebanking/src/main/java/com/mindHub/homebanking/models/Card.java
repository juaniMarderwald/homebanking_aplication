package com.mindHub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity

public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;
    private String cardHolder;
    private String number;
    private Integer cvv;
    private LocalDateTime expireDateFrom;
    private LocalDateTime expireDateThru;
    private CardColor cardColor;
    private CardType cardType;
    private Boolean esActiva;

    // Declaro la relacion Muchos a uno, quiere decir que muchas tarjetas pueden pertenecer a un Cliente
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_holder_id")
    private Client client;

    public Card() {}

    public Card( String number, Integer cvv, LocalDateTime expireDateThru, LocalDateTime expireDateFrom, CardType cardType, CardColor cardColor, Client client, Boolean activada) {
        this.cardHolder = client.getFirstName()+" "+client.getLastName();
        this.number = number;
        this.cvv = cvv;
        this.expireDateFrom = expireDateFrom;
        this.expireDateThru = expireDateThru;
        this.cardColor = cardColor;
        this.cardType = cardType;
        this.esActiva=activada;
        this.client = client;
        client.addCard(this);

    }

    public Card(String number, Integer cvv, LocalDateTime expireDateThru, LocalDateTime expireDateFrom, CardType cardType, CardColor cardColor, Client client) {

        //Concateno el nombre y el apellido para el cardHolder
        this.cardHolder = client.getFirstName()+" "+client.getLastName();
        this.number = number;
        this.cvv = cvv;
        this.expireDateFrom = expireDateFrom;
        this.expireDateThru = expireDateThru;
        this.cardColor = cardColor;
        this.cardType = cardType;
        this.client = client;
        client.addCard(this);
    }

    public Boolean getActivada() {
        return esActiva;
    }

    public void setActivada(Boolean activada) {
        this.esActiva = activada;
    }

    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getNumber() {
        return number;
    }

    public Integer getCvv() {
        return cvv;
    }

    public LocalDateTime getExpireDateFrom() {
        return expireDateFrom;
    }

    public LocalDateTime getExpireDateThru() {
        return expireDateThru;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public CardType getCardType() {
        return cardType;
    }

    public Client getClient() {
        return client;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public void setExpireDateFrom(LocalDateTime expireDateFrom) {
        this.expireDateFrom = expireDateFrom;
    }

    public void setExpireDateThru(LocalDateTime expireDateThru) {
        this.expireDateThru = expireDateThru;
    }

    public void setCardColor(CardColor cardColor) {
        this.cardColor = cardColor;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
