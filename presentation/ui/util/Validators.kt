package com.alborihi.myapp.presentation.ui.util

object Validators {
    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && name.length >= 2 && name.length <= 100
    }

    fun isValidPhone(phone: String): Boolean {
        return phone.isNotBlank() && phone.length >= 8 && phone.length <= 20
    }

    fun isValidGrade(grade: Double, maxGrade: Double = Constants.MAX_GRADE): Boolean {
        return grade >= 0 && grade <= maxGrade
    }

    fun isValidClassName(className: String): Boolean {
        return className.isNotBlank() && className.length <= 50
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
