package com.alborihi.myapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled Dashboard
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Export
import androidx.compose.material.icons.filled.Import
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings

/**
 *sealed class يُمثّل جميع الشاشات (Routes).
 * كل شاشة: عنوان، أيقونة (للـ Bottom Nav)، ومسار.
 */
sealed class Screen(
    val route: String,
    val title: String,
    val icon: @androidx.compose.material3.ExperimentalMaterial3Api
    () -> androidx.compose.ui.graphics.vector.ImageVector,
    val showInBottomNav: Boolean = true
) {
    // S1 — لوحة التحكم
    object Dashboard : Screen(
        route = NavRoute.DASHBOARD,
        title = "لوحة التحكم",
        icon = { Icons.Default.Dashboard }
    )

    // S2 — إدارة الطلاب
    object Students : Screen(
        route = NavRoute.STUDENTS,
        title = "الطلاب",
        icon = { Icons.Default.Person }
    )
    object StudentForm : Screen(
        route = "${NavRoute.STUDENT_FORM}/{studentId}",
        title = "تفاصيل الطالب",
        icon = { Icons.Default.PersonAdd },
        showInBottomNav = false
    )

    // S3/S4 — إدخال الدرجات + الجدول
    object GradeEntry : Screen(
        route = NavRoute.GRADE_ENTRY,
        title = "إدخال الدرجات",
        icon = { Icons.Default.Assessment }
    )
    object GradesTable : Screen(
        route = NavRoute.GRADES_TABLE,
        title = "جدول الدرجات",
        icon = { Icons.Default.Assessment },
        showInBottomNav = false
    )

    // S5 — ملف الطالب
    object StudentProfile : Screen(
        route = NavRoute.STUDENT_PROFILE,
        title = "ملف الطالب",
        icon = { Icons.Default.Person },
        showInBottomNav = false
    )

    // S6 — التحليل الإحصائي
    object Analytics : Screen(
        route = NavRoute.ANALYTICS,
        title = "التحليل الإحصائي",
        icon = { Icons.Default.PieChart }
    )

    // S7 — الاستيراد
    object Import : Screen(
        route = NavRoute.IMPORT,
        title = "استيراد",
        icon = { Icons.Default.Import }
    )

    // S8 — التصدير
    object Export : Screen(
        route = NavRoute.EXPORT,
        title = "تصدير",
        icon = { Icons.Default.Export }
    )

    // S9 — التقارير
    object Reports : Screen(
        route = NavRoute.REPORTS,
        title = "التقارير",
        icon = { Icons.Default.Analytics }
    )

    // S10 — الإعدادات
    object Settings : Screen(
        route = NavRoute.SETTINGS,
        title = "الإعدادات",
        icon = { Icons.Default.Settings }
    )

    // S11 — النسخ الاحتياطي
    object Backup : Screen(
        route = NavRoute.BACKUP,
        title = "النسخ الاحتياطي",
        icon = { Icons.Default.Backup }
    )

    // شاشة الخروج (non-navigable)
    object ExitApp : Screen(
        route = NavRoute.EXIT_APP,
        title = "",
        icon = { Icons.Default.ExitToApp },
        showInBottomNav = false
    )
}
