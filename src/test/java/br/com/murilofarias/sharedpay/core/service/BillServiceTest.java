package br.com.murilofarias.sharedpay.core.service;

import br.com.murilofarias.sharedpay.core.error.ResourceNotFoundException;
import br.com.murilofarias.sharedpay.core.model.*;
import br.com.murilofarias.sharedpay.core.plug.BillRepository;
import br.com.murilofarias.sharedpay.core.plug.PaymentRepository;
import br.com.murilofarias.sharedpay.core.plug.PaymentService;
import br.com.murilofarias.sharedpay.core.plug.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class BillServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    BillRepository billRepository;

    @Mock
    PaymentService paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @InjectMocks
    BillService billService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Testing if registerBill when user exists calls methods chargeByBill," +
            " generatePayments and repository method save")
    void registerBill_whenUserExists_shouldChargeUserAndGeneratePaymentsAndSaveTheBill() {
        //Arrange
        Bill bill = spy(Bill.class);
        Long IdToBeSetByRepository = 325L;
        doNothing().when(bill).generatePayments();
        doAnswer(invocation -> {
            Bill billGiven = invocation.getArgument(0);
            ReflectionTestUtils.setField(billGiven, "id", IdToBeSetByRepository);

            return billGiven;
        }).when(billRepository).save(bill);

        Person billOwner = new Person("Alan", "Santos", "329.616.180-53");
        when(bill.getOwner()).thenReturn(billOwner);
        User user = spy(User.class);
        when(user.getPerson()).thenReturn(billOwner);
        when(userRepository.findByPersonCpf(billOwner.getCpf())).thenReturn(Optional.of(user));
        doNothing().when(user).chargeByBill(bill);

        //Act
        Bill billResult = billService.registerBill(bill);

        //Assert
        verify(user, times(1)).chargeByBill(bill);
        verify(bill, times(1)).generatePayments();
        verify(billRepository, times(1)).save(bill);
        assertEquals(IdToBeSetByRepository, billResult.getId());

    }

    @Test
    @DisplayName("Testing if getBillPayments when repository finds bill returns bill payments")
    void getBillPayments_whenRepositoryFindsBill_shouldReturnBillPayments() {
        //Arrange
        Long billId = 325L;
        Bill bill = spy(Bill.class);
        when(billRepository.findById(billId)).thenReturn(Optional.ofNullable(bill));
        Person person1 = new Person("Alan", "Santos", "329.616.180-53");
        Payment payment1 = new Payment(person1, new BigDecimal("20.22"), bill);
        Person person2 = new Person("Brand", "Kay", "601.624.520-80");
        Payment payment2 = new Payment(person2, new BigDecimal("35.22"), bill);
        List<Payment> billPayments = Arrays.asList(payment1, payment2);
        when(bill.getPayments()).thenReturn(billPayments);

        //Act
        List<Payment> paymentsReturned = billService.getBillPayments(billId);

        //Assert
        verify(bill, times(1)).getPayments();
        for(int i = 0; i< paymentsReturned.size(); i++)
            assertEquals(billPayments.get(i).getValue(), paymentsReturned.get(i).getValue());

        assertEquals(billPayments.size(), paymentsReturned.size());
        verify(billRepository, times(1)).findById(billId);
    }

    @Test
    @DisplayName("Testing if getBillPayments when repository doesn\'t find bill throws ResourceNotFoundException " )
    void getBillPayments_whenRepositoryDoesNotFindBill_shouldThrowResourceNotFoundException() {
        //Arrange
        Long billId = 325L;
        when(billRepository.findById(billId)).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> billService.getBillPayments(billId));

        //Assert
        verify(billRepository, times(1)).findById(billId);

    }

    @Test
    void requestPayments_whenBillIsFoundByIdAndPaymentServiceIsSuccessfull_ShouldReturnListOfPaymentsNonFulfilled() {
        //Arrange
        Long billId = 4L;
        Bill bill = mock(Bill.class);
        List<Payment> payments = providePayments(bill);
        when(bill.getPayments()).thenReturn(payments);
        when(billRepository.findById(billId)).thenReturn(Optional.of(bill));

        doAnswer(invocation -> {
            List<Payment> paymentsGiven = invocation.getArgument(0);
            List<String> paymentsUrls = new ArrayList<>();
            for(int i = 0; i< paymentsGiven.size(); i++) {
                paymentsUrls.add("https://app.picpay.com/checkout/NWZkOTA" + i);
            }
            return paymentsUrls;
        }).when(paymentService).requestPayments(any(List.class));

        //Act
        List<Payment> paymentsReturned = billService.requestPayments(billId);

        //Assert
        assertEquals(1, paymentsReturned.size());
        assertNotEquals(PaymentStatus.FULFILLED, paymentsReturned.get(0).getStatus());

    }

    private List<Payment> providePayments(Bill bill){
        Person person1 = new Person("Alan", "Santos", "329.616.180-53");
        Payment payment1 = new Payment(person1, new BigDecimal("20.22"), bill);
        ReflectionTestUtils.setField(payment1, "status", PaymentStatus.FULFILLED);

        Person person2 = new Person("Brand", "Kay", "601.624.520-80");
        Payment payment2 = new Payment(person2, new BigDecimal("35.22"), bill);
        List<Payment> billPayments = new ArrayList<>(Arrays.asList(payment1, payment2));
        return billPayments;
    }
}