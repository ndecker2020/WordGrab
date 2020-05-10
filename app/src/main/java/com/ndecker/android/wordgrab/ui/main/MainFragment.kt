package com.ndecker.android.wordgrab.ui.main

import android.database.DataSetObserver
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ndecker.android.wordgrab.Category
import com.ndecker.android.wordgrab.R

class MainFragment : Fragment(), AdapterView.OnItemSelectedListener{

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private lateinit var playButton: Button
    private lateinit var settingsButton: Button
    private lateinit var createAListButton: Button
    private lateinit var instructionsButton: Button
    private lateinit var playersSpinner: Spinner
    private lateinit var categorySpinner: Spinner

    private val players = listOf(2,4,6,8,10,12)
    private val categories = emptyList<Category>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        playButton = view.findViewById(R.id.playButton)
        settingsButton = view.findViewById(R.id.settingsButton)
        createAListButton = view.findViewById(R.id.customListButton)
        instructionsButton = view.findViewById(R.id.instructionsButton)
        playersSpinner = view.findViewById(R.id.playersSpinner)
        categorySpinner = view.findViewById(R.id.categorySpinner)

        playersSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, players)
        playersSpinner.onItemSelectedListener = this

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
            val players = (viewModel.selectedPlayers+1)*2
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
            playersSpinner ->
                viewModel.selectedPlayers = -1
            else ->
                viewModel.selectedCategory = -1
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //number of players changed
        if(viewModel.selectedPlayers != 0 || viewModel.selectedCategory != 0)
            playButton.isEnabled = true
        view ?: return
        when(parent){
            playersSpinner ->
                viewModel.selectedPlayers = position
            else ->
                viewModel.selectedCategory = position
        }
    }
}
