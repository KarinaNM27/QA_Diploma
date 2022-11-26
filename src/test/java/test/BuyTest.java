package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.MainPage;
import page.PaymentPage;


import static com.codeborne.selenide.Selenide.open;


public class BuyTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void shouldStart() {
        DataHelper.clearSUTData();
        open("http://localhost:8080/");
    }

    @AfterEach
    void afterCase() {
        DataHelper.clearSUTData();
    }

    public final PaymentPage paymentPage = new PaymentPage();


    public void paymentApprovedStatus() {
        String statusExpected = "APPROVED";
        String statusActual = DataHelper.getPaymentStatus();
        Assertions.assertEquals(statusExpected, statusActual);
    }

    public void paymentDeclinedStatus() {
        String statusExpected = "DECLINED";
        String statusActual = DataHelper.getPaymentStatus();
        Assertions.assertEquals(statusExpected, statusActual);
    }

    public void paymentAcceptId() {
        long idExpected = 1;
        long idActual = DataHelper.getPaymentId();
        Assertions.assertEquals(idExpected, idActual);
    }

    public void paymentRejectedId() {
        long idExpected = 0;
        long idActual = DataHelper.getPaymentId();
        Assertions.assertEquals(idExpected, idActual);
    }

    public void orderAcceptId() {
        long idExpected = 1;
        long idActual = DataHelper.getOrderId();
        Assertions.assertEquals(idExpected, idActual);
    }

    public void orderRejectedId() {
        long idExpected = 0;
        long idActual = DataHelper.getOrderId();
        Assertions.assertEquals(idExpected, idActual);
    }

    //1.
    @Test
    void shouldBuyOnValidCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectBuyForm();


        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.acceptPay();
        paymentApprovedStatus();
        paymentAcceptId();
        orderAcceptId();
    }

    //2.
    @Test
    void shouldNotBuyOnDeclinedCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123";

        var cardInfo = DataHelper.setInvalidCard(month, year, owner, cvv);
        MainPage.selectBuyForm();


        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);

        paymentPage.rejectedPay();
        paymentDeclinedStatus();
        paymentRejectedId();
        orderRejectedId();

    }

    //3.
    @Test
    void shouldNotBuyOnCardLess16Numbers() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123",
                shortCardNumber = "4444 4444 4444 444";

        var cardInfo = DataHelper.setCard(shortCardNumber, month, year, owner, cvv);
        MainPage.selectBuyForm();


        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();
        paymentRejectedId();
        orderRejectedId();
    }

    //4.
    @Test
    void shouldNotBuyOnZeroCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123",
                shortCardNumber = "0000 0000 0000 0000";

        var cardInfo = DataHelper.setCard(shortCardNumber, month, year, owner, cvv);
        MainPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.rejectedPay();
        paymentRejectedId();
        orderRejectedId();


    }
    //5.

    @Test
    void shouldNotBuyOnEmptyNumberOfCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123",
                emptyCardNumber = "";

        var cardInfo = DataHelper.setCard(emptyCardNumber, month, year, owner, cvv);
        MainPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();
        paymentRejectedId();
        orderRejectedId();


    }

    //6.

    @Test
    void shouldNotBuyOnNotRegisteredCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123",
                notRegisteredCardNumber = "4444 4444 4444 4443";

        var cardInfo = DataHelper.setCard(notRegisteredCardNumber, month, year, owner, cvv);
        MainPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.rejectedPay();

        paymentRejectedId();
        orderRejectedId();


    }

    //7.
    @Test
    void shouldNotBuyWithZeroMonth() {
        String date = DataHelper.generateYear(2);
        String month = "00",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }
    //8.

    @Test
    void shouldNotBuyWith13Month() {
        String date = DataHelper.generateYear(2);
        String month = "13",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyIncorrectExpiryDate();

        paymentRejectedId();
        orderRejectedId();


    }

    //9.
    @Test
    void shouldNotBuyWithEmptyMonth() {
        String date = DataHelper.generateYear(2);
        String month = "",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }

    //10.
    @Test
    void shouldNotBuyWith1NumberOfMonth() {
        String date = DataHelper.generateYear(2);
        String month = "1",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }

    //11.
    @Test
    void shouldNotBuyWithLastYear() {
        String date = DataHelper.generateYear(-1);
        String month = "10",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyCardDateExpired();

        paymentRejectedId();
        orderRejectedId();


    }

    //12.
    @Test
    void shouldNotBuyWithEmptyYear() {

        String month = "10",
                year = "",
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }

    //13.
    @Test
    void shouldNotBuyWithZeroYear() {

        String month = "10",
                year = "00",
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyCardDateExpired();

        paymentRejectedId();
        orderRejectedId();


    }

    //14.
    @Test
    void shouldNotBuyWith1Year() {

        String month = "10",
                year = "1",
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }

    //15.
    @Test
    void shouldNotBuyWithCyrillicNameOfOwner() {

        String month = "10",
                year = "24",
                owner = DataHelper.generateOwner("Ru"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }

    //16.
    @Test
    void shouldNotBuyWithSymbolNameOfOwner() {

        String month = "10",
                year = "24",
                owner = "&!%$",
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();

    }

    //17.
    @Test
    void shouldNotBuyWithNumberNameOfOwner() {

        String month = "10",
                year = "24",
                owner = "12345",
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectBuyForm();
        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }

    //18.
    @Test
    void shouldNotBuyWithEmptyNameOfOwner() {

        String month = "10",
                year = "24",
                owner = "",
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectBuyForm();
        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyRequiredField();
        paymentRejectedId();
        orderRejectedId();

    }

    //19.
    @Test
    void shouldNotBuyWith2NumbersOfCVV() {

        String month = "10",
                year = "24",
                owner = DataHelper.generateOwner("En"),
                cvv = "12";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }

    //20.
    @Test
    void shouldNotBuyWithZeroCVV() {

        String month = "10",
                year = "24",
                owner = DataHelper.generateOwner("En"),
                cvv = "000";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }

    //21.
    @Test
    void shouldNotBuyWithEmptyCVV() {

        String month = "10",
                year = "24",
                owner = DataHelper.generateOwner("En"),
                cvv = "";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }
}
