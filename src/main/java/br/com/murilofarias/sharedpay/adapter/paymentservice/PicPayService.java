package br.com.murilofarias.sharedpay.adapter.paymentservice;

import br.com.murilofarias.sharedpay.adapter.paymentservice.dto.RequestPaymentRequestDto;
import br.com.murilofarias.sharedpay.adapter.paymentservice.dto.RequestPaymentResponseDto;
import br.com.murilofarias.sharedpay.adapter.paymentservice.dto.RequestPaymentStatusResponseDto;
import br.com.murilofarias.sharedpay.core.error.PaymentServiceException;
import br.com.murilofarias.sharedpay.core.model.Bill;
import br.com.murilofarias.sharedpay.core.model.Payment;
import br.com.murilofarias.sharedpay.core.model.PaymentStatus;
import br.com.murilofarias.sharedpay.core.plug.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PicPayService implements PaymentService {

    @Value("${picpay.token.value}")
    private String tokenValue;

    @Value("${picpay.token.name}")
    private String tokenName;

    @Value("${picpay.base-url}")
    private String baseUrl;

    @Value("${picpay.request-payment.endpoint}")
    private String requestPaymentEndpoint;

    @Value("${picpay.request-payment.request.expires-at}")
    private Long requestPaymentExpiresAt;

    @Value("${picpay.request-payment.request.callback-url}")
    private String requestPaymentCallbackUrl;

    @Value("${picpay.request-payment.request.return-url}")
    private String requestPaymentReturnUrl;

    @Override
    public List<String> requestPayments(List<Payment> payments) {
        WebClient client = WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(tokenName, tokenValue)
            .build();


        return payments.stream()
            .parallel()
            .map(payment -> requestPayment(client, payment))
            .collect(Collectors.toList());


    }

    private String requestPayment(WebClient client, Payment payment){


        RequestPaymentRequestDto requestPaymentRequestDto = new RequestPaymentRequestDto(
                payment,
                requestPaymentExpiresAt,
                requestPaymentCallbackUrl,
                requestPaymentReturnUrl);

        RequestPaymentResponseDto response = client.post()
                .uri(requestPaymentEndpoint)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestPaymentRequestDto), RequestPaymentRequestDto.class)
                .retrieve()
                .bodyToMono(RequestPaymentResponseDto.class)
                .doOnError(throwable -> {throw new PaymentServiceException(throwable.getMessage());})
                .block();


        if(response != null)
            return response.getPaymentUrl();
        else
            throw new PaymentServiceException("It was not possible to get a response");
    }

    public PaymentStatus requestPaymentStatus(String paymentId){
        WebClient client = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(tokenName, tokenValue)
                .build();

        RequestPaymentStatusResponseDto response = client.get()
                .uri("/payments/"+ paymentId + "/status")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(RequestPaymentStatusResponseDto.class)
                .doOnError(throwable -> {throw new PaymentServiceException(throwable.getMessage());})
                .block();

        if(response.getStatus().equals("created"))
            return PaymentStatus.PENDING;
        else if(response.getStatus().equals("expired"))
            return PaymentStatus.NON_REQUESTED;
        else
            return PaymentStatus.FULFILLED;
    }

}
