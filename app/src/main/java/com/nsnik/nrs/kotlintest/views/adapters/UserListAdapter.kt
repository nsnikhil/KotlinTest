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

package com.nsnik.nrs.kotlintest.views.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.nsnik.nrs.kotlintest.R
import com.nsnik.nrs.kotlintest.data.UserEntity
import com.twitter.serial.stream.Serial
import com.twitter.serial.stream.bytebuffer.ByteBufferSerial
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.single_list_item.view.*

class UserListAdapter(private val context: Context?, private var userList: List<UserEntity>) : RecyclerView.Adapter<UserListAdapter.MyViewHolder>() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val userData: UserEntity? = userList[position]
        holder.itemName?.text = userData?.name
        holder.itemPhone?.text = userData?.phone.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_list_item, parent, false))
    }

    fun updateList(newList: List<UserEntity>) {
        userList = newList
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView? = itemView.itemName
        val itemPhone: TextView? = itemView.itemPhone
        val item: ConstraintLayout? = itemView.listItem

        init {
            val serial: Serial = ByteBufferSerial()
            val byteArray: ByteArray = serial.toByteArray(userList[adapterPosition], UserEntity.SERIALIZER)
            val bundle = Bundle()
            bundle.putByteArray(context?.resources?.getString(R.string.bundleKeyUserEntity), byteArray)
            compositeDisposable.add(RxView.clicks(itemView).subscribe({ run { itemView.findNavController().navigate(R.id.listToDetails, bundle) } }))
        }

    }

}