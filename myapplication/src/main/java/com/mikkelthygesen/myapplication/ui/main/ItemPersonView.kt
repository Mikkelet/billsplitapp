package com.mikkelthygesen.myapplication.ui.main

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mikkelthygesen.myapplication.databinding.ItemPersonBinding

class ItemPersonView(
    context: Context
) :
    ConstraintLayout(context),
    TextWatcher {

    private var binding: ItemPersonBinding
    private lateinit var person: Person
    private lateinit var onChange: (Float) -> Unit

    init {
        binding = ItemPersonBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setValues(person: Person, onChange: (Float) -> Unit) {
        this.person = person
        this.onChange = onChange
        binding.textViewName.text = person.name
        binding.editTextDebt.addTextChangedListener(this)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        try {
            person.owed = p0.toString().toFloat()
            onChange(person.owed)
        } catch (e: Exception) {
            println(e)
        }
    }

    override fun afterTextChanged(p0: Editable?) {

    }
}