fun calculateCommission(cardType: String = "Мир", previousMonthlyAmount: Double = 0.0, previousDailyAmount: Double = 0.0,
                        amount: Double): Double {
    val dailyLimit = 150_000.0
    val monthlyLimit = 600_000.0

    var blocked = false

    if (previousDailyAmount + amount > dailyLimit) {
        println("Операция заблокирована: превышен суточный лимит ($dailyLimit руб.)")
        blocked = true
    }

    if (previousMonthlyAmount + amount > monthlyLimit) {
        println("Операция заблокирована: превышен месячный лимит ($monthlyLimit руб.)")
        blocked = true
    }

    if (blocked) return -1.0

    return when (cardType) {
        "Mastercard" -> {
            val freeAmount = maxOf(0.0, 75_000.0 - previousMonthlyAmount)
            val paidAmount = maxOf(0.0, amount - freeAmount)
            if (paidAmount > 0) paidAmount * 0.006 + 20 else 0.0
        }
        "Visa" -> maxOf(amount * 0.0075, 35.0)
        "Мир" -> 0.0
        else -> {
            println("Неизвестный тип карты: $cardType")
            -1.0
        }
    }
}

fun main() {
    val amount1 = 90_000.0
    val c1 = calculateCommission("Mastercard", 0.0, 0.0, amount1)
    println("= тип карты: Mastercard, сумма перевода: $amount1 руб. =")
    if (c1 >= 0) println("комиссия: $c1 руб.")

    println()
    val amount3 = 90_000.0
    val c3 = calculateCommission("Visa", 0.0, 0.0, amount3)
    println("= тип карты: Visa, сумма перевода: $amount3 руб. =")
    if (c3 >= 0) println("комиссия: $c3 руб.")

    println()
    val amount4 = 860_000.0
    val c4 = calculateCommission(amount = amount4)
    println("= тип карты: Мир, сумма перевода: $amount4 руб. =")
    if (c4 >= 0) println("комиссия: $c4 руб.")
}