package br.com.murilofarias.sharedpay.controller.dto;

import br.com.murilofarias.sharedpay.core.model.Bill;
import br.com.murilofarias.sharedpay.core.model.Payment;
import br.com.murilofarias.sharedpay.core.model.PaymentStatus;
import br.com.murilofarias.sharedpay.core.model.Person;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDto {


    private Long id;

    private PaymentStatus status;

    private PersonDto debtor;

    private OffsetDateTime fulfillmentDate;

    private BigDecimal value;

    private String paymentUrl;

    public PaymentDto(Payment payment){
        this.id = payment.getId();
        this.status = payment.getStatus();
        this.debtor = new PersonDto(payment.getDebtor());
        this.fulfillmentDate = payment.getFulfillmentDate();
        this.value = payment.getValue();
        this.paymentUrl = payment.getPaymentUrl();
    }
}
