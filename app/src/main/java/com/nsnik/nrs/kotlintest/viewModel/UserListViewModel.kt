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

package com.nsnik.nrs.kotlintest.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.nsnik.nrs.kotlintest.MyApplication
import com.nsnik.nrs.kotlintest.data.UserEntity
import com.nsnik.nrs.kotlintest.utils.DatabaseUtil
import com.nsnik.nrs.kotlintest.utils.NetworkUtil

class UserListViewModel(application: Application) : AndroidViewModel(application) {

    private val databaseUtil: DatabaseUtil = (application as MyApplication).databaseUtil
    private val networkUtil: NetworkUtil = (application as MyApplication).networkUtil
    var userList: LiveData<PagedList<UserEntity>> = databaseUtil.getUsersList()

    fun fetchDataFromServer() {
        networkUtil.getUserListFromServer()
    }

    fun insertUserLocal(userEntity: UserEntity) {
        databaseUtil.insertUser(userEntity)
    }

    fun insertUserServer(userEntity: UserEntity) {
        networkUtil.addUser(userEntity)
    }

    fun deleteUserLocal(userEntity: UserEntity) {
        databaseUtil.deleteUser(userEntity)
    }

    fun deleteUserServer(id: Int) {
        networkUtil.deleteUser(id)
    }

    fun getUserById(id: Int): LiveData<UserEntity> {
        return databaseUtil.getUserById(id)
    }
}