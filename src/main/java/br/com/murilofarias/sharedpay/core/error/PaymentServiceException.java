package br.com.murilofarias.sharedpay.core.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class PaymentServiceException extends ResponseStatusException
{
    public PaymentServiceException(String message)
    {
        super(HttpStatus.BAD_GATEWAY, message);
    }
}