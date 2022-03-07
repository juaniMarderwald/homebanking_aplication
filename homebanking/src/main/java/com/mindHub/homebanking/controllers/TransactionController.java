package com.mindHub.homebanking.controllers;

import com.mindHub.homebanking.models.Account;
import com.mindHub.homebanking.models.Transaction;
import com.mindHub.homebanking.models.TransactionType;
import com.mindHub.homebanking.repositories.AccountRepository;
import com.mindHub.homebanking.repositories.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionsRepository transactionsRepository;

    @Transactional
    @PostMapping("/transaction")
    public ResponseEntity<Object> doTransaction(@RequestParam String originAccountNumber, @RequestParam String destinyAccountNumber, @RequestParam String strAmount, @RequestParam String description){
        double amount = Double.parseDouble(strAmount);
        // Busco las cuentas origen y destino en el repositorio
        Account originAccount = accountRepository.findByNumber(originAccountNumber);
        Account destinyAccount = accountRepository.findByNumber(destinyAccountNumber);

        if (!originAccountNumber.equals("") || !destinyAccountNumber.equals("") || amount !=0 || !description.equals("")){
            if (destinyAccount!=null){
                if (!originAccountNumber.equals(destinyAccountNumber)){// si la cuenta de origen no es igual a la cuenta destino
                    if (originAccount.getBalance()>amount){

                        String destinyDescription = description + " - " +originAccount.getClient().getFirstName()+" " + originAccount.getClient().getLastName();
                        String originDescription=description+" - a: " +destinyAccount.getClient().getFirstName()+" "+destinyAccount.getClient().getLastName();

                        Transaction transactionCredit = new Transaction(TransactionType.CREDITO, amount,destinyDescription, LocalDateTime.now(), destinyAccount);
                        Transaction transactionDebit = new Transaction(TransactionType.DEBITO, amount,originDescription, LocalDateTime.now(), originAccount);

                        originAccount.addTransaction(transactionDebit);
                        destinyAccount.addTransaction(transactionCredit);

                        transactionsRepository.save(transactionCredit);
                        transactionsRepository.save(transactionDebit);
                        return new ResponseEntity<>(HttpStatus.CREATED);
                    }
                    else
                        {
                        return new ResponseEntity<>("Dinero insuficiente para realizar la transacción", HttpStatus.FORBIDDEN);

                    }
                }
                else{
                    return new ResponseEntity<>(" Cuenta de origen igual a cuenta de destino ", HttpStatus.FORBIDDEN);
                }
            }else{
                return new ResponseEntity<>("No existe la cuenta destino ", HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<>("No se puede realizar la transacción por falta de datos ", HttpStatus.FORBIDDEN);
        }



    }

}
