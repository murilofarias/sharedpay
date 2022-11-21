package br.com.murilofarias.sharedpay.core.model;

import br.com.murilofarias.sharedpay.core.error.DomainException;
import br.com.murilofarias.sharedpay.core.error.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PaymentTest {

    private Payment paymentTest;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        Bill bill = mock(Bill.class);
        Person person = new Person("Alan", "Santos", "329.616.180-53");
        paymentTest = new Payment(person, new BigDecimal("20.22"), bill);
    }

    @Test
    @DisplayName("Testing if constructor sets payment status to NON_REQUESTED")
    void constructor_shouldSetStatusToNonRequested() {

        //Assert
        assertEquals(PaymentStatus.NON_REQUESTED, paymentTest.getStatus());
    }

    @Test
    @DisplayName("Testing updatePaymentUrl when paymentUrl is null and payment status isn't fulfilled")
    void updatePaymentUrl_whenPaymentUrlIsNullAndStatusIsntFulfilled_ShouldThrowDomainException() {
        //Arrange
        String paymentUrl = null;


        //Act and Assert
        assertThrows(DomainException.class, () -> paymentTest.updatePaymentUrl(paymentUrl));
    }

    @Test
    @DisplayName("Testing updatePaymentUrl when paymentUrl is empty and payment status isn't fulfilled")
    void updatePaymentUrl_whenPaymentUrlIsEmptyAndStatusIsntFulfilled_ShouldThrowDomainException() {
        //Arrange
        String paymentUrl = "";


        //Act and Assert
        assertThrows(DomainException.class, () -> paymentTest.updatePaymentUrl(paymentUrl));
    }

    @Test
    @DisplayName("Testing updatePaymentUrl when paymentUrl is whitespaces and payment status isn't fulfilled")
    void updatePaymentUrl_whenPaymentUrlHasJustWhitespacesAndStatusIsntFulfilled_ShouldThrowDomainException() {
        //Arrange
        String paymentUrl = "    ";


        //Act and Assert
        assertThrows(DomainException.class, () -> paymentTest.updatePaymentUrl(paymentUrl));
    }

    @Test
    @DisplayName("Testing updatePaymentUrl when paymentUrl isn't blank and payment status is fulfilled")
    void updatePaymentUrl_whenPaymentUrlIsntBlankAndPaymentStatusIsFulfilled_ShouldThrowDomainException() {
        //Arrange
        String paymentUrl = "www.alaksslk.com/aks";
        ReflectionTestUtils.setField(paymentTest, "status", PaymentStatus.FULFILLED);


        //Act and Assert
        assertThrows(DomainException.class, () -> paymentTest.updatePaymentUrl(paymentUrl));
    }

    @Test
    @DisplayName("Testing updatePaymentUrl when paymentUrl isn't blank and payment status is NON_REQUESTED")
    void updatePaymentUrl_whenPaymentUrlIsntBlankAndPaymentStatusIsFulfilled_ShouldUpdatePaymentUrlAndChangePaymentStatusToPending() {
        //Arrange
        String paymentUrl = "www.alaksslk.com/aks";
        ReflectionTestUtils.setField(paymentTest, "status", PaymentStatus.NON_REQUESTED);


        //Act
        paymentTest.updatePaymentUrl(paymentUrl);

        //Arrange
        assertEquals(PaymentStatus.PENDING, paymentTest.getStatus());
        assertEquals(paymentUrl, paymentTest.getPaymentUrl());
    }

    @Test
    @DisplayName("Testing updatePaymentUrl when paymentUrl isn't blank and payment status is PENDING")
    void updatePaymentUrl_whenPaymentUrlIsntBlankAndPaymentStatusIsFulfilled_ShouldUpdatePaymentUrlAndMaintainStatusPending() {
        //Arrange
        String paymentUrl = "www.alaksslk.com/aks";
        ReflectionTestUtils.setField(paymentTest, "status", PaymentStatus.PENDING);


        //Act
        paymentTest.updatePaymentUrl(paymentUrl);

        //Assert
        assertEquals(PaymentStatus.PENDING, paymentTest.getStatus());
        assertEquals(paymentUrl, paymentTest.getPaymentUrl());
    }


}