package br.com.murilofarias.sharedpay.controller.dto.response;

import br.com.murilofarias.sharedpay.core.model.Payment;
import br.com.murilofarias.sharedpay.core.model.PaymentStatus;
import br.com.murilofarias.sharedpay.core.model.Person;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponseDto {


    private String id;


    private PaymentStatus status;


    private Person debtor;

    private BigDecimal value;


    private OffsetDateTime fulfillmentDate;

    public PaymentResponseDto(Payment payment){
        this.id = payment.getId().toString();
        this.status = payment.getStatus();
        this.debtor = payment.getDebtor();
        this.value = payment.getValue();
        this.fulfillmentDate = payment.getFulfillmentDate();
    }
}
