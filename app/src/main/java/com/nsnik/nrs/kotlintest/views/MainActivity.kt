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

package com.nsnik.nrs.kotlintest.views

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.test.espresso.IdlingResource
import com.nsnik.nrs.kotlintest.BuildConfig
import com.nsnik.nrs.kotlintest.MyApplication
import com.nsnik.nrs.kotlintest.R
import com.nsnik.nrs.kotlintest.utils.DatabaseUtil
import com.nsnik.nrs.kotlintest.utils.SimpleIdlingResource
import com.nsnik.nrs.kotlintest.utils.events.FetchListEvent
import com.nsnik.nrs.kotlintest.viewModel.UserListViewModel
import com.nsnik.nrs.kotlintest.views.fragments.dialog.AboutDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class MainActivity : AppCompatActivity() {

    private var mIdlingResource: SimpleIdlingResource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
    }

    private fun initialize() {
        setSupportActionBar(mainToolbar)
        if (checkConnection()) {
            ViewModelProviders.of(this).get(UserListViewModel::class.java).fetchDataFromServer()
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.mainNavHost).navigateUp()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.mainMenuAbout -> AboutDialogFragment().show(supportFragmentManager, "about")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkConnection(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetwork: NetworkInfo? = cm?.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun OnFetchListEvent(fetchListEvent: FetchListEvent) {
        val databaseUtil: DatabaseUtil = (this.applicationContext as MyApplication).databaseUtil
        fetchListEvent.userList.forEach { databaseUtil.insertUser(it) }
    }

    @VisibleForTesting
    @NonNull
    fun getIdlingResource(): IdlingResource {
        if (mIdlingResource == null) mIdlingResource = SimpleIdlingResource()
        return mIdlingResource as SimpleIdlingResource
    }

    override fun onDestroy() {
        super.onDestroy()
        if (BuildConfig.DEBUG) {
            val refWatcher = MyApplication.getRefWatcher(this)
            refWatcher?.watch(this)
        }
    }
}
