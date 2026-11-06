package com.alborihi.myapp.domain.model

enum class Month(
    val displayName: String,
    val displayAbbr: String,
    val monthNumber: Int
) {
    JANUARY("يناير", "ينا", 1),
    FEBRUARY("فبراير", "فبر", 2),
    MARCH("مارس", "مارس", 3),
    APRIL("أبريل", "أبريل", 4),
    MAY("مايو", "مايو", 5),
    JUNE("يونيو", "يونيو", 6),
    JULY("يوليو", "يوليو", 7),
    AUGUST("أغسطس", "أغسطس", 8),
    SEPTEMBER("سبتمبر", "سبتمبر", 9),
    OCTOBER("أكتوبر", "أكتوبر", 10),
    NOVEMBER("نوفمبر", "نوفمبر", 11),
    DECEMBER("ديسمبر", "ديسمبر", 12);

    companion object {
        fun fromNumber(number: Int): Month? = values().find { it.monthNumber == number }
    }
}
