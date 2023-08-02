package de.friedrichs.kanzeige.shell;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@ShellComponent
public class LoginLogout {

    public static final AtomicBoolean isLogin = new AtomicBoolean(false);

    @Autowired
    private WebDriver driver;

    @ShellMethod(value = "Login")
    public void login() {
        log.info("Starte Login");
        driver.get("https://www.kleinanzeigen.de/m-einloggen.html");
        try {
            WebElement button = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(By.id("gdpr-banner-accept")));
            button.click();
        } catch (Exception e) {
            log.info("'Bist du einverstanden?' Fenster wurde nicht gezeigt.");
        }
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.findElement(By.id("login-email"))).sendKeys("");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.findElement(By.id("login-password"))).sendKeys("");
        log.info("Bitte Login jetzt abschließen.");
        driver.manage().window().maximize();
        try {
            new WebDriverWait(driver, Duration.ofMinutes(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.id("my-manageads-adlist")));
        } catch (Exception e) {
            log.warn("Login war nicht erfolgreich", e);
            return;
        }
        isLogin.set(true);
        log.info("Login war erfolgreich - Was nun?");
        driver.manage().window().minimize();
    }

    @ShellMethod(value = "Logout")
    public void logout() {
        driver.get("https://www.kleinanzeigen.de/m-abmelden.html");
        isLogin.set(false);
        log.info("Logout wurde durchgeführt");
    }

    public Availability loginAvailability() {
        return (!isLogin.get())
                ? Availability.available()
                : Availability.unavailable("Login ist bereits erfolgt.");
    }

    public Availability logoutAvailability() {
        return (isLogin.get())
                ? Availability.available()
                : Availability.unavailable("Bitte zuerst einloggen.");
    }
}
