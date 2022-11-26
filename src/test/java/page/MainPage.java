package page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {

    final private static SelenideElement heading1 = $(byText("Путешествие дня"));
    final private static SelenideElement buttonBuy = $(byText("Купить"));

    final private static SelenideElement buttonCredit = $(byText("Купить в кредит"));


    public static void selectBuyForm() {
        buttonBuy.click();

    }

    public static void selectCreditForm() {
        buttonCredit.click();

    }
}