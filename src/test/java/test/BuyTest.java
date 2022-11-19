package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.PurchasePage;


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

    //1.
    @Test
    void shouldBuyOnValidCard() {
        String[] date = DataHelper.generateYear(2);
        String month = "10",
                year = date[0],
                owner = DataHelper.generateOwner("En"),
                cvv = "123";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.acceptPay();
        paymentPage.paymentApprovedStatus();
        paymentPage.paymentAcceptId();
        paymentPage.orderAcceptId();
    }
    //2.
    @Test
    void shouldNotBuyOnDeclinedCard() {
        String[] date = DataHelper.generateYear(2);
        String month = "10",
                year = date[0],
                owner = DataHelper.generateOwner("En"),
                cvv = "123";

        var cardInfo = DataHelper.setInvalidCard(month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);

        paymentPage.rejectedPay();
        paymentPage.paymentDeclinedStatus();
        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();

    }
    //3.
    @Test
    void shouldNotBuyOnCardLess16Numbers() {
        String[] date = DataHelper.generateYear(2);
        String month = "10",
                year = date[0],
                owner = DataHelper.generateOwner("En"),
                cvv = "123",
                shortCardNumber = "4444 4444 4444 444";

        var cardInfo = DataHelper.setCard(shortCardNumber, month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();
        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();
    }
    //4.
    @Test
    void shouldNotBuyOnZeroCard() {
        String[] date = DataHelper.generateYear(2);
        String month = "10",
                year = date[0],
                owner = DataHelper.generateOwner("En"),
                cvv = "123",
                shortCardNumber = "0000 0000 0000 0000";

        var cardInfo = DataHelper.setCard(shortCardNumber, month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.rejectedPay();
        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();


    }
    //5.

    @Test
    void shouldNotBuyOnEmptyNumberOfCard() {
        String[] date = DataHelper.generateYear(2);
        String month = "10",
                year = date[0],
                owner = DataHelper.generateOwner("En"),
                cvv = "123",
                emptyCardNumber = "";

        var cardInfo = DataHelper.setCard(emptyCardNumber, month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();
        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();


    }

    //6.

    @Test
    void shouldNotBuyOnNotRegisteredCard() {
        String[] date = DataHelper.generateYear(2);
        String month = "10",
                year = date[0],
                owner = DataHelper.generateOwner("En"),
                cvv = "123",
                notRegisteredCardNumber = "4444 4444 4444 4443";

        var cardInfo = DataHelper.setCard(notRegisteredCardNumber, month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.rejectedPay();

        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();


    }

    //7.
    @Test
    void shouldNotBuyWithZeroMonth() {
        String[] date = DataHelper.generateYear(2);
        String month = "00",
                year = date[0],
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();


    }
    //8.

    @Test
    void shouldNotBuyWith13Month() {
        String[] date = DataHelper.generateYear(2);
        String month = "13",
                year = date[0],
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyIncorrectExpiryDate();

        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();


    }

    //9.
    @Test
    void shouldNotBuyWithEmptyMonth() {
        String[] date = DataHelper.generateYear(2);
        String month = "",
                year = date[0],
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();


    }
    //10.
    @Test
    void shouldNotBuyWith1NumberOfMonth() {
        String[] date = DataHelper.generateYear(2);
        String month = "1",
                year = date[0],
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();


    }

    //11.
    @Test
    void shouldNotBuyWithLastYear() {
        String[] date = DataHelper.generateYear(-1);
        String month = "10",
                year = date[0],
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyCardDateExpired();

        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();


    }

    //12.
    @Test
    void shouldNotBuyWithEmptyYear() {

        String month = "10",
                year = "",
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();


    }

    //13.
    @Test
    void shouldNotBuyWithZeroYear() {

        String month = "10",
                year = "00",
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyCardDateExpired();

        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();


    }

    //14.
    @Test
    void shouldNotBuyWith1Year() {

        String month = "10",
                year = "1",
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();


    }

    //15.
    @Test
    void shouldNotBuyWithCyrillicNameOfOwner() {

        String month = "10",
                year = "24",
                owner = DataHelper.generateOwner("Ru"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();


    }

    //16.
    @Test
    void shouldNotBuyWithSymbolNameOfOwner() {

        String month = "10",
                year = "24",
                owner = "&!%$",
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();

    }

    //17.
    @Test
    void shouldNotBuyWithNumberNameOfOwner() {

        String month = "10",
                year = "24",
                owner = "12345",
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();


    }

    //18.
    @Test
    void shouldNotBuyWithEmptyNameOfOwner() {

        String month = "10",
                year = "24",
                owner = "",
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyRequiredField();
        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();

    }

    //19.
    @Test
    void shouldNotBuyWith2NumbersOfCVV() {

        String month = "10",
                year = "24",
                owner = DataHelper.generateOwner("En"),
                cvv = "12";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();


    }

    //20.
    @Test
    void shouldNotBuyWithZeroCVV() {

        String month = "10",
                year = "24",
                owner = DataHelper.generateOwner("En"),
                cvv = "000";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();


    }

    //21.
    @Test
    void shouldNotBuyWithEmptyCVV() {

        String month = "10",
                year = "24",
                owner = DataHelper.generateOwner("En"),
                cvv = "";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = PurchasePage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentPage.paymentRejectedId();
        paymentPage.orderRejectedId();


    }
}
