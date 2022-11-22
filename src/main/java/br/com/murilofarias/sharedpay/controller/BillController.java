package br.com.murilofarias.sharedpay.controller;

import br.com.murilofarias.sharedpay.controller.dto.PaymentDto;
import br.com.murilofarias.sharedpay.controller.dto.request.BillRequestDto;
import br.com.murilofarias.sharedpay.controller.dto.response.BillResponseDto;
import br.com.murilofarias.sharedpay.core.model.Bill;
import br.com.murilofarias.sharedpay.core.model.Payment;
import br.com.murilofarias.sharedpay.core.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/bills")
public class BillController {

    @Autowired
    BillService billService;

    @PostMapping
    public ResponseEntity<BillResponseDto> registerBill(@Valid @RequestBody BillRequestDto billRequestDto) {
        Bill bill = billService.registerBill(billRequestDto.toDomainModel());
        BillResponseDto response = new BillResponseDto(bill);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/payments")
    public ResponseEntity<List<PaymentDto>> getBillPayments(@PathVariable(value = "id") Long billId) {
        List<Payment> payments = billService.getBillPayments(billId);
        List<PaymentDto> response = payments.stream().map(PaymentDto::new).collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}/request-payments")
    public ResponseEntity<List<PaymentDto>> requestPayments(@PathVariable(value = "id") Long billId) {
        List<Payment> paymentsRequested = billService.requestPayments(billId);
        List<PaymentDto> response = paymentsRequested.stream().map(PaymentDto::new).collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
