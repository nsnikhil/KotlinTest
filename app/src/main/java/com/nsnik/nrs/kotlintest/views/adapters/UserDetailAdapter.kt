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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nsnik.nrs.kotlintest.R
import com.nsnik.nrs.kotlintest.data.UserEntity
import kotlinx.android.synthetic.main.single_detail_item.view.*
import kotlinx.android.synthetic.main.single_details_image.view.*

class UserDetailAdapter constructor(private val context: Context, private val userEntity: UserEntity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val DETAILS_IMAGE: Int = 0
        private const val DETAILS_TEXT: Int = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DETAILS_IMAGE)
            ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.single_details_image, parent, false))
        else
            TextViewHolder(LayoutInflater.from(context).inflate(R.layout.single_detail_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0)
            Glide.with(context).load(userEntity.avatar).into((holder as ImageViewHolder).detailsImage)
        else
            bindText((holder as TextViewHolder).detailsText, position)
    }

    private fun bindText(textView: TextView, position: Int) {
        when (position) {
            1 -> textView.text = userEntity.id.toString()
            2 -> textView.text = userEntity.name
            3 -> textView.text = userEntity.age.toString()
            4 -> textView.text = userEntity.phone.toString()
            5 -> textView.text = userEntity.address
            6 -> textView.text = userEntity.email
            7 -> textView.text = userEntity.date
        }
    }

    override fun getItemCount(): Int {
        return 8
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
    }

}