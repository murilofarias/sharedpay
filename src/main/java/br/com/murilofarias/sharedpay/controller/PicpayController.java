package br.com.murilofarias.sharedpay.controller;

import br.com.murilofarias.sharedpay.controller.dto.PaymentDto;
import br.com.murilofarias.sharedpay.controller.dto.request.BillRequestDto;
import br.com.murilofarias.sharedpay.controller.dto.request.PicpayNotificationRequestDto;
import br.com.murilofarias.sharedpay.core.model.Payment;
import br.com.murilofarias.sharedpay.core.service.BillNotificationService;
import br.com.murilofarias.sharedpay.core.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PicpayController {

    @Autowired
    BillNotificationService billNotificationService;

    @PostMapping("/confirm-payment")
    public ResponseEntity<String> getBillPayments(
            @RequestBody PicpayNotificationRequestDto picpayNotificationRequestDto) {
        String response = billNotificationService.checkPaymentStatus(picpayNotificationRequestDto.getReferenceId());
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }
}
