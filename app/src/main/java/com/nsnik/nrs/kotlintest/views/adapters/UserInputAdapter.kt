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
import android.graphics.drawable.Drawable
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.nsnik.nrs.kotlintest.R
import com.nsnik.nrs.kotlintest.data.UserEntity
import kotlinx.android.synthetic.main.single_details_image.view.*
import kotlinx.android.synthetic.main.single_input_user_item.view.*
import java.util.*


class UserInputAdapter constructor(private val context: Context?, val userEntity: UserEntity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            1 -> modifyInput(inputTextViewHolder.inputText, resource?.getString(R.string.dialogNameHint), InputType.TYPE_CLASS_TEXT,
                    (ContextCompat.getDrawable(context!!, R.drawable.ic_outline_account_circle_24px)), position = 1)

            2 -> modifyInput(inputTextViewHolder.inputText, resource?.getString(R.string.dialogAgeHint), InputType.TYPE_CLASS_NUMBER,
                    (ContextCompat.getDrawable(context!!, R.drawable.ic_outline_terrain_24px)), position = 2)

            3 -> modifyInput(inputTextViewHolder.inputText, resource?.getString(R.string.dialogPhoneHint), InputType.TYPE_CLASS_PHONE,
                    (ContextCompat.getDrawable(context!!, R.drawable.ic_outline_call_24px)), position = 3)

            4 -> modifyInput(inputTextViewHolder.inputText, resource?.getString(R.string.dialogAddressHint), InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS,
                    (ContextCompat.getDrawable(context!!, R.drawable.ic_outline_place_24px)), position = 4)

            5 -> modifyInput(inputTextViewHolder.inputText, resource?.getString(R.string.dialogEmailHint), InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
                    (ContextCompat.getDrawable(context!!, R.drawable.ic_outline_mail_outline_24px)), position = 5)

            6 -> modifyInput(inputTextViewHolder.inputText, resource?.getString(R.string.dialogDateHint), InputType.TYPE_CLASS_DATETIME,
                    (ContextCompat.getDrawable(context!!, R.drawable.ic_outline_date_range_24px)), false, position = 6)
        }
    }

    private fun modifyInput(inputEditText: TextInputEditText, hint: String?, inputType: Int, drawable: Drawable?, isFocusable: Boolean = true, position: Int) {
        inputEditText.hint = hint
        inputEditText.inputType = inputType
        inputEditText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        inputEditText.isFocusableInTouchMode = isFocusable
        RxTextView.textChanges(inputEditText).subscribe({ setText(position, it.toString()) })
    }

    private fun setText(position: Int, value: String) {
        when (position) {
            1 -> userEntity.name = value
            2 -> userEntity.age = if (value != "") value.toInt() else 0
            3 -> userEntity.phone = if (value != "") value.toDouble() else 0.0
            4 -> userEntity.address = value
            5 -> userEntity.email = value
            6 -> userEntity.date = value
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

        init {
            RxView.clicks(itemView).subscribe { userEntity.avatar = "http://takeaseathome.com//media//images//avatar-placeholder.png" }
        }
    }

    inner class InputTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val inputText: TextInputEditText = itemView.inputDialogText

        init {
            RxView.clicks(inputText).subscribe { inputText.setText(Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault()).time.toString()) }
        }
    }
}