package br.com.murilofarias.sharedpay.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class BillTest {

    private static Person billOwner;

    @BeforeEach
    public void init(){
        billOwner = new Person("Alan", "Santos", "329.616.180-53");

    }

    @ParameterizedTest
    @MethodSource("provideSpendings")
    @DisplayName("Testing if bill constructor sets properties passed as arguments")
    void constructor_shouldSetsBillProperties(SpendingBundleTest spendingBundleTest) {

        //Act
        Bill bill =  new Bill(
                spendingBundleTest.getAdditionals(),
                spendingBundleTest.getDiscounts(),
                true,
                spendingBundleTest.getIndividualSpendings(),
                billOwner);

        //Assert
        assertEquals(spendingBundleTest.getAdditionals(), bill.getAdditionals());
        assertEquals(spendingBundleTest.getDiscounts(), bill.getDiscounts());
        for(int i = 0; i< bill.getIndividualSpendings().size(); i++)
            assertEquals(spendingBundleTest.individualSpendings.get(i).getValue(), bill.getIndividualSpendings().get(i).getValue());
    }

    @ParameterizedTest
    @MethodSource("provideSpendings")
    @DisplayName("Testing if bill constructor leaves payments null and id null")
    void constructor_shouldSetsBillPropertiesAndSetPaymentsNullAndIdNull(SpendingBundleTest spendingBundleTest) {

        //Act
        Bill bill =  new Bill(
                spendingBundleTest.getAdditionals(),
                spendingBundleTest.getDiscounts(),
                true,
                spendingBundleTest.getIndividualSpendings(),
                billOwner);


        //Assert
        assertThat(bill.getId()).isNull();
        assertThat(bill.getPayments()).isNull();
    }


    @ParameterizedTest
    @MethodSource("provideBillSpendingsForGeneratePaymentsWithoutWaiterService")
    @DisplayName("Testing generation of payments in a bill without waiter service")
    void generatePayments_whenThereIsNoWaiterService(
            BigDecimal additionals,
            BigDecimal discounts,
            List<IndividualSpending> individualSpendings,
            List<BigDecimal> expectedPayments) {

        //Arrange
        boolean hasWaiterService = false;


        Bill bill =  new Bill(
                additionals,
                discounts,
                hasWaiterService,
                individualSpendings,
                billOwner);

        //Act
        bill.generatePayments();

        //Assert
        for(int i = 0; i< expectedPayments.size(); i++)
            assertEquals(expectedPayments.get(i), bill.getPayments().get(i).getValue());

    }

    private static Stream<Arguments> provideBillSpendingsForGeneratePaymentsWithoutWaiterService() {
        List<SpendingBundleTest> spendingBundleTests = provideSpendings();

        List<BigDecimal> paymentsValueExpected1 =
                Arrays.asList( new BigDecimal("26.30"), new BigDecimal("33.39"));

        List<BigDecimal> paymentsValueExpected2 =
                Arrays.asList( new BigDecimal("6.08"));

        return Stream.of(
                Arguments.of(
                        spendingBundleTests.get(0).additionals,
                        spendingBundleTests.get(0).discounts,
                        spendingBundleTests.get(0).individualSpendings,
                        paymentsValueExpected1),
                Arguments.of(
                        spendingBundleTests.get(1).additionals,
                        spendingBundleTests.get(1).discounts,
                        spendingBundleTests.get(1).individualSpendings,
                        paymentsValueExpected2)

        );
    }

    @ParameterizedTest
    @MethodSource("provideBillSpendingsForGeneratePaymentsWithWaiterService")
    @DisplayName("Testing generation of payments in a bill with waiter service")
    void generatePayments_whenThereIsWaiterService(
            BigDecimal additionals,
            BigDecimal discounts,
            List<IndividualSpending> individualSpendings,
            List<BigDecimal> expectedPayments) {
        //Arrange
        boolean hasWaiterService = true;


        Bill bill =  new Bill(
                additionals,
                discounts,
                hasWaiterService,
                individualSpendings,
                billOwner);

        //Act
        bill.generatePayments();

        //Assert
        for(int i = 0; i< expectedPayments.size(); i++)
            assertEquals(expectedPayments.get(i), bill.getPayments().get(i).getValue());

    }


    private static Stream<Arguments> provideBillSpendingsForGeneratePaymentsWithWaiterService() {
        List<SpendingBundleTest> spendingBundleTests = provideSpendings();

        List<BigDecimal> paymentsValueExpected1 =
                Arrays.asList(new BigDecimal("28.93"), new BigDecimal("36.72"));

        List<BigDecimal> paymentsValueExpected2 =
                Arrays.asList( new BigDecimal("6.69"));

        return Stream.of(
                Arguments.of(
                        spendingBundleTests.get(0).additionals,
                        spendingBundleTests.get(0).discounts,
                        spendingBundleTests.get(0).individualSpendings,
                        paymentsValueExpected1),
                Arguments.of(
                        spendingBundleTests.get(1).additionals,
                        spendingBundleTests.get(1).discounts,
                        spendingBundleTests.get(1).individualSpendings,
                        paymentsValueExpected2)

        );
    }

    private static  List<SpendingBundleTest> provideSpendings(){
        List<SpendingBundleTest> spendingBundleTests = new ArrayList<>();

        Person person2 = new Person("Brand", "Kay", "601.624.520-80");
        Person person3 = new Person("Gilbert", "Jey", "905.424.800-93");

        //First data
        IndividualSpending individualSpending1_1 = new IndividualSpending(new BigDecimal("12.65"), billOwner );
        IndividualSpending individualSpending1_2 = new IndividualSpending(new BigDecimal("25.15"), person2 );
        IndividualSpending individualSpending1_3 = new IndividualSpending(new BigDecimal("31.92"), person3 );

        List<IndividualSpending> individualSpendings1 =
                Arrays.asList(individualSpending1_1, individualSpending1_2, individualSpending1_3);

        BigDecimal additionals1 = new BigDecimal("8.20");
        BigDecimal discounts1 = new BigDecimal("5.00");

        spendingBundleTests.add(new SpendingBundleTest(individualSpendings1, additionals1, discounts1));

        //Second Data
        IndividualSpending individualSpending2_1 = new IndividualSpending(new BigDecimal("42.00"), billOwner );
        IndividualSpending individualSpending2_2 = new IndividualSpending(new BigDecimal("8.00"), person2 );

        List<IndividualSpending> individualSpendings2 =
                Arrays.asList(individualSpending2_1, individualSpending2_2);

        BigDecimal additionals2 = new BigDecimal("8.00");
        BigDecimal discounts2 = new BigDecimal("20.00");

        spendingBundleTests.add(new SpendingBundleTest(individualSpendings2, additionals2, discounts2));
        return spendingBundleTests;

    }

    @Getter
    @Setter
    static
    class SpendingBundleTest{
        private List<IndividualSpending> individualSpendings;
        private BigDecimal additionals;
        private BigDecimal discounts;
        private List<BigDecimal> paymentValuesExpected;

        public SpendingBundleTest(
                List<IndividualSpending> individualSpendings, BigDecimal additionals, BigDecimal discounts){
            this.individualSpendings = individualSpendings;
            this.additionals = additionals;
            this.discounts = discounts;
        }
    }


}