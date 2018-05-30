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

package com.nsnik.nrs.kotlintest.utils

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.nsnik.nrs.kotlintest.dagger.scopes.ApplicationScope
import com.nsnik.nrs.kotlintest.data.AppDatabase
import com.nsnik.nrs.kotlintest.data.UserEntity
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


@ApplicationScope
class DatabaseUtil @Inject constructor(private val appDatabase: AppDatabase) {

    private val pageConfig: PagedList.Config = PagedList.Config.Builder().setPageSize(10)
            .build()

    fun getUsersList(): LiveData<PagedList<UserEntity>> {
        return LivePagedListBuilder(appDatabase.getUserDao().getUserList(), pageConfig).build()
    }

    fun getUserById(id: Int): LiveData<UserEntity> {
        return appDatabase.getUserDao().getUserById(id)
    }

    fun getUsersByName(name: String): LiveData<PagedList<UserEntity>> {
        return LivePagedListBuilder(appDatabase.getUserDao().getUserByName(name), pageConfig).build()
    }

    fun getUsersByAge(age: Int): LiveData<PagedList<UserEntity>> {
        return LivePagedListBuilder(appDatabase.getUserDao().getUserByAge(age), pageConfig).build()
    }

    fun getUserByPhone(phone: Double): LiveData<PagedList<UserEntity>> {
        return LivePagedListBuilder(appDatabase.getUserDao().getUserByPhone(phone), pageConfig).build()
    }

    fun getUsersByAddress(address: String): LiveData<PagedList<UserEntity>> {
        return LivePagedListBuilder(appDatabase.getUserDao().getUserByAddress(address), pageConfig).build()
    }

    fun getUserByEmail(email: String): LiveData<PagedList<UserEntity>> {
        return LivePagedListBuilder(appDatabase.getUserDao().getUserByEmail(email), pageConfig).build()
    }

    fun insertUser(userEntity: UserEntity) {
        val single: Single<List<Long>> = Single.fromCallable({ appDatabase.getUserDao().insertUser(userEntity) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        single.subscribe(object : SingleObserver<List<Long>> {
            override fun onSuccess(t: List<Long>) {
                t.forEach { Timber.d(it.toString()) }
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                Timber.d(e)
            }

        })
    }

    fun updateUser(userEntity: UserEntity) {
        val single: Single<Int> = Single.fromCallable({ appDatabase.getUserDao().updateUser(userEntity) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        single.subscribe(object : SingleObserver<Int> {
            override fun onSuccess(t: Int) {
                Timber.d(t.toString())
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                Timber.d(e)
            }

        })
    }

    fun deleteUser(userEntity: UserEntity) {
        val single: Single<Unit> = Single.fromCallable({ appDatabase.getUserDao().deleteUser(userEntity) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        single.subscribe(object : SingleObserver<Unit> {
            override fun onSuccess(t: Unit) {
                Timber.d("Deletion Successful")
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                Timber.d(e)
            }
        })
    }
}