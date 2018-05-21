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

package com.nsnik.nrs.kotlintest.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface UserDao {

    @Query("SELECT * FROM UserEntity")
    fun getUserList(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM UserEntity WHERE id = :id")
    fun getUserById(id: Int): LiveData<UserEntity>

    @Query("SELECT * FROM UserEntity WHERE name LIKE '%' || :name || '%'")
    fun getUserByName(name: String): LiveData<List<UserEntity>>

    @Query("SELECT * FROM UserEntity WHERE age = :age")
    fun getUserByAge(age: Int): LiveData<List<UserEntity>>

    @Query("SELECT * FROM UserEntity WHERE phone LIKE :phone || '%'")
    fun getUserByPhone(phone: Double): LiveData<List<UserEntity>>

    @Query("SELECT * FROM UserEntity WHERE address LIKE :address || '%'")
    fun getUserByAddress(address: String): LiveData<List<UserEntity>>

    @Query("SELECT * FROM UserEntity WHERE email LIKE :email || '%'")
    fun getUserByEmail(email: String): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(vararg userEntity: UserEntity): List<Long>

    @Delete
    fun deleteUser(vararg userEntity: UserEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(vararg userEntity: UserEntity): Int

}