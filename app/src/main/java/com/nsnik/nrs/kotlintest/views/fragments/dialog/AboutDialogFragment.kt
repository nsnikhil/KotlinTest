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

package com.nsnik.nrs.kotlintest.views.fragments.dialog


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.jakewharton.rxbinding2.view.RxView
import com.nsnik.nrs.kotlintest.R
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_about_dialog.*


class AboutDialogFragment : DialogFragment() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() {
        aboutSourceCode.movementMethod = LinkMovementMethod.getInstance()
        aboutNikhil.movementMethod = LinkMovementMethod.getInstance()
        compositeDisposable.addAll(
                RxView.clicks(aboutLibraries).subscribe({ startActivity(Intent(activity, OssLicensesMenuActivity::class.java)) }),
                RxView.clicks(aboutLicense).subscribe({ chromeCustomTab(activity?.resources?.getString(R.string.aboutLicense)) })
        )
    }

    private fun chromeCustomTab(url: String?) {
        val builder = CustomTabsIntent.Builder()
        if (activity != null) {
            builder.setToolbarColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
            builder.setSecondaryToolbarColor(ContextCompat.getColor(activity!!, R.color.colorPrimaryDark))
            builder.setExitAnimations(activity!!, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(activity!!, Uri.parse(url))
        }
    }

    private fun cleanUp() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        cleanUp()
    }

}