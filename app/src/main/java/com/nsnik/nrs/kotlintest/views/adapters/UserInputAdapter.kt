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

import android.app.DatePickerDialog
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
import com.nsnik.nrs.kotlintest.R
import com.nsnik.nrs.kotlintest.data.UserEntity
import kotlinx.android.synthetic.main.single_details_image.view.*
import kotlinx.android.synthetic.main.single_input_user_item.view.*
import timber.log.Timber
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
                    (ContextCompat.getDrawable(context!!, R.drawable.ic_outline_account_circle_24px)))

            2 -> modifyInput(inputTextViewHolder.inputText, resource?.getString(R.string.dialogAgeHint), InputType.TYPE_CLASS_NUMBER,
                    (ContextCompat.getDrawable(context!!, R.drawable.ic_outline_terrain_24px)))

            3 -> modifyInput(inputTextViewHolder.inputText, resource?.getString(R.string.dialogPhoneHint), InputType.TYPE_CLASS_PHONE,
                    (ContextCompat.getDrawable(context!!, R.drawable.ic_outline_call_24px)))

            4 -> modifyInput(inputTextViewHolder.inputText, resource?.getString(R.string.dialogAddressHint), InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS,
                    (ContextCompat.getDrawable(context!!, R.drawable.ic_outline_place_24px)))

            5 -> modifyInput(inputTextViewHolder.inputText, resource?.getString(R.string.dialogEmailHint), InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
                    (ContextCompat.getDrawable(context!!, R.drawable.ic_outline_mail_outline_24px)))

            6 -> modifyInput(inputTextViewHolder.inputText, resource?.getString(R.string.dialogDateHint), InputType.TYPE_CLASS_DATETIME,
                    (ContextCompat.getDrawable(context!!, R.drawable.ic_outline_date_range_24px)), false)
        }
    }

    private fun modifyInput(inputEditText: TextInputEditText, hint: String?, inputType: Int, drawable: Drawable?, isFocusable: Boolean = true) {
        inputEditText.hint = hint
        inputEditText.inputType = inputType
        inputEditText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        inputEditText.isFocusableInTouchMode = isFocusable
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
            RxView.clicks(itemView).subscribe { Timber.d("Image") }
        }
    }

    inner class InputTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val inputText: TextInputEditText = itemView.inputDialogText

        init {
            RxView.clicks(inputText).subscribe { showDatePicker(inputText) }
        }
    }

    private fun showDatePicker(inputEditText: TextInputEditText) {

        val datePickerListener = DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDay ->
            val year = selectedYear.toString()
            val month = (selectedMonth + 1).toString()
            val day = selectedDay.toString()
            inputEditText.setText("$day/$month/$year")
        }

        val cal = Calendar.getInstance()
        val datePicker = DatePickerDialog(context, datePickerListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
        datePicker.setCancelable(false)
        datePicker.setTitle("Select the date")
        datePicker.show()
    }
}