# التوقيع والنشر

1. أنشئ مفتاح التوقيع:

```bash
keytool -genkey -v -keystore release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias release
```

2. انسخ `keystore.properties.example` إلى `keystore.properties` واملأ القيم.
3. ابنِ:

```bash
gradle wrapper   # مرة واحدة لتوليد gradlew
./gradlew assembleRelease   # APK موقّع
./gradlew bundleRelease     # AAB لمتجر Google Play
```

4. للتجربة على جهاز: `adb install -r app/build/outputs/apk/release/app-release.apk`

> لا ترفع ملف المفتاح أو كلمات المرور إلى أي مستودع.
