package ru.hivislav.shoppinglistapp.presentation

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import ru.hivislav.shoppinglistapp.R

@BindingAdapter("errorInputName")
fun bindErrorInputName(textInputLayout: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        textInputLayout.context.getString(R.string.error_name_text_input_shop_item)
    } else {
        null
    }
    textInputLayout.error = message
}

@BindingAdapter("errorInputCount")
fun bindErrorInputCount(textInputLayout: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        textInputLayout.context.getString(R.string.error_count_text_input_shop_item)
    } else {
        null
    }
    textInputLayout.error = message
}