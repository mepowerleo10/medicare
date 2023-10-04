package com.marisia.medicare.repository;

import org.springframework.data.repository.CrudRepository;

import com.marisia.medicare.model.Payment;

public interface PaymentRepository extends CrudRepository<Payment, Long> {

}
