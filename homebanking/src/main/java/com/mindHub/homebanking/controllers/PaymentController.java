package com.mindHub.homebanking.controllers;

import com.mindHub.homebanking.models.Account;
import com.mindHub.homebanking.models.Card;
import com.mindHub.homebanking.models.Transaction;
import com.mindHub.homebanking.models.TransactionType;
import com.mindHub.homebanking.repositories.AccountRepository;
import com.mindHub.homebanking.repositories.CardRepository;
import com.mindHub.homebanking.repositories.ClientRepository;
import com.mindHub.homebanking.repositories.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class PaymentController {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionsRepository transactionsRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CardRepository cardRepository;


    @Transactional
    @PostMapping("/payment")
    public ResponseEntity<Object> makePayment(@RequestParam String number,@RequestParam String cardHolder, @RequestParam Integer cvv, @RequestParam String expiredMonth, @RequestParam String expiredYear, @RequestParam String accountNumber, @RequestParam String montoADebitar,@RequestParam String descripcionPago){

        //Busco la tarjeta en el repositorio por el número de tarjeta
        Card card=cardRepository.findByNumber(number);

        // Busco la cuenta en el repositorio por el número de cuenta
        Account account=accountRepository.findByNumber(accountNumber);

        //Parseo el String que recibo como monto a double
        double monto = Double.parseDouble(montoADebitar);

        //Rescato el mes y año actual para ver si la tarjeta se encuentra vencida en una de las comprobaciones
        LocalDateTime fechaActual =LocalDateTime.now();
        String mesActual = String.valueOf(fechaActual.getMonth());
        String añoActual = String.valueOf(fechaActual.getYear());

        if (Objects.isNull(card)){
            return new ResponseEntity<>("El numero de tarjeta es inválido", HttpStatus.FORBIDDEN);
        }

        if (!card.getCardHolder().toUpperCase(Locale.ROOT).equals(cardHolder.toUpperCase())){
            return new ResponseEntity<>("Lo sentimos, el nombre asociado a la tarjeta es inválido", HttpStatus.FORBIDDEN);
        }

        if (!card.getCvv().equals(cvv)){
            return new ResponseEntity<>("Lo sentimos, el código de seguridad es incorrecto", HttpStatus.FORBIDDEN);
        }

        if (account.getBalance()<monto){
           return new ResponseEntity<>("Lo sentimos, el monto en la cuenta es insuficiente para realizar el pago", HttpStatus.FORBIDDEN);
        }

        // boolean isAfter( otherDateTime ) – Checks if given date-time is after the other date-time.
        //En este caso si la fecha actual es mayor que la fecha de la tarjeta quiere decir que la tarjeta está vencida al momento del pago
        if (card.getExpireDateThru().isBefore(LocalDateTime.now())){
            return new ResponseEntity<>("La tarjeta se encuentra vencida", HttpStatus.FORBIDDEN);
        }

        String descripcionDebito = montoADebitar+ " / " + descripcionPago +" / Abonado con Tarjeta: "+card.getNumber();
        Transaction transaction = new Transaction(TransactionType.DEBITO,monto,descripcionDebito, LocalDateTime.now());
        account.addTransaction(transaction);
        transactionsRepository.save(transaction);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
