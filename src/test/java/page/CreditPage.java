package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.byText;

public class CreditPage {
    final private static SelenideElement headingBuyCredit = $(byText("Кредит по данным карты"));
    final private SelenideElement notificationAcceptTitle = $(".notification_status_ok");
    final private SelenideElement notificationAcceptContent = $(".notification_status_ok .notification__content");
    final private SelenideElement notificationErrorTitle = $(".notification_status_error");
    final private SelenideElement notificationErrorContent = $(".notification_status_error .notification__content");
    final private static SelenideElement number = $("input[placeholder='0000 0000 0000 0000']");
    final private static SelenideElement month = $("input[placeholder='08']");
    ;
    final private static SelenideElement year = $("input[placeholder='22']");
    final private static SelenideElement owner = $x("//*[.='Владелец'] //input");
    final private static SelenideElement cvv = $("[placeholder='999']");
    final private static SelenideElement incorrectFormat = $(byText("Неверный формат"));
    final private static SelenideElement incorrectExpiryDate = $(byText("Неверно указан срок действия карты"));
    final private static SelenideElement cardDateExpired = $(byText("Истёк срок действия карты"));
    final private static SelenideElement requiredField = $(byText("Поле обязательно для заполнения"));
    final private static SelenideElement buttonContinue = $(".form-field button");


    public static void creditForm() {
        headingBuyCredit.shouldBe(Condition.visible);
    }


    public void acceptPay() {
        notificationAcceptTitle.shouldBe(Condition.text("Успешно"), Duration.ofSeconds(10)).shouldBe(Condition.visible);
        notificationAcceptContent.shouldBe(Condition.text("Операция одобрена Банком."), Duration.ofSeconds(10)).shouldBe(Condition.visible);
    }

    public void rejectedPay() {
        notificationErrorTitle.shouldBe(Condition.text("Ошибка"), Duration.ofSeconds(10)).shouldBe(Condition.visible);
        notificationErrorContent.shouldBe(Condition.text("Ошибка! Банк отказал в проведении операции."), Duration.ofSeconds(10)).shouldBe(Condition.visible);
    }


    public final void completeCardData(DataHelper.CardInfo info) {
        number.setValue(info.getNumber());
        month.setValue(info.getMonth());
        year.setValue(info.getYear());
        owner.setValue(info.getOwner());
        cvv.setValue(info.getCvv());
        buttonContinue.click();
    }

    public final void cleanFields() {
        number.sendKeys(Keys.LEFT_CONTROL + "A");
        number.sendKeys(Keys.DELETE);
        month.sendKeys(Keys.LEFT_CONTROL + "A");
        month.sendKeys(Keys.DELETE);
        year.sendKeys(Keys.LEFT_CONTROL + "A");
        year.sendKeys(Keys.DELETE);
        owner.sendKeys(Keys.LEFT_CONTROL + "A");
        owner.sendKeys(Keys.DELETE);
        cvv.sendKeys(Keys.LEFT_CONTROL + "A");
        cvv.sendKeys(Keys.DELETE);
    }

    public void shouldVerifyErrorOfField() {
        incorrectFormat.shouldBe(Condition.visible);
    }

    public void shouldVerifyIncorrectExpiryDate() {
        incorrectExpiryDate.shouldBe(Condition.visible);
    }

    public void shouldVerifyCardDateExpired() {
        cardDateExpired.shouldBe(Condition.visible);
    }

    public void shouldVerifyRequiredField() {
        requiredField.shouldBe(Condition.visible);
    }


}