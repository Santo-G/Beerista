package com.worldbeers.beerista

import android.app.Application
import android.os.StrictMode
import com.worldbeers.beerista.domain.appModule
import com.worldbeers.beerista.koindi.networkingKoinModule
import com.worldbeers.beerista.utils.LineNumberDebugTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.BuildConfig
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber


class MyApplication : Application() {
    override fun onCreate() {
        setupStrictMode()
        super.onCreate()
        // Start Koin
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(appModule, networkingKoinModule)
        }
        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(LineNumberDebugTree())
        }
    }

    private fun setupStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog() // log in logcat
                    .build()
            )
        }
    }

}
