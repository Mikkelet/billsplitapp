package com.mikkelthygesen.myapplication.ui.main

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.mikkelthygesen.myapplication.databinding.ItemPersonBinding

class PeopleAdapter(val listener: Listener) : RecyclerView.Adapter<PeopleAdapter.ViewHolder>() {

    interface Listener {
        fun onValueChange(value: String)
    }

    private var people = emptyList<Person>()

    inner class ViewHolder(private val binding: ItemPersonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(person: Person, index: Int) {
            binding.textViewName.text = person.name
            binding.editTextDebt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(chars: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    listener.onValueChange(chars.toString())
                    try {
                        person.owed = chars.toString().toFloat()
                        notifyItemChanged(index)
                    } catch (e: Exception) {
                        Log.e(javaClass.simpleName, e.toString())
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
        }
    }

    fun updateList(newPeople: List<Person>) {
        people = newPeople
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = people[position]
        holder.bind(person, position)
    }

    override fun getItemCount(): Int = people.size
}