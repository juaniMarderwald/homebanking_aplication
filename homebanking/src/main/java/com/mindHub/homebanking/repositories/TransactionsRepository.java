package com.mindHub.homebanking.repositories;

import com.mindHub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface TransactionsRepository extends JpaRepository<Transaction,Long> {
}
