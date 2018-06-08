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
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.jakewharton.rxbinding2.view.RxView
import com.nsnik.nrs.kotlintest.R
import com.nsnik.nrs.kotlintest.data.UserEntity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.single_detail_item.view.*
import kotlinx.android.synthetic.main.single_details_image.view.*

class UserDetailAdapter constructor(private val context: Context?, private val userEntity: UserEntity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val DETAILS_IMAGE: Int = 0
        private const val DETAILS_TEXT: Int = 1
    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DETAILS_IMAGE)
            ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.single_details_image, parent, false))
        else
            TextViewHolder(LayoutInflater.from(context).inflate(R.layout.single_detail_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            Glide.with(context!!).load(userEntity.avatar)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(RequestOptions().placeholder(R.drawable.circle).error(R.drawable.circle))
                    .into((holder as ImageViewHolder).detailsImage)
        } else
            bindText((holder as TextViewHolder).detailsText, position)
    }

    private fun bindText(textView: TextView, position: Int) {
        when (position) {
            1 -> setValue(textView, userEntity.id.toString(), ContextCompat.getDrawable(context!!, R.drawable.ic_outline_person_24px))
            2 -> setValue(textView, userEntity.name, ContextCompat.getDrawable(context!!, R.drawable.ic_outline_person_24px))
            3 -> setValue(textView, userEntity.age.toString(), ContextCompat.getDrawable(context!!, R.drawable.ic_outline_terrain_24px))
            4 -> setValue(textView, String.format("%.0f", userEntity.phone), ContextCompat.getDrawable(context!!, R.drawable.ic_outline_call_24px))
            5 -> setValue(textView, userEntity.address, ContextCompat.getDrawable(context!!, R.drawable.ic_outline_place_24px))
            6 -> setValue(textView, userEntity.email, ContextCompat.getDrawable(context!!, R.drawable.ic_outline_mail_outline_24px))
            7 -> setValue(textView, userEntity.date, ContextCompat.getDrawable(context!!, R.drawable.ic_outline_date_range_24px))
        }
    }

    private fun setValue(textView: TextView, value: String?, drawable: Drawable?) {
        textView.apply {
            text = value
            setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }
    }

    override fun getItemCount(): Int {
        return 7
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            DETAILS_IMAGE
        else
            DETAILS_TEXT
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val detailsImage: ImageView = itemView.detailsImage
    }

    inner class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val detailsText: TextView = itemView.detailsText

        init {
            compositeDisposable.add(RxView.clicks(detailsText).subscribe({ context?.toast(detailsText.text.toString()) }))
        }

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