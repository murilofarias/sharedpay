package br.com.murilofarias.sharedpay.core.service;

import br.com.murilofarias.sharedpay.core.error.DomainException;
import br.com.murilofarias.sharedpay.core.error.PaymentServiceException;
import br.com.murilofarias.sharedpay.core.error.ResourceNotFoundException;
import br.com.murilofarias.sharedpay.core.model.Bill;
import br.com.murilofarias.sharedpay.core.model.Payment;
import br.com.murilofarias.sharedpay.core.model.PaymentStatus;
import br.com.murilofarias.sharedpay.core.model.User;
import br.com.murilofarias.sharedpay.core.plug.BillRepository;
import br.com.murilofarias.sharedpay.core.plug.PaymentRepository;
import br.com.murilofarias.sharedpay.core.plug.PaymentService;
import br.com.murilofarias.sharedpay.core.plug.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static br.com.murilofarias.sharedpay.util.CpfUtils.eliminateDotsAndDashes;

@Service
public class BillService {

    private UserRepository userRepository;

    private BillRepository billRepository;

    private PaymentService paymentService;


    private PaymentRepository paymentRepository;

    @Autowired
    public BillService(BillRepository billRepository, PaymentService paymentService,
                       PaymentRepository paymentRepository, UserRepository userRepository){
        this.billRepository = billRepository;
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    /**
     * generates Payments for the bill and saves it to the database.
     *
     * @param  bill the bill to be saved.
     *
     * @return the saved bill.
     */
    @Transactional
    public Bill registerBill(Bill bill){
        User billOwner = userRepository.findByPersonCpf(eliminateDotsAndDashes(bill.getOwner().getCpf())).orElseThrow(() ->
                new ResourceNotFoundException("There is no User with cpf " + bill.getOwner().getCpf() ));

        billOwner.chargeByBill(bill);
        bill.generatePayments();

        userRepository.save(billOwner);
        return billRepository.save(bill);
    }


    /**
     * gets all the payments of a bill .
     *
     * @param  billId  id of the bill
     *
     * @return the list of payments
     *
     * @throws ResourceNotFoundException if billId is not found
     */
    public List<Payment> getBillPayments(Long billId){
        Bill bill = billRepository.findById(billId).orElseThrow(() ->
                new ResourceNotFoundException("There is no Bill with id " + billId ));

        return bill.getPayments();
    }

    /**
     * requests payments not fulfilled yet for the bill .
     *
     * @param  billId  id of the bill which the payments that hasn't been made yet will be requested
     *
     * @return the list of payments not fulfilled with their paymentUrl updated.
     *
     * @throws ResourceNotFoundException if billId is not found
     * @throws PaymentServiceException if a problem happens when using the payment service.
     * @throws DomainException if some rule to update the paymentUrl in payment is violated.
     */
    @Transactional
    public List<Payment> requestPayments(Long billId){
        Bill bill = billRepository.findById(billId).orElseThrow(() ->
                new ResourceNotFoundException("There is no Bill with id " + billId ));

        List<Payment> paymentsNonFulfilled = bill
                .getPayments()
                .stream()
                .filter(payment -> !payment.getStatus().equals(PaymentStatus.FULFILLED))
                .collect(Collectors.toList());

        List<String> paymentsUrl = paymentService.requestPayments(paymentsNonFulfilled);


        for(int i =0; i < paymentsNonFulfilled.size(); i++)
            paymentsNonFulfilled.get(i).updatePaymentUrl(paymentsUrl.get(i));

        paymentRepository.saveAll(paymentsNonFulfilled);

        return paymentsNonFulfilled;
    }
}
