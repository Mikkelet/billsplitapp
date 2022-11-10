package com.mikkelthygesen.myapplication.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mikkelthygesen.myapplication.databinding.ItemPersonDebtBinding

class DebtOverviewAdapter : RecyclerView.Adapter<DebtOverviewAdapter.ViewHolder>() {

    private var people = emptyList<Person>()
    private lateinit var shared: Person

    inner class ViewHolder(private val binding: ItemPersonDebtBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(person: Person) {
            val sharedDebt = shared.owed / people.size
            binding.textViewDebt.text = "${person.name}: ${person.owed + sharedDebt}"
        }
    }

    fun updateList(newPeople: List<Person>, shared: Person) {
        people = newPeople
        this.shared = shared
        notifyDataSetChanged()
    }

    fun notifyUpdate() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = ItemPersonDebtBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = people[position]
        holder.bind(person)
    }

    override fun getItemCount(): Int = people.size
}