package com.mindHub.homebanking.controllers;

import com.mindHub.homebanking.models.Account;
import com.mindHub.homebanking.models.Client;
import com.mindHub.homebanking.models.Transaction;
import com.mindHub.homebanking.models.TransactionType;
import com.mindHub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GrapichsController {

    @Autowired ClientRepository clientRepository;

    @GetMapping("/dailyBalance")
    public double[] getDailyBalanceCurrentMonth(Authentication authentication){
        Calendar fecha = new GregorianCalendar();
        int mes = fecha.get(Calendar.MONTH)+1;
        System.out.println("MES");
        System.out.println(mes);
        Client currentClient = clientRepository.findByEmail(authentication.getName());
        Set<Account> accounts = currentClient.getAccounts();

        Set<Transaction> transactions = new HashSet<>();
        accounts.forEach(account -> {
            transactions.addAll(account.getTransactions());
        });
        System.out.println(transactions.size());
        Set<Transaction> transaccionesMesActual = new HashSet<>();
        transaccionesMesActual=transactions.stream().filter(transaction -> transaction.getDate().getMonth().getValue() == mes).collect(Collectors.toSet());

        System.out.println(transaccionesMesActual);

        double[] balanceDiario = new double[31];

        transaccionesMesActual.forEach(transaction -> {
            int dia=transaction.getDate().getDayOfMonth();
            if(transaction.getType().equals(TransactionType.CREDITO)){
                balanceDiario[dia-1] += transaction.getAmount();
            }
            if(transaction.getType().equals(TransactionType.DEBITO)){
                balanceDiario[dia-1] -= transaction.getAmount();
            }

        });

        return balanceDiario;
    }


}
