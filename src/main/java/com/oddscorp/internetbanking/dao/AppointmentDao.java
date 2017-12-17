package com.oddscorp.internetbanking.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.oddscorp.internetbanking.domain.Appointment;

public interface AppointmentDao extends CrudRepository<Appointment, Long> {

    List<Appointment> findAll();
}
