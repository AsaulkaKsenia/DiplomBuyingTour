package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.page.PurchasePage;

import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataGenerator.*;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.DataHelper.getPaymentInfo;

public class DiplomBuyingTour {

    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void openPage() {
        open("http://localhost:8080");
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
        databaseCleanUp();
    }

    @Nested
    //Позитивные тесты
    public class ValidCard {

        @Test
        @SneakyThrows
        @DisplayName("Покупка тура по дебетовой карте")
        public void shouldPaymentValidCard() {
            var purchasePage = new PurchasePage();
            purchasePage.cardPayment();
            var info = getApprovedCard();
            purchasePage.sendingData(info);
            TimeUnit.SECONDS.sleep(12);
            var expected = "APPROVED";
            var paymentInfo = getPaymentInfo();
            var orderInfo = getOrderInfo();
            assertEquals(expected, paymentInfo.getStatus());
            assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
            purchasePage.bankApproved();
        }

        @Test
        @SneakyThrows
        @DisplayName("Покупка тура в кредит")
        public void shouldCreditValidCard() {
            var purchasePage = new PurchasePage();
            purchasePage.cardCredit();
            var info = getApprovedCard();
            purchasePage.sendingData(info);
            TimeUnit.SECONDS.sleep(12);
            var expected = "APPROVED";
            var creditRequestInfo = getCreditRequestInfo();
            var orderInfo = getOrderInfo();
            assertEquals(expected, creditRequestInfo.getStatus());
            assertEquals(creditRequestInfo.getBank_id(), orderInfo.getCredit_id());
            purchasePage.bankApproved();
        }
    }

    @Nested
    //Негативные тесты
    public class InvalidCard {

        @Test
        @SneakyThrows
        @DisplayName("Покупка не валидной картой")
        public void shouldPaymentInvalidCard() {
            var purchasePage = new PurchasePage();
            purchasePage.cardPayment();
            var info = getDeclinedCard();
            purchasePage.sendingData(info);
            TimeUnit.SECONDS.sleep(12);
            var expected = "DECLINED";
            var paymentInfo = getPaymentInfo();
            var orderInfo = getOrderInfo();
            assertEquals(expected, paymentInfo.getStatus());
            assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
            purchasePage.bankDeclined();
        }

        @Test
        @SneakyThrows
        @DisplayName("Получение кредита по не валидной карте")
        public void shouldCreditInvalidCard() {
            var purchasePage = new PurchasePage();
            purchasePage.cardCredit();
            var info = getDeclinedCard();
            purchasePage.sendingData(info);
            TimeUnit.SECONDS.sleep(12);
            var expected = "DECLINED";
            var creditRequestInfo = getCreditRequestInfo();
            var orderInfo = getOrderInfo();
            assertEquals(expected, creditRequestInfo.getStatus());
            assertEquals(creditRequestInfo.getBank_id(), orderInfo.getCredit_id());
            purchasePage.bankApproved();
        }
    }

    @Nested
    //Покупка с пустыми полями
    public class PaymentFormFieldValidation {

        @BeforeEach
        public void setPayment() {
            var purchasePage = new PurchasePage();
            purchasePage.cardPayment();
        }

        @Test
        @DisplayName("Покупка тура с пустыми полями в форме покупки")
        public void shouldEmpty() {
            var purchasePage = new PurchasePage();
            purchasePage.emptyForm();
        }

        @Test
        @DisplayName("Покупка тура с пустым полем 'Номер карты'")
        public void shouldEmptyCardNumberField() {
            var purchasePage = new PurchasePage();
            var info = getApprovedCard();
            purchasePage.emptyCardNumberField(info);
        }

        @Test
        @DisplayName("Покупка тура с пустым полем 'Месяц'")
        public void shouldEmptyMonthField() {
            var purchasePage = new PurchasePage();
            var info = getApprovedCard();
            purchasePage.emptyMonthField(info);
        }

        @Test
        @DisplayName("Покупка тура с пустым полем 'Год'")
        public void shouldEmptyYearField() {
            var purchasePage = new PurchasePage();
            var info = getApprovedCard();
            purchasePage.emptyYearField(info);
        }

        @Test
        @DisplayName("Покупка тура с пустым полем 'Владелец'")
        public void shouldEmptyOwnerField() {
            var purchasePage = new PurchasePage();
            var info = getApprovedCard();
            purchasePage.emptyOwnerField(info);
        }

        @Test
        @DisplayName("Покупка тура с пустым полем 'CVC/CVV'")
        public void shouldEmptyCVCField() {
            var purchasePage = new PurchasePage();
            var info = getApprovedCard();
            purchasePage.emptyCVCField(info);
        }

        //Покупка с невалидными значениями в полях
        @Test
        @DisplayName("Покупка тура по карте с невалидным номером")
        public void shouldCardWithIncompleteCardNumber() {
            var purchasePage = new PurchasePage();
            var info = getCardWithIncompleteCardNumber();
            purchasePage.invalidCardNumberField(info);
        }

        @Test
        @DisplayName("Покупка тур по карте с 'просроченным' месяцем")
        public void shouldCardWithOverdueMonth() {
            var purchasePage = new PurchasePage();
            var info = getCardWithOverdueMonth();
            purchasePage.invalidMonthField(info);
        }

        @Test
        @DisplayName("Покупка тура по карте с 'просроченным' годом")
        public void shouldCardWithOverdueYear() {
            var purchasePage = new PurchasePage();
            var info = getCardWithOverdueYear();
            purchasePage.invalidYearField(info);
        }


        @Test
        @DisplayName("Покупка тур по карте с несуществующим месяцем")
        public void shouldCardWithGreaterMonthValue() {
            var purchasePage = new PurchasePage();
            var info = getCardWithGreaterMonthValue();
            purchasePage.invalidMonthField(info);
        }

        @Test
        @DisplayName("Покупка тур по карте с годом значительно большим текущего")
        public void shouldCardWithYearFromFuture() {
            var purchasePage = new PurchasePage();
            var info = getCardWithYearFromFuture();
            purchasePage.invalidYearField(info);
        }

        @Test
        @DisplayName("Покупка тура по карте с номером 'CVC', содержащим меньше трех цифр")
        public void shouldCardWithIncompleteCVC() {
            var purchasePage = new PurchasePage();
            var info = getCardWithIncompleteCVC();
            purchasePage.invalidCVCField(info);
        }


        @Test
        @DisplayName("Покупка тура по карте с невалидными значениями в поле 'Владелец', пробелы")
        public void shouldCardWithSpaceOrHyphenOwner() {
            var purchasePage = new PurchasePage();
            var info = getCardWithSpaceOrHyphenOwner();
            purchasePage.invalidOwnerField(info);
        }

        @Test
        @DisplayName("Покупка тура по карте с невалидными значениями в поле 'Владелец', спецсимволы")
        public void shouldCardWithSpecialSymbolsOwner() {
            var purchasePage = new PurchasePage();
            var info = getCardWithSpecialSymbolsOwner();
            purchasePage.invalidOwnerField(info);
        }

        @Test
        @DisplayName("Покупка тура по карте с невалидными значениями в поле 'Владелец', цифры")
        public void shouldCardWithNumbersOwner() {
            var purchasePage = new PurchasePage();
            var info = getCardWithNumbersOwner();
            purchasePage.invalidOwnerField(info);
        }

    }

    @Nested
    //Покупка с пустыми полями в кредит:
    public class CreditFormFieldValidation {

        @BeforeEach
        public void setPayment() {
            var purchasePage = new PurchasePage();
            purchasePage.cardCredit();
        }

        @Test
        @DisplayName("Покупка тура в кредит с пустыми полями")
        public void shouldEmpty() {
            var purchasePage = new PurchasePage();
            purchasePage.emptyForm();
        }

        @Test
        @DisplayName("Покупка тура в кредит с пустым полем 'Номер карты'")
        public void shouldEmptyCardNumberField() {
            var purchasePage = new PurchasePage();
            var info = getApprovedCard();
            purchasePage.emptyCardNumberField(info);
        }

        @Test
        @DisplayName("Покупка тура в кредит с пустым полем 'Месяц'")
        public void shouldEmptyMonthField() {
            var purchasePage = new PurchasePage();
            var info = getApprovedCard();
            purchasePage.emptyMonthField(info);
        }

        @Test
        @DisplayName("Покупка тура в кредит с пустым полем 'Год'")
        public void shouldEmptyYearField() {
            var purchasePage = new PurchasePage();
            var info = getApprovedCard();
            purchasePage.emptyYearField(info);
        }

        @Test
        @DisplayName("Покупка тура в кредит с пустым полем 'Владелец'")
        public void shouldEmptyOwnerField() {
            var purchasePage = new PurchasePage();
            var info = getApprovedCard();
            purchasePage.emptyOwnerField(info);
        }

        @Test
        @DisplayName("Покупка тура в кредит с пустым полем 'CVC/CVV'")
        public void shouldEmptyCVCField() {
            var purchasePage = new PurchasePage();
            var info = getApprovedCard();
            purchasePage.emptyCVCField(info);
        }

        //Покупка тура в кредит с невалидными значениями в полях

        @Test
        @DisplayName("Покупка тура в кредит по карте с невалидным номером")
        public void shouldCardWithIncompleteCardNumber() {
            var purchasePage = new PurchasePage();
            var info = getCardWithIncompleteCardNumber();
            purchasePage.invalidCardNumberField(info);
        }

        @Test
        @DisplayName("Покупка тура в кредит по карте с 'просроченным' месяцем")
        public void shouldCardWithOverdueMonth() {
            var purchasePage = new PurchasePage();
            var info = getCardWithOverdueMonth();
            purchasePage.invalidMonthField(info);
        }

        @Test
        @DisplayName("Покупка тура в кредит по карте с 'просроченным' годом")
        public void shouldCardWithOverdueYear() {
            var purchasePage = new PurchasePage();
            var info = getCardWithOverdueYear();
            purchasePage.invalidYearField(info);
        }

        @Test
        @DisplayName("окупка тура в кредит по карте с несуществующим месяцем")
        public void shouldCardWithGreaterMonthValue() {
            var purchasePage = new PurchasePage();
            var info = getCardWithGreaterMonthValue();
            purchasePage.invalidMonthField(info);
        }

        @Test
        @DisplayName("Покупка тур в кредит с годом значительно больше текущего")
        public void shouldCardWithYearFromFuture() {
            var purchasePage = new PurchasePage();
            var info = getCardWithYearFromFuture();
            purchasePage.invalidYearField(info);
        }

        @Test
        @DisplayName("Покупка тура в кредит с номером 'CVC', содержащим меньше трех цифр")
        public void shouldCardWithIncompleteCVC() {
            var purchasePage = new PurchasePage();
            var info = getCardWithIncompleteCVC();
            purchasePage.invalidCVCField(info);
        }


        @Test
        @DisplayName("Покупка тура в кредит с невалидными значениями в поле 'Владелец', пробелы")
        public void shouldCardWithSpaceOrHyphenOwner() {
            var purchasePage = new PurchasePage();
            var info = getCardWithSpaceOrHyphenOwner();
            purchasePage.invalidOwnerField(info);
        }

        @Test
        @DisplayName("Покупка тура в кредит с невалидными значениями в поле 'Владелец', спецсимволы")
        public void shouldCardWithSpecialSymbolsOwner() {
            var purchasePage = new PurchasePage();
            var info = getCardWithSpecialSymbolsOwner();
            purchasePage.invalidOwnerField(info);
        }

        @Test
        @DisplayName("Покупка тура в кредит с невалидными значениями в поле 'Владелец', цифры")
        public void shouldCardWithNumbersOwner() {
            var purchasePage = new PurchasePage();
            var info = getCardWithNumbersOwner();
            purchasePage.invalidOwnerField(info);
        }


    }
}
