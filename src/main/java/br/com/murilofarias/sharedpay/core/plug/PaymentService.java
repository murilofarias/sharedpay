package br.com.murilofarias.sharedpay.core.plug;

import br.com.murilofarias.sharedpay.core.model.Bill;
import br.com.murilofarias.sharedpay.core.model.Payment;
import br.com.murilofarias.sharedpay.core.model.PaymentStatus;

import java.util.List;
import java.util.Map;

public interface PaymentService{
    List<String> requestPayments(List<Payment> payments);
    PaymentStatus requestPaymentStatus(String referenceId);
}
