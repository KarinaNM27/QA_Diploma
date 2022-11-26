package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.CreditPage;
import page.MainPage;



import static com.codeborne.selenide.Selenide.open;


public class CreditTest {
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

    public final CreditPage creditPage = new CreditPage();

    public void creditApprovedStatus() {
        String statusExpected = "APPROVED";
        String statusActual = DataHelper.getCreditStatus();
        Assertions.assertEquals(statusExpected, statusActual);
    }

    public void creditDeclinedStatus() {
        String statusExpected = "DECLINED";
        String statusActual = DataHelper.getCreditStatus();
        Assertions.assertEquals(statusExpected, statusActual);
    }

    public void creditAcceptId() {
        long idExpected = 1;
        long idActual = DataHelper.getCreditId();
        Assertions.assertEquals(idExpected, idActual);
    }

    public void creditRejectedId() {
        long idExpected = 0;
        long idActual = DataHelper.getCreditId();
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
    void shouldBuyCreditOnValidCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectCreditForm();


        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.acceptPay();
        creditApprovedStatus();
        creditAcceptId();
        orderAcceptId();
    }

    //2.
    @Test
    void shouldNotBuyCreditOnDeclinedCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123";

        var cardInfo = DataHelper.setInvalidCard(month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);

        creditPage.rejectedPay();
        creditDeclinedStatus();
        creditRejectedId();
        orderRejectedId();

    }

    //3.
    @Test
    void shouldNotBuyCreditOnCardLess16Numbers() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123",
                shortCardNumber = "4444 4444 4444 444";

        var cardInfo = DataHelper.setCard(shortCardNumber, month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.shouldVerifyErrorOfField();
        creditRejectedId();
        orderRejectedId();

    }

    //4.
    @Test
    void shouldNotBuyCreditOnZeroCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123",
                shortCardNumber = "0000 0000 0000 0000";

        var cardInfo = DataHelper.setCard(shortCardNumber, month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.rejectedPay();
        creditRejectedId();
        orderRejectedId();


    }
    //5.

    @Test
    void shouldNotBuyCreditOnEmptyNumberOfCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123",
                emptyCardNumber = "";

        var cardInfo = DataHelper.setCard(emptyCardNumber, month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.shouldVerifyErrorOfField();
        creditRejectedId();
        orderRejectedId();


    }

    //6.

    @Test
    void shouldNotBuyCreditOnNotRegisteredCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123",
                notRegisteredCardNumber = "4444 4444 4444 4443";

        var cardInfo = DataHelper.setCard(notRegisteredCardNumber, month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.rejectedPay();

        creditRejectedId();
        orderRejectedId();


    }

    //7.
    @Test
    void shouldNotBuyCreditWithZeroMonth() {
        String date = DataHelper.generateYear(2);
        String month = "00",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }
    //8.

    @Test
    void shouldNotBuyCreditWith13Month() {
        String date = DataHelper.generateYear(2);
        String month = "13",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.shouldVerifyIncorrectExpiryDate();

        creditRejectedId();
        orderRejectedId();


    }

    //9.
    @Test
    void shouldNotBuyCreditWithEmptyMonth() {
        String date = DataHelper.generateYear(2);
        String month = "",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }

    //10.
    @Test
    void shouldNotBuyCreditWith1NumberOfMonth() {
        String date = DataHelper.generateYear(2);
        String month = "1",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }

    //11.
    @Test
    void shouldNotBuyCreditWithLastYear() {
        String date = DataHelper.generateYear(-1);
        String month = "10",
                year = date,
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.shouldVerifyCardDateExpired();

        creditRejectedId();
        orderRejectedId();


    }

    //12.
    @Test
    void shouldNotBuyCreditWithEmptyYear() {

        String month = "10",
                year = "",
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }

    //13.
    @Test
    void shouldNotBuyCreditWithZeroYear() {

        String month = "10",
                year = "00",
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.shouldVerifyCardDateExpired();

        creditRejectedId();
        orderRejectedId();


    }

    //14.
    @Test
    void shouldNotBuyCreditWith1Year() {

        String month = "10",
                year = "1",
                owner = DataHelper.generateOwner("En"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }

    //15.
    @Test
    void shouldNotBuyCreditWithCyrillicNameOfOwner() {

        String month = "10",
                year = "24",
                owner = DataHelper.generateOwner("Ru"),
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }

    //16.
    @Test
    void shouldNotBuyCreditWithSymbolNameOfOwner() {

        String month = "10",
                year = "24",
                owner = "&!%$",
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }

    //17.
    @Test
    void shouldNotBuyCreditWithNumberNameOfOwner() {

        String month = "10",
                year = "24",
                owner = "12345",
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }

    //18.
    @Test
    void shouldNotBuyCreditWithEmptyNameOfOwner() {

        String month = "10",
                year = "24",
                owner = "",
                cvv = "123";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.shouldVerifyRequiredField();
        creditRejectedId();
        orderRejectedId();

    }

    //19.
    @Test
    void shouldNotBuyCreditWith2NumbersOfCVV() {

        String month = "10",
                year = "24",
                owner = DataHelper.generateOwner("En"),
                cvv = "12";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }

    //20.
    @Test
    void shouldNotBuyCreditWithZeroCVV() {

        String month = "10",
                year = "24",
                owner = DataHelper.generateOwner("En"),
                cvv = "000";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }

    //21.
    @Test
    void shouldNotBuyCreditWithEmptyCVV() {

        String month = "10",
                year = "24",
                owner = DataHelper.generateOwner("En"),
                cvv = "";


        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        MainPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(cardInfo);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }
}
