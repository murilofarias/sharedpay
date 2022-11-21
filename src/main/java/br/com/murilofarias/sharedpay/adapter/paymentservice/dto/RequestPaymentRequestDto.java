package br.com.murilofarias.sharedpay.adapter.paymentservice.dto;

import br.com.murilofarias.sharedpay.core.model.Payment;
import br.com.murilofarias.sharedpay.core.model.Person;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class RequestPaymentRequestDto{

    private String referenceId;
    private String callbackUrl;
    private String returnUrl;
    private String expiresAt;
    private BigDecimal value;
    private BuyerDto buyer;

    public RequestPaymentRequestDto(Payment payment, Long expiresAt, String callbackUrl, String returnUrl){
        this.referenceId = payment.getId().toString();
        this.buyer = new BuyerDto(payment.getDebtor());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        this.expiresAt = OffsetDateTime.now().plusMinutes(expiresAt).format(formatter);
        this.value = payment.getValue();
        this.callbackUrl = callbackUrl;
        this.returnUrl = returnUrl;
    }
}
