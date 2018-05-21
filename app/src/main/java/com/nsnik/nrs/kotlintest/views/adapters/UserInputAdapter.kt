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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.nsnik.nrs.kotlintest.R
import kotlinx.android.synthetic.main.single_details_image.view.*
import kotlinx.android.synthetic.main.single_input_user_item.view.*

class UserInputAdapter constructor(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val INPUT_TYPE_IMAGE: Int = 0
        private const val INPUT_TYPE_EDIT_TEXT: Int = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == INPUT_TYPE_IMAGE)
            ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.single_details_image, parent, false))
        else
            InputTextViewHolder(LayoutInflater.from(context).inflate(R.layout.user_input_fragment, parent, false))
    }

    override fun getItemCount(): Int {
        return 8
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            INPUT_TYPE_IMAGE
        else
            INPUT_TYPE_EDIT_TEXT
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val inputImage: ImageView = itemView.detailsImage
    }

    inner class InputTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val inputText: TextInputEditText = itemView.inputDialogText
    }

}