package com.mindHub.homebanking.controllers;


import com.mindHub.homebanking.extra.Utilities;
import com.mindHub.homebanking.models.Card;
import com.mindHub.homebanking.models.CardColor;
import com.mindHub.homebanking.models.CardType;
import com.mindHub.homebanking.models.Client;
import com.mindHub.homebanking.repositories.CardRepository;
import com.mindHub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CardRepository cardRepository;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam String color, @RequestParam String tipo){

        CardColor colorTarjeta = CardColor.valueOf(color);
        CardType tipoTarjeta = CardType.valueOf(tipo);

        //Declaro un objeto utilities para acceder a las funcionalidades de creacion de numeros de tarjetas al azar, necesarios para crear las tarjetas
        Utilities utilities = new Utilities();

        //Obtengo el cliente que está logeado para consultarle por la tarjeta
        Client currentClient = clientRepository.findByEmail(authentication.getName());

        if( currentClient.getCards().stream().filter(tarjeta->
        {
            boolean b = tarjeta.getCardType().toString().equals(tipo.toString()) && tarjeta.getActivada();
            return b;
        }).count()>2){
            return new ResponseEntity<>("El cliente ya posee 3 tarjetas del mismo tipo", HttpStatus.FORBIDDEN);
        }

        Card newCard = new Card(utilities.getRandomCardNumber(), utilities.getRandomCVV(), LocalDateTime.now().plusYears(5), LocalDateTime.now(),tipoTarjeta, colorTarjeta, clientRepository.findByEmail(authentication.getName()),true);
        cardRepository.save(newCard);
        return new ResponseEntity<>("201 Tarjeta creada correctamente",HttpStatus.CREATED);
    }

    //Metodo para borrar una tarjeta, que en realidad cambia una variable boolean que dice si está activa o no, para que aparezca comno si estuviera borrada pero sigue estando en la BD
    @PatchMapping("clients/delete/card")
    public ResponseEntity<Object> deleteCard(@RequestParam String cardNumber){
        System.out.println(cardNumber);
        Card card=cardRepository.findByNumber(cardNumber);
        card.setActivada(false);

        cardRepository.save(card);
        return new ResponseEntity<>("201 Tarjeta eliminada correctamente",HttpStatus.CREATED);
    }

}
