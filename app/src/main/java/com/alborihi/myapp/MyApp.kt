package com.alborihi.myapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application Root مع Hilt.
 * يُنشأ عند بدء تشغيل التطبيق قبل أي Activity.
 */
@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // إعدادات إضافية عالمية (اختياري: logging، crash reporting...)
        instance = this
    }

    companion object {
        @Volatile
        private var instance: MyApp? = null

        val instance: MyApp
            get() = instance ?: throw IllegalStateException("MyApp instance not initialized")
    }
}
