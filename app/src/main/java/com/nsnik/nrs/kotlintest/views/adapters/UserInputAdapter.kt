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
import android.content.res.Resources
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.jakewharton.rxbinding2.view.RxView
import com.nsnik.nrs.kotlintest.R
import kotlinx.android.synthetic.main.single_details_image.view.*
import kotlinx.android.synthetic.main.single_input_user_item.view.*
import timber.log.Timber

class UserInputAdapter constructor(private val context: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val INPUT_TYPE_IMAGE: Int = 0
        private const val INPUT_TYPE_EDIT_TEXT: Int = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == INPUT_TYPE_IMAGE)
            ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.single_details_image, parent, false))
        else
            InputTextViewHolder(LayoutInflater.from(context).inflate(R.layout.single_input_user_item, parent, false))
    }

    override fun getItemCount(): Int {
        return 7
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (position) {
            0 -> bindImage(holder as ImageViewHolder)
            else -> bindInputFields(holder as InputTextViewHolder, position)
        }
    }

    private fun bindImage(imageViewHolder: ImageViewHolder) {
        imageViewHolder.inputImage.setImageResource(R.drawable.ic_outline_person_24px)
    }

    private fun bindInputFields(inputTextViewHolder: InputTextViewHolder, position: Int) {
        val resource: Resources? = context?.resources
        when (position) {
            1 -> {
                inputTextViewHolder.inputText.hint = resource?.getString(R.string.dialogNameHint)
                inputTextViewHolder.inputText
                        .setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context!!, R.drawable.ic_outline_account_circle_24px), null, null, null)
            }
            2 -> {
                inputTextViewHolder.inputText.hint = resource?.getString(R.string.dialogAgeHint)
                inputTextViewHolder.inputText.inputType = InputType.TYPE_CLASS_NUMBER
                inputTextViewHolder.inputText
                        .setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context!!, R.drawable.ic_outline_terrain_24px), null, null, null)
            }
            3 -> {
                inputTextViewHolder.inputText.hint = resource?.getString(R.string.dialogPhoneHint)
                inputTextViewHolder.inputText.inputType = InputType.TYPE_CLASS_PHONE
                inputTextViewHolder.inputText
                        .setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context!!, R.drawable.ic_outline_call_24px), null, null, null)
            }
            4 -> {
                inputTextViewHolder.inputText.hint = resource?.getString(R.string.dialogAddressHint)
                inputTextViewHolder.inputText.inputType = InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
                inputTextViewHolder.inputText
                        .setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context!!, R.drawable.ic_outline_place_24px), null, null, null)
            }
            5 -> {
                inputTextViewHolder.inputText.hint = resource?.getString(R.string.dialogEmailHint)
                inputTextViewHolder.inputText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                inputTextViewHolder.inputText
                        .setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context!!, R.drawable.ic_outline_mail_outline_24px), null, null, null)
            }
            6 -> {
                inputTextViewHolder.inputText.hint = resource?.getString(R.string.dialogDateHint)
                inputTextViewHolder.inputText.isEnabled = false
                inputTextViewHolder.inputText.inputType = InputType.TYPE_CLASS_DATETIME
                inputTextViewHolder.inputText
                        .setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
        }
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

        init {
            when (adapterPosition) {
                6 -> RxView.clicks(inputText).subscribe { Timber.d("Test") }
            }
        }
    }
}