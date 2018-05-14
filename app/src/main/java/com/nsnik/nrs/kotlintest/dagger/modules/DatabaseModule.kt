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

package com.nsnik.nrs.kotlintest.dagger.modules

import android.content.Context
import androidx.room.Room
import com.nsnik.nrs.kotlintest.dagger.qualifiers.ApplicationQualifier
import com.nsnik.nrs.kotlintest.dagger.qualifiers.DatabaseName
import com.nsnik.nrs.kotlintest.dagger.scopes.ApplicationScope
import com.nsnik.nrs.kotlintest.data.AppDatabase
import dagger.Module
import dagger.Provides

@Module(includes = [(ContextModule::class)])
class DatabaseModule {

    private val DATABASE_NAME: String = "NA"

    @Provides
    @DatabaseName
    fun getDatabaseName(): String {
        return this.DATABASE_NAME
    }

    @Provides
    @ApplicationScope
    fun getAppDatabase(@ApplicationQualifier context: Context, @DatabaseName databaseName: String): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, databaseName).build()
    }

}