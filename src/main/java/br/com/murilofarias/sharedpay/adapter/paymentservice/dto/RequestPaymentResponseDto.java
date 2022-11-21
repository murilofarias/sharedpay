package br.com.murilofarias.sharedpay.adapter.paymentservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPaymentResponseDto {

    private String referenceId;

    private String paymentUrl;


}
