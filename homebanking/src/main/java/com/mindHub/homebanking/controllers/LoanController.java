package com.mindHub.homebanking.controllers;

import com.mindHub.homebanking.dtos.LoanApplicationDTO;
import com.mindHub.homebanking.dtos.LoanDTO;
import com.mindHub.homebanking.models.*;
import com.mindHub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionsRepository transactionsRepository;

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> takeLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){


        //Busco en el repositorio el Loan y el cliente segun los datos que traje desde el loanApplicationDTO desde el front
        Loan loan = loanRepository.findByName(loanApplicationDTO.getLoanName());
        Client client = clientRepository.findByEmail(authentication.getName());

        //Parseo el amoun y cuotas a los tipos correctos, ya que traigo String desde el front
        double amount = Double.parseDouble(loanApplicationDTO.getMonto());
        Integer cuotas = Integer.valueOf(loanApplicationDTO.getCuotas());

        String description = "Credito de tipo: "+loan.getName()+" otorgado a la cuenta: "+loanApplicationDTO.getCuentaDestino();

        //Verifico que el monto solicitado no exceda lo permitido por el prestamo
        if (amount>loan.getMaxAmount()){
            return new ResponseEntity<>("El monto solicitado es mayor que el permitido para este Prestamo", HttpStatus.FORBIDDEN);
        }

        //Compruebo que exista el prestamo solicitado, en teoria nunca tendria que entrar a esta comprobación, ya que lo corroboro en el front que esto no suceda
        if(Objects.isNull(loan)){
            return new ResponseEntity<>("El tipo de prestamo solicitado no existe", HttpStatus.FORBIDDEN);
        }

        //Compruebo que el monto ingresado no sea negativo
        if(amount<=0){
            return new ResponseEntity<>("El monto ingresado para el préstamo es inválido", HttpStatus.FORBIDDEN);
        }

        if (cuotas==0){
            return new ResponseEntity<>("La cantidad de cuotas ingresado es 0, por favor elija una cantidad de cuotas válida", HttpStatus.FORBIDDEN);
        }



        //Hago la transaccion de tipo credit a la cuenta destino del usuario verificado
        Account cuentaDestino = accountRepository.findByNumber(loanApplicationDTO.getCuentaDestino());

        //Corroboro que la cuenta de Destino del préstamo sea válida
        if (Objects.isNull(cuentaDestino)){
            return new ResponseEntity<>("La Cuenta de destino es inválida", HttpStatus.FORBIDDEN);
        }

        Transaction transaction = new Transaction(TransactionType.CREDITO,amount,description, LocalDateTime.now());
        cuentaDestino.addTransaction(transaction);

        transactionsRepository.save(transaction);

        //Le sumo 1 al interés del préstamo para sacar el monto final a pagar.
        double finalInterest = 1+loan.getInterest();

        double amountFinal = amount * finalInterest;
        ClientLoan clientLoan = new ClientLoan(amountFinal,cuotas,client,loan);
        clientLoanRepository.save(clientLoan);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }


}
