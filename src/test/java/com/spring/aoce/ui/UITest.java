package com.spring.aoce.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UITest {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private final static String URL = "http://localhost:8081";

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        driver.manage().window().maximize();
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testUserRegistrationAndLogin() {
        // 1. Нажать на кнопку регистрации
        driver.get(URL + "/login");
        WebElement registrationLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Зарегистрироваться")));
        registrationLink.click();
        assertEquals("Регистрация", driver.getTitle(), "Проверка заголовка страницы регистрации");

        // 2. Заполнить форму регистрации и зарегистрироваться
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email"))).sendKeys("testuser@example.com");
        driver.findElement(By.name("fullName")).sendKeys("Иван Иванов Иванович");
        driver.findElement(By.name("phone")).sendKeys("+7(111)111-11-10");
        driver.findElement(By.name("birthday")).sendKeys("2000-01-01");
        driver.findElement(By.name("password")).sendKeys("password123");
        driver.findElement(By.name("passwordConfirm")).sendKeys("password123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // 3. Проверка перехода на страницу входа
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h2"), "Вход"));
        assertEquals("Вход", driver.findElement(By.tagName("h2")).getText());

        // 4. Заполнить форму входа и войти
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("testuser@example.com");
        driver.findElement(By.name("password")).sendKeys("password123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // 5. Проверка перехода на главную страницу
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
        assertEquals("Добро пожаловать на сайт учета вычислительной техники!",
                driver.findElement(By.tagName("h1")).getText());

        // 6. Перейти на вкладку "Техника"
        driver.get(URL + "/computer");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h2"), "Техника"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(), 'Создать заявку на добавление техники')]")));
        assertEquals("Техника", driver.findElement(By.tagName("h2")).getText());

        // 7. Нажать на "Выход"
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Выход")));
        logoutLink.click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h2"), "Вход"));
        assertEquals("Вход", driver.findElement(By.tagName("h2")).getText());

        // 8. Регистрация администратора
        registrationLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Зарегистрироваться")));
        registrationLink.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email"))).sendKeys("admin@example.com");
        driver.findElement(By.name("fullName")).sendKeys("Админ Админов Админович");
        driver.findElement(By.name("phone")).sendKeys("+7(111)111-22-22");
        driver.findElement(By.name("birthday")).sendKeys("1980-01-01");
        driver.findElement(By.name("password")).sendKeys("admin123");
        driver.findElement(By.name("passwordConfirm")).sendKeys("admin123");
        driver.findElement(By.name("code")).sendKeys("111");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h2"), "Вход"));

        // 9. Вход в аккаунт администратора
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("admin@example.com");
        driver.findElement(By.name("password")).sendKeys("admin123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // 10. Проверка главной страницы администратора
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
        assertEquals("Добро пожаловать на сайт учета вычислительной техники!",
                driver.findElement(By.tagName("h1")).getText());

        // 11. Перейти на вкладку "Техника" как администратор
        driver.get(URL + "/computer");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h2"), "Техника"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(), 'Добавить технику')]")));
        assertEquals("Техника", driver.findElement(By.tagName("h2")).getText());

        // 12. Нажать на "Выход"
        logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Выход")));
        logoutLink.click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h2"), "Вход"));
        assertEquals("Вход", driver.findElement(By.tagName("h2")).getText());
    }

    @Test
    public void testCreateEquipment() {
        // 1. Переход на вход
        driver.get(URL + "/login");

        // 2. Вход в аккаунт администратора
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h2"), "Вход"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("admin@admin.ru");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // 3. Переход на вкладку "Техника"
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));

        // 4. Переход на страницу создания техники
        driver.get(URL + "/computer");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h2"), "Техника"));
        WebElement createEquipmentButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(), 'Добавить технику')]")));
        createEquipmentButton.click();

        // 5. Заполнение формы для создания нового оборудования
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys("Компьютер 1");
        driver.findElement(By.id("serialNumber")).sendKeys("SN123456");
        driver.findElement(By.id("inventoryNumber")).sendKeys("IN123456");
        driver.findElement(By.id("manufacturer")).sendKeys("Manufacturer XYZ");
        driver.findElement(By.id("model")).sendKeys("Model ABC");
        driver.findElement(By.id("purchaseDate")).sendKeys("20022003");
        driver.findElement(By.id("warrantyExpiration")).sendKeys("20022005");

        // Выбор статуса оборудования
        WebElement statusDropdown = driver.findElement(By.id("status"));
        statusDropdown.click();
        WebElement statusOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//option[@value='IN_USE']")));
        statusOption.click();

        // Заполнение полей для компьютера (если это компьютер)
        driver.findElement(By.id("cpu")).sendKeys("Intel Core i7");
        driver.findElement(By.id("ram")).sendKeys("16");
        driver.findElement(By.id("storage")).sendKeys("512");
        driver.findElement(By.id("gpu")).sendKeys("NVIDIA GTX 1080");

        // 6. Отправка формы
        WebElement button = driver.findElement(By.cssSelector("button[type='submit']"));
        Actions actions = new Actions(driver);
        actions.moveToElement(button).perform();
        button.click();

        // 7. Проверка успешного создания оборудования
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h2"), "Техника"));
        assertEquals("Техника", driver.findElement(By.tagName("h2")).getText());

        // 8. Проверка, что новое оборудование появляется в списке
        WebElement createdEquipment = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(), 'Компьютер 1')]")));
        assertNotNull(createdEquipment, "Созданное оборудование должно отображаться в списке.");

        // 12. Нажать на "Выход"
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Выход")));
        logoutLink.click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h2"), "Вход"));
        assertEquals("Вход", driver.findElement(By.tagName("h2")).getText());
    }

    @Test
    public void testEditEquipment() {
        // 1. Переход на вход
        driver.get(URL + "/login");

        // 2. Вход в аккаунт администратора
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h2"), "Вход"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("admin@admin.ru");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // 4. Переход на страницу редактирования техники
        driver.get(URL + "/computer");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h2"), "Техника"));
        WebElement editButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(), 'Изменить')]")));
        editButton.click();

        // 5. Заполнение формы для редактирования оборудования
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).clear();
        driver.findElement(By.id("name")).sendKeys("Обновленный Компьютер");

        driver.findElement(By.id("serialNumber")).clear();
        driver.findElement(By.id("serialNumber")).sendKeys("SN654321");

        driver.findElement(By.id("inventoryNumber")).clear();
        driver.findElement(By.id("inventoryNumber")).sendKeys("IN654321");

        driver.findElement(By.id("manufacturer")).clear();
        driver.findElement(By.id("manufacturer")).sendKeys("Manufacturer ABC");

        driver.findElement(By.id("model")).clear();
        driver.findElement(By.id("model")).sendKeys("Model XYZ");

        // Выбор нового статуса
        WebElement statusDropdown = driver.findElement(By.id("status"));
        statusDropdown.click();
        WebElement statusOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//option[@value='IN_REPAIR']")));
        statusOption.click();

        // Поля для компьютера (если это компьютер)
        driver.findElement(By.id("cpu")).clear();
        driver.findElement(By.id("cpu")).sendKeys("Intel Core i9");

        driver.findElement(By.id("ram")).clear();
        driver.findElement(By.id("ram")).sendKeys("32");

        driver.findElement(By.id("storage")).clear();
        driver.findElement(By.id("storage")).sendKeys("1024");

        driver.findElement(By.id("gpu")).clear();
        driver.findElement(By.id("gpu")).sendKeys("NVIDIA RTX 3080");

        // 6. Отправка формы
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        Actions actions = new Actions(driver);
        actions.moveToElement(submitButton).perform();
        submitButton.click();

        // 7. Проверка успешного изменения оборудования
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h2"), "Техника"));
        assertEquals("Техника", driver.findElement(By.tagName("h2")).getText());

        // 8. Проверка, что обновленное оборудование появляется в списке
        WebElement updatedEquipment = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(), 'Обновленный Компьютер')]")));
        assertNotNull(updatedEquipment, "Обновленное оборудование должно отображаться в списке.");

        // 12. Нажать на "Выход"
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Выход")));
        logoutLink.click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h2"), "Вход"));
        assertEquals("Вход", driver.findElement(By.tagName("h2")).getText());
    }

    @Test
    public void testCreateEquipmentDecommissionRequest() throws InterruptedException {
        // 1. Авторизация пользователя
        driver.get(URL + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("testuser@example.com");
        driver.findElement(By.name("password")).sendKeys("password123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // 2. Переход на вкладку "Техника"
        driver.get(URL + "/computer");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h2"), "Техника"));
        assertEquals("Техника", driver.getTitle(), "Проверка заголовка страницы списка техники");

        // 3. Открытие заявки на списание первой доступной техники
        WebElement firstEquipmentCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".card")));
        WebElement decommissionButton = firstEquipmentCard.findElement(By.cssSelector(".btn-danger[onclick^='deleteEquipmentRequest']"));
        decommissionButton.click();

        // 4. Подтверждение создания заявки
        driver.switchTo().alert().accept();
        Thread.sleep(1000);

        // 5. Переход на страницу заявок и проверка статуса
        driver.get(URL + "/computer/request");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h2"), "Заявки"));

        List<WebElement> rows = driver.findElements(By.cssSelector("table.table tbody tr"));
        boolean found = false;

        for (WebElement row : rows) {
            WebElement equipmentNameCell = wait.until(ExpectedConditions.visibilityOf(row.findElement(By.cssSelector("td:nth-child(1)"))));
            WebElement requestTypeCell = wait.until(ExpectedConditions.visibilityOf(row.findElement(By.cssSelector("td:nth-child(3)"))));
            WebElement statusCell = wait.until(ExpectedConditions.visibilityOf(row.findElement(By.cssSelector("td:nth-child(4)"))));

            if (equipmentNameCell.getText().contains("Обновленный Компьютер") &&
                    requestTypeCell.getText().equals("Списание") &&
                    statusCell.getText().equals("Ожидает рассмотрения")) {
                found = true;
                break;
            }
        }

        assertTrue(found, "Проверка, что заявка на списание оборудования была создана и имеет статус 'В обработке'.");

        // 6. Выход
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Выход")));
        logoutLink.click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h2"), "Вход"));
        assertEquals("Вход", driver.findElement(By.tagName("h2")).getText());
    }

    @Test
    public void testNotificationArrivedAndAcceptRequestAndUpdateEquipmentStatus() throws InterruptedException {
        // 1. Переход на вход
        driver.get(URL + "/login");

        // 2. Вход в аккаунт администратора
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h2"), "Вход"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("admin@admin.ru");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Открыть страницу уведомлений
        driver.get(URL + "/notification");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".notification-card")));

        // Находим уведомление, соответствующее заявке на списание
        WebElement notificationCard = driver.findElement(By.xpath("//div[contains(@class, 'notification-card')]"));
        notificationCard.click();

        // Ожидаем появления модального окна и принимаем заявку
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notificationModal")));
        WebElement acceptBtn = modal.findElement(By.id("acceptBtn"));
        acceptBtn.click();
        wait.until(ExpectedConditions.alertIsPresent()).accept();

        // Переходим на страницу "Техника"
        driver.get(URL + "/computer");

        // Находим технику, которая была списана
        WebElement equipmentCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(), 'Обновленный Компьютер')]")));

        // Проверяем, что статус техники "Списана" в карточке
        WebElement statusText = equipmentCard.findElement(By.xpath("//div[@class='card-body']//p[contains(text(), 'Статус:')]"));
        assertTrue(statusText.getText().contains("Списана"));
    }
}
