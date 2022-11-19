package page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class PurchasePage {
    final private static SelenideElement buttonBuy = $(byText("Купить"));

    final private static SelenideElement buttonCredit = $(byText("Купить в кредит"));


    public static PaymentPage selectBuyForm() {
        buttonBuy.click();
        PaymentPage.buyForm();
        return new PaymentPage();
    }

    public static PaymentPage selectCreditForm() {
        buttonCredit.click();
        PaymentPage.creditForm();
        return new PaymentPage();
    }
}