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
import com.jakewharton.rxbinding2.view.RxView
import com.nsnik.nrs.kotlintest.R
import com.nsnik.nrs.kotlintest.viewModel.UserListViewModel
import com.nsnik.nrs.kotlintest.views.adapters.UserListAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_list.*

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
        compositeDisposable.add(RxView.clicks(homeAdd).subscribe({

        }))
    }

}