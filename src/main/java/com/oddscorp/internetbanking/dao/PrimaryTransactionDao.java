package com.oddscorp.internetbanking.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.oddscorp.internetbanking.domain.PrimaryTransaction;

public interface PrimaryTransactionDao extends CrudRepository<PrimaryTransaction, Long> {
	List<PrimaryTransaction> findAll();
}
