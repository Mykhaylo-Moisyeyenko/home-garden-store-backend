package com.homegarden.store.backend.exception;

public class PaymentNotFoundException extends RuntimeException {

  public PaymentNotFoundException(Long paymentId) {
    super("Payment with paymentId=" + paymentId + " not found");
  }
}


