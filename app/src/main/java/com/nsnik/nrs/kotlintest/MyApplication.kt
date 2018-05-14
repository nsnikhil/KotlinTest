/*
 *    Copyright 2018 nsnikhil
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.nsnik.nrs.kotlintest

import android.app.Application
import android.content.Context
import android.os.StrictMode
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatDelegate
import com.github.moduth.blockcanary.BlockCanary
import com.nsnik.nrs.kotlintest.dagger.components.DaggerDatabaseComponent
import com.nsnik.nrs.kotlintest.dagger.components.DaggerNetworkComponent
import com.nsnik.nrs.kotlintest.dagger.modules.ContextModule
import com.nsnik.nrs.kotlintest.utils.AppBlockCanaryContext
import com.nsnik.nrs.kotlintest.utils.DatabaseUtil
import com.nsnik.nrs.kotlintest.utils.NetworkUtil
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import timber.log.Timber


class MyApplication : Application() {

    private var refWatcher: RefWatcher? = null
    private val contextModule: ContextModule = ContextModule(this)
    var networkUtil: NetworkUtil = DaggerNetworkComponent.create().getNetworkUtil()
    var databaseUtil: DatabaseUtil = DaggerDatabaseComponent.builder().contextModule(contextModule).build().getDatabaseUtil()

    companion object {

        init {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        fun getRefWatcher(context: Context): RefWatcher? {
            val application: MyApplication? = context.applicationContext as? MyApplication
            return application?.refWatcher
        }
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                @NonNull
                override fun createStackElementTag(@NonNull element: StackTraceElement): String? {
                    return super.createStackElementTag(element) + ":" + element.lineNumber
                }
            })
            refWatcher = LeakCanary.install(this)
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
            BlockCanary.install(this, AppBlockCanaryContext()).start()
        }
    }

}