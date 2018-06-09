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

import com.nsnik.nrs.kotlintest.dagger.scopes.ApplicationScope
import com.nsnik.nrs.kotlintest.data.UserEntity
import com.nsnik.nrs.kotlintest.utils.events.FetchListEvent
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject

@ApplicationScope
class NetworkUtil @Inject constructor(private val retrofit: Retrofit) {

    fun getUserListFromServer() {
        retrofit.create(RetrofitServiceApi::class.java)
                .getUserList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<UserEntity>> {
                    override fun onSuccess(t: List<UserEntity>) {
                        EventBus.getDefault().post(FetchListEvent(t))
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        Timber.d(e)
                    }

                })
    }

    fun addUser(userEntity: UserEntity?) {
        retrofit.create(RetrofitServiceApi::class.java)
                .addUser(userEntity?.name, userEntity?.age, userEntity?.phone, userEntity?.address, userEntity?.email, userEntity?.date, userEntity?.avatar)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<String> {
                    override fun onSuccess(t: String) {
                        Timber.d(t)
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        Timber.d(e)
                    }

                })
    }

    fun updateUser(userEntity: UserEntity?) {
        retrofit.create(RetrofitServiceApi::class.java)
                .updateUser(userEntity?.id, userEntity?.name, userEntity?.age, userEntity?.phone, userEntity?.address, userEntity?.email, userEntity?.date, userEntity?.avatar)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<String> {
                    override fun onSuccess(t: String) {
                        Timber.d(t)
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        Timber.d(e)
                    }

                })
    }

    fun deleteUser(id: Int) {
        retrofit.create(RetrofitServiceApi::class.java)
                .deleteUser(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<String> {
                    override fun onSuccess(t: String) {
                        Timber.d(t)
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        Timber.d(e)
                    }

                })
    }
}