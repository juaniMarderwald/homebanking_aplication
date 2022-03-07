package com.mindHub.homebanking.controllers;

import com.mindHub.homebanking.models.Account;
import com.mindHub.homebanking.models.Client;
import com.mindHub.homebanking.repositories.AccountRepository;
import com.mindHub.homebanking.repositories.ClientRepository;
import com.mindHub.homebanking.repositories.TransactionsRepository;
import com.mindHub.homebanking.services.PDFGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/api")
public class PDFExportController {

    @Autowired
    PDFGeneratorService pdfGeneratorService;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/pdf/generate/{id}")
    public void generatePDF(HttpServletResponse response, Authentication authentication, @PathVariable long id, @RequestParam String fechaDesde, @RequestParam String fechaHasta ) throws IOException {

        Client currentClient = clientRepository.findByEmail(authentication.getName());
        Account account=accountRepository.getById(id);

        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=balance_"+currentClient.getFirstName()+"_"+currentClient.getLastName()+".pdf";
        response.setHeader(headerKey,headerValue);

        pdfGeneratorService.export(response,currentClient,account, fechaDesde,fechaHasta);

    }
}
