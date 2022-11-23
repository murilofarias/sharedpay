package br.com.murilofarias.sharedpay.core.plug;

import br.com.murilofarias.sharedpay.core.model.Bill;
import br.com.murilofarias.sharedpay.core.model.Payment;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface PaymentRepository extends PagingAndSortingRepository<Payment, UUID> {

}
