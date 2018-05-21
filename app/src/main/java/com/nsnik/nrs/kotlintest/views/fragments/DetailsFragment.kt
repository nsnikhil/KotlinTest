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
import com.nsnik.nrs.kotlintest.R
import com.nsnik.nrs.kotlintest.data.UserEntity
import com.twitter.serial.stream.Serial
import com.twitter.serial.stream.bytebuffer.ByteBufferSerial
import timber.log.Timber


class DetailsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_details, container, false)
        initialize()
        return view
    }

    private fun initialize() {
        val serial: Serial = ByteBufferSerial()
        val userEntity: UserEntity? = serial.fromByteArray(arguments?.getByteArray(activity?.resources?.getString(R.string.bundleKeyUserEntity)), UserEntity.SERIALIZER)
        Timber.d(userEntity?.name)
    }

}
