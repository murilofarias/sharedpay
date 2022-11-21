package br.com.murilofarias.sharedpay.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PicpayNotificationRequestDto {

    @NotBlank
    private String referenceId;

    @NotBlank
    private String authorizationId;
}
