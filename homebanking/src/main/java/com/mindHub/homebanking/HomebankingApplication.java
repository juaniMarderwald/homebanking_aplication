package com.mindHub.homebanking;

import com.mindHub.homebanking.models.*;
import com.mindHub.homebanking.repositories.*;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionsRepository transactionsRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {

			// save a couple of clients
			Client client1 = new Client("Melba", "Morel","melba@mindhub.com","123456");
			Client client2 = new Client("Melba", "Lorenzo","mlorenzo@mindhub.com","456789");
			Client client3 = new Client("Juan","Marderwald","jmarderwald87@gmail.com","123456");

			//Creo un admin
			Client admin = new Client("admin","admin","admin","123456");


			client1.setPassword(passwordEncoder.encode(client1.getPassword()));
			client2.setPassword(passwordEncoder.encode(client2.getPassword()));
			client3.setPassword(passwordEncoder.encode(client3.getPassword()));
			admin.setPassword(passwordEncoder.encode(admin.getPassword()));

			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);
			clientRepository.save(admin);

			Account account1 = new Account(LocalDateTime.now(),5000.0,"VIN001");
			account1.setAccountType(AccountType.CAJA_AHORRO);
			Account account2 = new Account(LocalDateTime.now().plusDays(1),7500.0,"VIN002");
			account2.setAccountType(AccountType.CAJA_AHORRO);
			Account account3 = new Account(LocalDateTime.now(), 15000,"VIN003");
			account3.setAccountType(AccountType.CUENTA_CORRIENTE);
			Account account4 = new Account(LocalDateTime.now(),2000,"VIN004");
			account4.setAccountType(AccountType.CUENTA_CORRIENTE);
			Account account5 = new Account(LocalDateTime.now(),150000,"VIN005");
			account5.setAccountType(AccountType.CAJA_AHORRO);
			// Las dos cuentas van a pertenecer a Melba, le asigno el owner, y las guardo en la BD.
			// Creo una tercer cuenta para el cliente 2
			account1.setClient(client1);
			account2.setClient(client1);
			account3.setClient(client2);
			account4.setClient(client2);
			account5.setClient(client1);


			client1.addAccount(account1);
			client1.addAccount(account2);
			client1.addAccount(account5);
			client2.addAccount(account3);
			client2.addAccount(account4);

			//Transacciones

			Transaction transaction1 = new Transaction(TransactionType.CREDITO,12359,"Trabajo de carpinteria",LocalDateTime.now().minusDays(5));
			Transaction transaction7 = new Transaction(TransactionType.CREDITO,12359,"Yerba para el mate",LocalDateTime.now().minusDays(1));
			Transaction transaction4=new Transaction(TransactionType.DEBITO,528,"Compra de pan en el almacen",LocalDateTime.now());
			Transaction transaction2 = new Transaction(TransactionType.CREDITO,65050,"Sueldo",LocalDateTime.now());
			Transaction transaction3= new Transaction(TransactionType.DEBITO,1026,"Compra cajon de cerveza",LocalDateTime.now());
			Transaction transaction5= new Transaction(TransactionType.DEBITO,842.59,"Compra caja de vino tetra",LocalDateTime.now());
			Transaction transaction6= new Transaction(TransactionType.CREDITO,1843,"Venta estupefacientes",LocalDateTime.now());
			Transaction transaction8 = new Transaction(TransactionType.CREDITO,2000,"Cosas",LocalDateTime.now().minusDays(3));
			Transaction transaction9 = new Transaction(TransactionType.CREDITO,1000," Zapatillas vendidas",LocalDateTime.now().minusDays(2));

			account1.addTransaction(transaction1);
			account1.addTransaction(transaction4);
			account1.addTransaction(transaction5);
			account1.addTransaction(transaction6);
			account1.addTransaction(transaction7);
			account1.addTransaction(transaction8);
			account1.addTransaction(transaction9);
			account3.addTransaction(transaction3);
			account3.addTransaction(transaction2);

			//Hipotecario, monto máximo 500.000, cuotas 12,24,36,48,60.
			//Personal, monto máximo 100.000, cuotas 6,12,24
			//Automotriz, monto máximo 300.000, cuotas 6,12,24,36


			//Creacion de las listas de cuotas, y posterior adicion de diferentes cantidad de cuotas para cada prestamo
			List<Integer> cuotasHipotecario= new ArrayList<>();
			List<Integer> cuotasPersonal= new ArrayList<>();
			List<Integer> cuotasAutomotriz= new ArrayList<>();
			cuotasHipotecario.add(12);
			cuotasHipotecario.add(24);
			cuotasHipotecario.add(36);
			cuotasHipotecario.add(48);
			cuotasHipotecario.add(60);

			cuotasPersonal.add(6);
			cuotasPersonal.add(12);
			cuotasPersonal.add(24);

			cuotasAutomotriz.add(6);
			cuotasAutomotriz.add(12);
			cuotasAutomotriz.add(24);
			cuotasAutomotriz.add(36);

			//Creo 3 Prestamos
			Loan loanHipotecario=new Loan("Hipotecario",500000,cuotasHipotecario);
			loanHipotecario.setInterest(0.2);
			Loan loanPersonal=new Loan("Personal",100000,cuotasPersonal);
			loanPersonal.setInterest(0.2);
			Loan loanAutomotriz=new Loan("Automotriz",300000,cuotasAutomotriz);
			loanAutomotriz.setInterest(0.2);

			ClientLoan clientLoan1=new ClientLoan(400000,60, client1,loanHipotecario);
			ClientLoan clientLoan2=new ClientLoan(50000,12,client1,loanAutomotriz);

			loanHipotecario.addClientLoan(clientLoan1);

			clientLoan2.setLoan(loanPersonal);
			loanPersonal.addClientLoan(clientLoan2);

			//Agregamos a Melba Morel los dos primeros prestamos
			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);

			//Creo los segundos prestamos para el segundo cliente Mabel Lorenzo
			ClientLoan clientLoan3=new ClientLoan(100000,24);
			ClientLoan clientLoan4= new ClientLoan(2000000,36);
			clientLoan3.setLoan(loanPersonal);
			loanPersonal.addClientLoan(clientLoan3);

			clientLoan4.setLoan(loanAutomotriz);
			loanPersonal.addClientLoan(clientLoan4);

			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);
			accountRepository.save(account5);

			transactionsRepository.save(transaction1);
			transactionsRepository.save(transaction2);
			transactionsRepository.save(transaction3);
			transactionsRepository.save(transaction4);
			transactionsRepository.save(transaction5);
			transactionsRepository.save(transaction6);
			transactionsRepository.save(transaction7);
			transactionsRepository.save(transaction8);
			transactionsRepository.save(transaction9);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);
			accountRepository.save(account5);

			loanRepository.save(loanHipotecario);
			loanRepository.save(loanPersonal);
			loanRepository.save(loanAutomotriz);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);


			//Creo Tress tarjetas tarjetas para Melba, dos de credito y una de debito

			Card card1 = new Card("4523 6859 2458 0184 3409",554,LocalDateTime.now().plusYears(5), LocalDateTime.now(),CardType.DEBIT,CardColor.GOLD,client1,true);
			Card card2 = new Card("4896 4561 1234 4895",487, LocalDateTime.now(), LocalDateTime.now(),CardType.CREDIT,CardColor.TITANIUM,client1,true );
			Card card4 = new Card("4879 5874 9876 5678",248, LocalDateTime.now().plusYears(5), LocalDateTime.now(),CardType.CREDIT,CardColor.SILVER,client1,true );

			//Creo una tarjeta Silver para el segundo usuario melba Lorenzo
			Card card3 = new Card("4879 2546 1234 5678",546, LocalDateTime.now(), LocalDateTime.now(),CardType.CREDIT,CardColor.TITANIUM,client2,true );

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
			cardRepository.save(card4);

		};
	}
}
