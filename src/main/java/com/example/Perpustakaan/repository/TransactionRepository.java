package com.example.Perpustakaan.repository;

import com.example.Perpustakaan.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
