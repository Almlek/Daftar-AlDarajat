package com.alborihi.myapp.presentation.navigation

/**
 * ثوابت المسارات (Routes Constants).
 * يجب أن تتطابق تماماً مع المسارات في Screen.kt.
 */
object NavRoute {
    const val DASHBOARD = "dashboard"
    const val STUDENTS = "students"
    const val STUDENT_FORM = "student_form/{studentId}"
    const val GRADE_ENTRY = "grade_entry/{studentId}"
    const val GRADES_TABLE = "grades_table/{studentId}"
    const val STUDENT_PROFILE = "student_profile/{studentId}"
    const val ANALYTICS = "analytics"
    const val IMPORT = "import"
    const val EXPORT = "export"
    const val REPORTS = "reports"
    const val SETTINGS = "settings"
    const val BACKUP = "backup"
    const val EXIT_APP = "exit_app"

    // Parameters
    const val STUDENT_ID = "studentId"
}
