package tests;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.logevents.SelenideLogger.addListener;
import static io.qameta.allure.Allure.step;
import static helpers.AttachmentsHelper.*;
import static com.codeborne.selenide.WebDriverRunner.closeWebDriver;

public class SimpleGoogleTest {


    @BeforeAll
    static void setup() {
        addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
        Configuration.startMaximized = true;
    }

    @AfterEach
    @Step("Attachments")
    public void afterEach(){
        attachScreenshot("Last screenshot");
        attachPageSource();
        attachAsText("Browser console logs", getConsoleLogs());

        closeWebDriver();
    }

    @Test
    @DisplayName("Успешный поисковый запрос в Google")
    void successfulSelenideSearchTest() {

        String searchQuery = "Selenide";
        String searchResults = "selenide.org";

        step("Открываем google.com",() -> {
            open("https://www.google.com");
        });

        step("Вводим в поисковую строку " + searchQuery,() -> {
            $(byName("q")).setValue(searchQuery).pressEnter();
        });

        step("Результат поиска должен содержать " + searchResults,() -> {
            $("html").shouldHave(text(searchResults));
        });
    }

    @Test
    @DisplayName("Тест с ошибкой")
    void unsuccessfulSelenideSearchTest() {

        String searchQuery = "Selenide";
        String searchResults = "selenide.org";

        step("Открываем google.com",() -> {
            open("https://www.google.com");
        });

        step("Вводим в поисковую строку " + searchQuery,() -> {
            $(byName("q")).setValue(searchQuery).pressEnter();
        });

        step("Результат поиска должен содержать " + searchResults,() -> {
            $("html").shouldNotHave(text(searchResults));
        });
    }

}