package com.marisia.medicare.service.payment;

import com.marisia.medicare.model.Payment;

public interface PaymentService {
  Payment make(Payment payment);
}
