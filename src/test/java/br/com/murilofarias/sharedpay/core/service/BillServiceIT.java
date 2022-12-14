package br.com.murilofarias.sharedpay.core.service;

import br.com.murilofarias.sharedpay.core.model.*;
import br.com.murilofarias.sharedpay.core.plug.BillRepository;
import br.com.murilofarias.sharedpay.core.plug.PaymentRepository;
import br.com.murilofarias.sharedpay.core.plug.PaymentService;
import br.com.murilofarias.sharedpay.core.plug.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BillServiceIT {



    @Autowired
    BillRepository billRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Mock
    PaymentService paymentService;

    @Autowired
    BillService billService;


    private Bill bill;
    private User billOwner;

    @BeforeEach
    public void init() {

        MockitoAnnotations.openMocks(this);

        Person person1 = new Person("Alan", "Santos", "329.616.180-53");
        Person person2 = new Person("Brand", "Kay", "601.624.520-80");
        Person person3 = new Person("Gilbert", "Jey", "905.424.800-93");

        billOwner = new User(person1);
        IndividualSpending individualSpending1 = new IndividualSpending(new BigDecimal("12.65"), person1 );
        IndividualSpending individualSpending2 = new IndividualSpending(new BigDecimal("25.15"), person2 );
        IndividualSpending individualSpending3 = new IndividualSpending(new BigDecimal("31.92"), person3 );

        List<IndividualSpending> individualSpendings =
                Arrays.asList(individualSpending1, individualSpending2, individualSpending3);

        BigDecimal additionals = new BigDecimal("8.20");
        BigDecimal discounts = new BigDecimal("5.00");
        Boolean hasWaiterService = true;
        Boolean includeOwnerPayment = false;
        bill = new Bill(
                additionals,
                discounts,
                hasWaiterService,
                individualSpendings,
                includeOwnerPayment,
                billOwner.getPerson().getCpf());
        billService =  new BillService(billRepository,paymentService,  paymentRepository, userRepository);

    }

    @Test
    @Transactional
    void registerBill_whenUserExistsAndHasSufficientCredit_shouldReturnBillWithIdAndPayments() {
        //Arrange
        ReflectionTestUtils.setField(billOwner, "credit", BigDecimal.valueOf(100));
        userRepository.save(billOwner);

        //Act
        Bill  billReturned = billService.registerBill(bill);

        //Assert
        assertThat(billReturned).isNotNull();
        assertThat(billReturned.getId()).isNotNull();
        assertThat(billReturned.getPayments()).isNotNull();


    }

    @Test
    @Sql("classpath:test-data.sql")
    @Transactional
    void getBillPayments_whenBillIsInTheRepository_ShouldReturnBillPayments() {
        List<Payment> billPaymentsExpected = providePayments();

        //Act
        List<Payment> paymentsReturned = billService.getBillPayments(245L);

        //Assert
        assertEquals(billPaymentsExpected.size(), paymentsReturned.size());
        for(int i = 0; i< billPaymentsExpected.size(); i++) {
            assertEquals(billPaymentsExpected.get(i).getPaymentUrl(), paymentsReturned.get(i).getPaymentUrl());
            assertEquals(billPaymentsExpected.get(i).getStatus(), paymentsReturned.get(i).getStatus());
            assertEquals(billPaymentsExpected.get(i).getValue(), paymentsReturned.get(i).getValue());
            assertEquals(billPaymentsExpected.get(i).getDebtor().getCpf(), paymentsReturned.get(i).getDebtor().getCpf());
        }
    }


    @Test
    @Sql("classpath:test-data.sql")
    @Transactional
    void requestPayments_whenPaymentServiceIsSuccessful_shouldReturnPendingPaymentsWithPaymentUrlFromPaymentService() {
        String paymentUrl1 = "https://app.picpay.com/checkout/NWZkOTK";
        String paymentUrl2 = "https://app.picpay.com/checkout/NWZkOTA";
        List<String> paymentsUrl = new ArrayList<>(Arrays.asList(paymentUrl1, paymentUrl2));
        when(paymentService.requestPayments(any(List.class))).thenReturn(paymentsUrl);

        //Act
        List<Payment> paymentsReturned = billService.requestPayments(245L);

        //Assert
        assertEquals(paymentsUrl.size(), paymentsReturned.size());
        for(int i = 0; i< paymentsReturned.size(); i++) {
            assertEquals(paymentsUrl.get(i), paymentsReturned.get(i).getPaymentUrl());
            assertEquals(PaymentStatus.PENDING, paymentsReturned.get(i).getStatus());
        }
    }

    private List<Payment> providePayments(){
        String paymentUrl1 = "https://app.picpay.com/checkout/NWZkOTK";
        String paymentUrl2 = "https://app.picpay.com/checkout/NWZkOTA";
        Person person1 = new Person("Brand", "Kay", "601.624.520-80");
        Payment payment1 = new Payment(person1, new BigDecimal("20.22"), bill);
        payment1.setStatus(PaymentStatus.PENDING);
        ReflectionTestUtils.setField(payment1, "paymentUrl", paymentUrl1);

        Person person2 = new Person("Gilbert", "Jey", "905.424.800-93");
        Payment payment2 = new Payment(person2, new BigDecimal("35.22"), bill);
        ReflectionTestUtils.setField(payment2, "paymentUrl", paymentUrl2);
        payment2.setStatus(PaymentStatus.PENDING);
        List<Payment> billPayments = new ArrayList<>(Arrays.asList(payment1, payment2));
        return billPayments;
    }
}
