package com.mindHub.homebanking.controllers;

import com.mindHub.homebanking.dtos.AccountDTO;
import com.mindHub.homebanking.dtos.ClientDTO;
import com.mindHub.homebanking.extra.Utilities;
import com.mindHub.homebanking.models.Account;
import com.mindHub.homebanking.models.AccountType;
import com.mindHub.homebanking.models.CardColor;
import com.mindHub.homebanking.models.Client;
import com.mindHub.homebanking.repositories.AccountRepository;
import com.mindHub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    //Servicio que devuelve todas las cuentas del repositorio
    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    //Servicio que devuelve un accountDTO de acuerdo a un ID pasado por parámetro
    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable long id){
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    //Servicio que devuelve el nombre del titular de una cuenta en especifico, de acuerdo al numero de cuenta pasado por parametro al path
    @RequestMapping("/account/{accountNumber}")
    public String getNombreTitularCuenta(@PathVariable String accountNumber){
        Account cuenta = accountRepository.findByNumber(accountNumber);
        return cuenta.getClient().getFirstName()+" "+cuenta.getClient().getLastName();
    }

    //Servicio que crea una cuenta nueva para el usuario autenticado, permite crear un máximo de 3 cuentas
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(@RequestParam String tipoCuenta,Authentication authentication){
        AccountType accountType=AccountType.valueOf(tipoCuenta);
        Utilities utilities =new Utilities();
        Client currentClient =  clientRepository.findByEmail(authentication.getName());
        //Corroboro que el cliente tenga menos de 3 cuentas, si tiene 3 cuentas, no permitirá realizar la operación
        if (currentClient.getAccounts().size() > 2){
            return new ResponseEntity<>("El cliente ya posee 3 cuentas", HttpStatus.FORBIDDEN);
        }

        //Creo una nueva cuenta con balance 0
        Account newAccount= new Account( LocalDateTime.now(),0, utilities.getRandomAccountNumber(), currentClient,accountType);
        currentClient.addAccount(newAccount);
        accountRepository.save(newAccount);

        return new ResponseEntity<>("200 Cuenta creada correctamente",HttpStatus.CREATED);
    }

    //Servicio que elimina la cuenta del usuario actual, eliminando primero todas las transacciones de la cuenta para luego eliminar la cuenta
    @DeleteMapping("/clients/current/accounts")
    public ResponseEntity<Object> deleteAccount(Authentication authentication){
        return new ResponseEntity<>("200 Cuenta borrada con éxito",HttpStatus.CREATED);
    }

}
