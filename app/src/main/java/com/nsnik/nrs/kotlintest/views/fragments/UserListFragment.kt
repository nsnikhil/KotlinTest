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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.jakewharton.rxbinding2.view.RxView
import com.nsnik.nrs.kotlintest.R
import com.nsnik.nrs.kotlintest.data.UserEntity
import com.nsnik.nrs.kotlintest.utils.events.UserDeleteEvent
import com.nsnik.nrs.kotlintest.viewModel.UserListViewModel
import com.nsnik.nrs.kotlintest.views.adapters.UserListAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class UserListFragment : Fragment() {

    private lateinit var listAdapter: UserListAdapter
    private lateinit var listViewModel: UserListViewModel
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        listeners()
    }

    private fun initialize() {
        listAdapter = UserListAdapter()


        homeList?.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = listAdapter
        }

        listViewModel = ViewModelProviders.of(this).get(UserListViewModel::class.java)
        listViewModel.userList.observe(this, Observer { newUserList -> listAdapter.submitList(newUserList) })
    }

    private fun listeners() {
        compositeDisposable.add(RxView.clicks(homeAdd).subscribe({ homeAdd.findNavController().navigate(R.id.listToInput) }))
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun OnUserDeleteEvent(userDeleteEvent: UserDeleteEvent) {
        deleteUserWork(userDeleteEvent.userEntity)
    }

    private fun deleteUserWork(userEntity: UserEntity) {

        val workManager: WorkManager = WorkManager.getInstance()

        val constraint = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val inputData: Data = mapOf("id" to userEntity.id).toWorkData()

        val localDeleteRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<TestLocalDeleteWorker>()
                .setInputData(inputData)
                .build()

        val networkDeleteRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<TestNetworkDeleteWorker>()
                .setConstraints(constraint)
                .setInputData(inputData)
                .build()

        workManager.beginWith(localDeleteRequest)
                .then(networkDeleteRequest)
                .enqueue()

        val status = workManager.getStatusById(networkDeleteRequest.id)
                .observe(this, Observer {
                    if (it != null && it.state.isFinished) {
                        //DELETE FROM DATABASE COMPLETE
                    }
                })
    }

    private inner class TestLocalDeleteWorker : Worker() {

        override fun doWork(): WorkerResult {
            deleteUserLocal()
            return WorkerResult.SUCCESS
        }

        private fun deleteUserLocal() {
            listViewModel.getUserById(inputData.getInt("id", -1)).observe(this@UserListFragment, Observer {
                listViewModel.deleteUserLocal(it!!)
            })
        }
    }

    private inner class TestNetworkDeleteWorker : Worker() {

        override fun doWork(): WorkerResult {
            deleteUserNetwork()
            return WorkerResult.SUCCESS
        }

        private fun deleteUserNetwork() {
            listViewModel.deleteUserServer(inputData.getInt("id", -1))
        }

    }

}