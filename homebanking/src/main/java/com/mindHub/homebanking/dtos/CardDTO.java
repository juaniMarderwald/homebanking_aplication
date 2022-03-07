package com.mindHub.homebanking.dtos;

import com.mindHub.homebanking.models.Card;
import com.mindHub.homebanking.models.CardColor;
import com.mindHub.homebanking.models.CardType;

import java.time.LocalDateTime;

public class CardDTO {

    private long id;
    private String cardHolder;
    private String number;
    private Integer cvv;
    private LocalDateTime FromDate;
    private LocalDateTime ThruDate;
    private CardColor cardColor;
    private CardType cardType;
    private Boolean esActiva;

    public CardDTO(Card card) {
        this.id=card.getId();
        this.cardHolder= card.getCardHolder();
        this.cardType=card.getCardType();
        this.cardColor=card.getCardColor();
        this.number=card.getNumber();
        this.cvv=card.getCvv();
        this.FromDate=card.getExpireDateFrom();
        this.ThruDate=card.getExpireDateThru();
        this.esActiva=card.getActivada();
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

    public LocalDateTime getFromDate() {
        return FromDate;
    }

    public LocalDateTime getThruDate() {
        return ThruDate;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public CardType getCardType() {
        return cardType;
    }

    public Boolean getEsActiva(){return this.esActiva;}
}
