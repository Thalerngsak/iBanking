package com.oddscorp.internetbanking.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.oddscorp.internetbanking.domain.SavingsTransaction;

public interface SavingsTransactionDao extends CrudRepository<SavingsTransaction, Long> {
    List<SavingsTransaction> findAll();
}
