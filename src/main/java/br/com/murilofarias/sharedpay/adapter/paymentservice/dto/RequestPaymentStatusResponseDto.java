package br.com.murilofarias.sharedpay.adapter.paymentservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
public class RequestPaymentStatusResponseDto {


    private String referenceId;

    private String authorizationId;

    private String status;
}