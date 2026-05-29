import org.junit.Assert.*
import org.junit.Test

class MainTest {

    // === Mastercard / Maestro ===

    @Test
    fun mastercardFreeWhenAmountInRange() {
        val result = calculateCommission("Mastercard", 0.0, 0.0, 50_000.0)
        assertEquals(0.0, result, 0.0)
    }

    @Test
    fun mastercardPaidWhenAmountBelow300() {
        val result = calculateCommission("Mastercard", 0.0, 0.0, 200.0)
        assertEquals(21.2, result, 0.001)
    }

    @Test
    fun mastercardPaidWhenMonthlyLimitExceeded() {
        val result = calculateCommission("Mastercard", 70_000.0, 0.0, 10_000.0)
        assertEquals(10_000.0 * 0.006 + 20, result, 0.001)
    }

    @Test
    fun maestroFreeWhenAmountInRange() {
        val result = calculateCommission("Maestro", 0.0, 0.0, 5_000.0)
        assertEquals(0.0, result, 0.0)
    }

    @Test
    fun maestroPaidWhenMonthlyLimitExceeded() {
        val result = calculateCommission("Maestro", 75_000.0, 0.0, 1_000.0)
        assertEquals(1_000.0 * 0.006 + 20, result, 0.001)
    }

    // === Visa / Мир ===

    @Test
    fun visaMinCommission() {
        val result = calculateCommission("Visa", 0.0, 0.0, 4_000.0)
        assertEquals(35.0, result, 0.0)
    }

    @Test
    fun visaPercentCommission() {
        val result = calculateCommission("Visa", 0.0, 0.0, 10_000.0)
        assertEquals(75.0, result, 0.0)
    }

    @Test
    fun mirMinCommission() {
        val result = calculateCommission("Мир", 0.0, 0.0, 4_000.0)
        assertEquals(35.0, result, 0.0)
    }

    @Test
    fun mirPercentCommission() {
        val result = calculateCommission("Мир", 0.0, 0.0, 10_000.0)
        assertEquals(75.0, result, 0.0)
    }

    // === VK Pay ===

    @Test
    fun vkPayNoCommission() {
        val result = calculateCommission("VK Pay", 0.0, 0.0, 5_000.0)
        assertEquals(0.0, result, 0.0)
    }

    @Test
    fun vkPayBlockedWhenSingleTransferOver15000() {
        val result = calculateCommission("VK Pay", 0.0, 0.0, 20_000.0)
        assertEquals(-1.0, result, 0.0)
    }

    @Test
    fun vkPayBlockedWhenMonthlyOver40000() {
        val result = calculateCommission("VK Pay", 35_000.0, 0.0, 10_000.0)
        assertEquals(-1.0, result, 0.0)
    }

    // === Лимиты ===

    @Test
    fun dailyLimitExceeded() {
        val result = calculateCommission("Visa", 0.0, 140_000.0, 20_000.0)
        assertEquals(-1.0, result, 0.0)
    }

    @Test
    fun monthlyLimitExceeded() {
        val result = calculateCommission("Visa", 590_000.0, 0.0, 20_000.0)
        assertEquals(-1.0, result, 0.0)
    }

    @Test
    fun bothLimitsExceeded() {
        val result = calculateCommission("Visa", 590_000.0, 140_000.0, 20_000.0)
        assertEquals(-1.0, result, 0.0)
    }

    // === Неизвестный тип карты ===

    @Test
    fun unknownCardType() {
        val result = calculateCommission("Amex", 0.0, 0.0, 10_000.0)
        assertEquals(-1.0, result, 0.0)
    }
}