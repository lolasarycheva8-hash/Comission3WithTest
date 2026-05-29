fun calculateCommission(cardType: String = "Мир", previousMonthlyAmount: Double = 0.0, previousDailyAmount: Double = 0.0, amount: Double
): Double {

    if (cardType == "VK Pay") {
        var blocked = false
        if (amount > 15_000.0) {
            println("Операция заблокирована: превышен лимит одного перевода VK Pay (15000.0 руб.)")
            blocked = true
        }
        if (previousMonthlyAmount + amount > 40_000.0) {
            println("Операция заблокирована: превышен месячный лимит VK Pay (40000.0 руб.)")
            blocked = true
        }
        if (blocked) return -1.0
        return 0.0
    }

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
        "Mastercard", "Maestro" -> {
            if (amount >= 300.0 && previousMonthlyAmount + amount <= 75_000.0) 0.0
            else amount * 0.006 + 20
        }
        "Visa", "Мир" -> maxOf(amount * 0.0075, 35.0)
        else -> {
            println("Неизвестный тип карты: $cardType")
            -1.0
        }
    }
}

fun main() {
    println("= Тип карты: Mastercard (в рамках бесплатного лимита) =")
    val c1 = calculateCommission("Mastercard", 0.0, 0.0, 50_000.0)
    println("комиссия: $c1 руб.\n")

    println("= Тип карты: Mastercard (превышен месячный лимит) =")
    val c2 = calculateCommission("Mastercard", 70_000.0, 0.0, 10_000.0)
    println("комиссия: $c2 руб.\n")

    println("= Тип карты: Maestro =")
    val c3 = calculateCommission("Maestro", 0.0, 0.0, 5_000.0)
    println("комиссия: $c3 руб.\n")

    println("= Тип карты: Visa =")
    val c4 = calculateCommission("Visa", 0.0, 0.0, 10_000.0)
    println("комиссия: $c4 руб.\n")

    println("= Тип карты: Мир =")
    val c5 = calculateCommission("Мир", 0.0, 0.0, 10_000.0)
    println("комиссия: $c5 руб.\n")

    println("= Тип карты: VK Pay =")
    val c6 = calculateCommission("VK Pay", 0.0, 0.0, 5_000.0)
    println("комиссия: $c6 руб.\n")

    println("= Тип карты: VK Pay (превышен лимит операции) =")
    calculateCommission("VK Pay", 0.0, 0.0, 20_000.0)
}