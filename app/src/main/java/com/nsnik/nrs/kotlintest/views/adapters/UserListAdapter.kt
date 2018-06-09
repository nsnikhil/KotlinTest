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
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.jakewharton.rxbinding2.view.RxView
import com.nsnik.nrs.kotlintest.R
import com.nsnik.nrs.kotlintest.data.UserEntity
import com.nsnik.nrs.kotlintest.utils.events.UserDeleteEvent
import com.nsnik.nrs.kotlintest.views.adapters.DiffUtil.UserDiffUtil
import com.twitter.serial.stream.Serial
import com.twitter.serial.stream.bytebuffer.ByteBufferSerial
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.single_list_item.view.*
import org.greenrobot.eventbus.EventBus

class UserListAdapter : PagedListAdapter<UserEntity, RecyclerView.ViewHolder>(UserDiffUtil()) {

    private lateinit var context: Context
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val userData: UserEntity? = getItem(position)
        (holder as MyViewHolder).itemName?.text = userData?.name
        holder.itemPhone?.text = String.format("%.0f", userData?.phone)
        Glide.with(context).load(userData?.avatar)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions().placeholder(R.drawable.circle).error(R.drawable.circle))
                .into(holder.itemImage!!)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView? = itemView.itemName
        val itemPhone: TextView? = itemView.itemPhone
        val itemImage: ImageView? = itemView.itemImage

        init {
            compositeDisposable.add(RxView.clicks(itemView).subscribe({
                val bundle = Bundle()
                val serial: Serial = ByteBufferSerial()
                val byteArray: ByteArray = serial.toByteArray(getItem(adapterPosition), UserEntity.SERIALIZER)
                bundle.putByteArray(context.resources.getString(R.string.bundleKeyUserEntity), byteArray)
                itemView.findNavController().navigate(R.id.listToDetails, bundle)
            }))

            compositeDisposable.add(RxView.longClicks(itemView).subscribe(Consumer {
                val popUp = PopupMenu(context, itemView, Gravity.END)
                popUp.menuInflater.inflate(R.menu.list_pop_up_menu, popUp.menu)

                popUp.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menuPopUpListEdit -> itemClick(Actions.EDIT, adapterPosition)
                        R.id.menuPopUpListDelete -> itemClick(Actions.DELETE, adapterPosition)
                        else -> false
                    }
                }

                popUp.show()
            }))
        }
    }

    private fun itemClick(action: Actions, position: Int): Boolean {
        when (action) {
            Actions.EDIT -> {
            }
            Actions.DELETE -> EventBus.getDefault().post(UserDeleteEvent(getItem(position)!!))
        }
        return true
    }

    private fun cleanUp() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        cleanUp()
    }

}

enum class Actions {
    DELETE, EDIT
}