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

package com.nsnik.nrs.kotlintest.views.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.jakewharton.rxbinding2.view.RxView
import com.nsnik.nrs.kotlintest.R
import com.nsnik.nrs.kotlintest.data.UserEntity
import com.nsnik.nrs.kotlintest.viewModel.UserListViewModel
import com.nsnik.nrs.kotlintest.views.adapters.UserInputAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.user_input_fragment.*
import timber.log.Timber

class UserInputFragment : Fragment() {

    private lateinit var userInputAdapter: UserInputAdapter
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var listViewModel: UserListViewModel
    private val userEntity: UserEntity = UserEntity()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.user_input_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        listeners()
    }

    private fun initialize() {
        userInputAdapter = UserInputAdapter(activity, userEntity)

        addUserList?.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = userInputAdapter
        }

        listViewModel = ViewModelProviders.of(this).get(UserListViewModel::class.java)
    }

    private fun listeners() {
        compositeDisposable.add(RxView.clicks(addUserAdd).subscribe { if (checkValues()) insertUserWork() else Timber.d("Invalid Values") })
    }

    private fun cleanUp() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        cleanUp()
    }

    private fun checkValues(): Boolean {
        return true
    }

    private fun insertUserWork() {

        val workManager: WorkManager = WorkManager.getInstance()

        val constraint = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val localInsertRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<LocalInsertWorker>()
                .build()

        val networkInsertRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<NetworkInsertWorker>()
                .setConstraints(constraint)
                .build()

        workManager.beginWith(localInsertRequest)
                .then(networkInsertRequest)
                .enqueue()

        workManager.getStatusById(networkInsertRequest.id)
                .observe(this, Observer {
                    if (it != null && it.state.isFinished) {
                        //INSERT INTO DATABASE COMPLETE
                    }
                })
    }

    private inner class LocalInsertWorker : Worker() {

        override fun doWork(): WorkerResult {
            listViewModel.insertUserLocal(userEntity)
            return WorkerResult.SUCCESS
        }


    }

    private inner class NetworkInsertWorker : Worker() {

        override fun doWork(): WorkerResult {
            listViewModel.insertUserServer(userEntity)
            return WorkerResult.SUCCESS
        }

    }
}