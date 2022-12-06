package com.mikkelthygesen.myapplication.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.mikkelthygesen.myapplication.R
import com.mikkelthygesen.myapplication.databinding.FragmentMainBinding

class MainFragment : Fragment(), PeopleAdapter.Listener {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding
    private val peopleAdapter = PeopleAdapter(this)
    private val overviewAdapter = DebtOverviewAdapter()
    private val people = mutableListOf<Person>()
    private val shared = Person("SHARED", 0f)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewPeople.adapter = peopleAdapter
        binding.button.setOnClickListener { addPerson() }
        binding.recyclerViewOverview.adapter = overviewAdapter
        addPersonView(shared)
    }

    private fun addPerson(){
        val person = Person("Person ${people.size + 1}", 0f)
        people.add(person)
        peopleAdapter.updateList(people)
        overviewAdapter.updateList(people, shared)
        addPersonView(person)
    }

    private fun addPersonView(person: Person){
        val personView = ItemPersonView(requireContext())
        personView.setValues(person) {
            overviewAdapter.notifyUpdate()
        }
        binding.linearLayout2.addView(personView)
    }

    override fun onValueChange(value: String) {
        overviewAdapter.notifyUpdate()
    }


    companion object {
        fun newInstance() = MainFragment()
    }
}