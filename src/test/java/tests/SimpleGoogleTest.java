package tests;

import com.codeborne.selenide.Configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;

import static io.qameta.allure.Allure.step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.closeWebDriver;
import static com.codeborne.selenide.logevents.SelenideLogger.addListener;
import static helpers.AttachmentsHelper.*;

public class SimpleGoogleTest {

    final static String REMOTE_BROWSER_URL = "https://user1:1234@" + System.getProperty("remote.browser.url") + ":4444/wd/hub/";

    final static String SEARCH_QUERY = "Selenide";
    final static String SEARCH_RESULT = "selenide.org";

    @BeforeAll
    static void setup() {

        addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);

        Configuration.browserCapabilities = capabilities;
        Configuration.remote = REMOTE_BROWSER_URL;
        Configuration.startMaximized = true;

    }

    @AfterEach
    @Step("Attachments")
    public void afterEach(){
        attachScreenshot("Last screenshot");
        attachPageSource();
        attachAsText("Browser console logs", getConsoleLogs());
        attachVideo();

        closeWebDriver();

    }

    @Test
    @DisplayName("Успешный поисковый запрос в Google")
    void successfulSelenideSearchTest() {

        step("Открываем google.com",() -> {
            open("https://www.google.com");
        });

        step("Вводим в поисковую строку " + SEARCH_QUERY,() -> {
            $(byName("q")).setValue(SEARCH_QUERY).pressEnter();
        });

        step("Результат поиска должен содержать " + SEARCH_RESULT,() -> {
            $("html").shouldHave(text(SEARCH_RESULT));
        });

    }

    @Test
    @DisplayName("Тест с ошибкой")
    void unsuccessfulSelenideSearchTest() {

        step("Открываем google.com",() -> {
            open("https://www.google.com");
        });

        step("Вводим в поисковую строку " + SEARCH_QUERY,() -> {
            $(byName("q")).setValue(SEARCH_QUERY).pressEnter();
        });

        step("Результат поиска должен содержать " + SEARCH_RESULT,() -> {
            $("html").shouldNotHave(text(SEARCH_RESULT));
        });

    }

}