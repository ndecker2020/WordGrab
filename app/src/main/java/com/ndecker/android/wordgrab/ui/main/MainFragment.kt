package com.ndecker.android.wordgrab.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ndecker.android.wordgrab.Category
import com.ndecker.android.wordgrab.R
import java.util.*

class MainFragment : Fragment(), AdapterView.OnItemSelectedListener{

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private lateinit var playButton: Button
    private lateinit var settingsButton: Button
    private lateinit var createAListButton: Button
    private lateinit var instructionsButton: Button
    private lateinit var scoreSpinner: Spinner
    private lateinit var categorySpinner: Spinner

    private val categories = emptyList<Category>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        playButton = view.findViewById(R.id.playButton)
        settingsButton = view.findViewById(R.id.settingsButton)
        createAListButton = view.findViewById(R.id.customListButton)
        instructionsButton = view.findViewById(R.id.instructionsButton)
        scoreSpinner = view.findViewById(R.id.scoreSpinner)
        categorySpinner = view.findViewById(R.id.categorySpinner)

        scoreSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listOf(2,3,4,5,6,7,8,9,10))
        scoreSpinner.onItemSelectedListener = this

        categorySpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
        categorySpinner.onItemSelectedListener = this
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.categoriesLiveData.observe(viewLifecycleOwner,
            Observer {categories ->
                categorySpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories.map { it.name })
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        categorySpinner.setSelection(viewModel.selectedCategory)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        playButton.setOnClickListener {
            val players = (viewModel.selectedScore+2)
            val category = viewModel.categoriesLiveData.value!![viewModel.selectedCategory].name
            val action = MainFragmentDirections.actionMainFragmentToGameFragment(category, players)
            findNavController().navigate(action)
        }

        createAListButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_customListFragment)
        }

        settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
        }

        instructionsButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_instructions2)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        playButton.isEnabled = false
        when(parent){
            scoreSpinner ->
                viewModel.selectedScore = -1
            else ->
                viewModel.selectedCategory = -1
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //number of players changed
        if(viewModel.selectedScore != 0 || viewModel.selectedCategory != 0)
            playButton.isEnabled = true
        view ?: return
        when(parent){
            scoreSpinner ->
                viewModel.selectedScore = position
            else ->
                viewModel.selectedCategory = position
        }
    }
}
