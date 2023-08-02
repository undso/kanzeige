package de.friedrichs.kanzeige.shell;

import de.friedrichs.kanzeige.model.Anzeige;
import de.friedrichs.kanzeige.model.Bild;
import de.friedrichs.kanzeige.repo.AnzeigeRepository;
import de.friedrichs.kanzeige.repo.BildRepository;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ShellComponent
public class Ebay {

    @Autowired
    private LoginLogout loginLogout;
    @Autowired
    private WebDriver driver;
    @Autowired
    private AnzeigeRepository anzeigeRepository;
    @Autowired
    private BildRepository bildRepository;

    @ShellMethod(value = "Lies alle Anzeigen ein", key = "liesAlleAnzeigenEin")
    public void liesAlleAnzeigenEin() {
        if (!driver.getCurrentUrl().startsWith("https://www.kleinanzeigen.de/m-meine-anzeigen.html")) {
            driver.get("https://www.kleinanzeigen.de/m-meine-anzeigen.html");
        }
        WebElement adlist = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("my-manageads-adlist")));
        log.info("{}", adlist.getTagName());
        List<WebElement> adlistElements = adlist.findElements(By.xpath("li"));
        log.info("adlistElements: {}", adlistElements.size());
        List<Anzeige> anzeigen = new ArrayList<>();
        adlistElements.forEach(a -> {
            WebElement titelElement = a.findElement(By.xpath("article/section/section[2]/h2/div/a"));
            Anzeige anzeige = new Anzeige();
            anzeige.setTitel(titelElement.getText());
            anzeige.setUrl(titelElement.getAttribute("href"));
            WebElement preis = a.findElement(By.xpath("article/section/section[2]/ul/li[1]"));
            anzeige.setPreis(Integer.parseInt(preis.getText().split(" ")[0]));
            WebElement endetElement = a.findElement(By.xpath("article/section/section[2]/ul/li[2]/span"));
            anzeige.setAblaufdatum(LocalDate.parse(endetElement.getText(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            anzeigen.add(anzeige);
        });
        log.info("{} Anzeigen gefunden. Lese Details ein", anzeigen.size());

        anzeigen.forEach(anzeige -> {
            driver.get(anzeige.getUrl());
            WebElement beschreibung = new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.id("viewad-description-text")));
            anzeige.setBeschreibung(beschreibung.getText());
            anzeige.setVerhandlungsbasis(driver.findElement(By.id("viewad-price")).getText().contains("VB"));
            List<WebElement> bilder = driver.findElements(By.xpath("//*[@id=\"viewad-product\"]/div[1]/div"));
            bilder.forEach(bild -> {
                if (!bild.findElements(By.xpath("img")).isEmpty()) {
                    anzeige.getBilder().add(bildRepository.save(
                            new Bild(
                                    bild.findElement(By.xpath("img")).getAttribute("src")
                            )));
                }
            });
            anzeige.setVersand(!driver.findElements(By.xpath("//div[@id = 'viewad-main-info']/div/span[normalize-space(text()) = 'Versand möglich']")).isEmpty());
            List<WebElement> breadcrumps = driver.findElement(By.id("vap-brdcrmb")).findElements(By.xpath("a"));
            breadcrumps.forEach(breadcrump -> anzeige.getBreadcrumps().add(breadcrump.findElement(By.xpath("span")).getText()));
            List<WebElement> details = driver.findElements(By.xpath("//*[@id=\"viewad-details\"]/ul/li"));
            details.forEach(detail -> {
                String text = detail.getText();
                anzeige.getDetails().put(text.split("\\R")[0], text.split("\\R")[1]);
            });
            this.anzeigeRepository.save(anzeige);
        });
    }

    public Availability liesAlleAnzeigenEinAvailability() {
        return loginLogout.logoutAvailability();
    }
}
