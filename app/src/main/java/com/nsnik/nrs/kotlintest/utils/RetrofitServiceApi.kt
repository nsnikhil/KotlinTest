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

import com.nsnik.nrs.kotlintest.data.UserEntity
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface RetrofitServiceApi {

    @GET("/php/postgreSQLReadAll.php")
    fun getUserList(): Single<List<UserEntity>>

    @FormUrlEncoded
    @POST("/php/postgreSQLInsert.php")
    fun addUser(@Field("name") name: String?,
                @Field("age") age: Int?,
                @Field("phone") phone: Double?,
                @Field("address") address: String?,
                @Field("email") email: String?,
                @Field("date") date: String?,
                @Field("avatar") avatar: String?): Single<String>

    @FormUrlEncoded
    @POST("/php/postgreSQLUpdate.php")
    fun updateUser(@Field("id") id: Int?,
                   @Field("name") name: String?,
                   @Field("age") age: Int?,
                   @Field("phone") phone: Double?,
                   @Field("address") address: String?,
                   @Field("email") email: String?,
                   @Field("date") date: String?,
                   @Field("avatar") avatar: String?): Single<String>

    @FormUrlEncoded
    @POST("/php/postgreSQLDelete.php")
    fun deleteUser(@Field("id") id: Int?): Single<String>
}