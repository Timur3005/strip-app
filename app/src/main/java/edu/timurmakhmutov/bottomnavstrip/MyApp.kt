package edu.timurmakhmutov.bottomnavstrip

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("4e7a4456-36a5-46f5-8d1c-7452d3557ba9")
    }
}