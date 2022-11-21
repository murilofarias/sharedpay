package br.com.murilofarias.sharedpay.controller.dto.response;


import br.com.murilofarias.sharedpay.core.model.Payment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterBillResponseDto {
    private List<PaymentResponseDto> payments;

    private Long BillId;

    public RegisterBillResponseDto(List<Payment> payments){
        this.payments = payments
                .stream()
                .map(PaymentResponseDto::new)
                .collect(Collectors.toList());
    }
}
