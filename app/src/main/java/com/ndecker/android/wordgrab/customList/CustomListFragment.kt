package com.ndecker.android.wordgrab.customList

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import com.ndecker.android.wordgrab.Category
import com.ndecker.android.wordgrab.R
import com.ndecker.android.wordgrab.Word
import java.util.*

class CustomListFragment : Fragment() {
    private lateinit var categoryField: EditText
    private lateinit var categorySubmitButton: Button
    private lateinit var categorySpinner: Spinner
    private lateinit var wordField: EditText
    private lateinit var submitWord: Button

    private val categories = emptyList<Category>()

    companion object {
        fun newInstance() = CustomListFragment()
    }

    private lateinit var viewModel: CustomListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.custom_list_fragment, container, false)

        categoryField = view.findViewById(R.id.category_edit_text) as EditText
        categorySubmitButton = view.findViewById(R.id.submit_category_button) as Button
        categorySpinner = view.findViewById(R.id.category_spinner) as Spinner
        wordField = view.findViewById(R.id.enter_word) as EditText
        submitWord = view.findViewById(R.id.submit_word) as Button

        categorySpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
        //categorySpinner.onItemSelectedListener = this

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(CustomListViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.categoriesLiveData.observe(viewLifecycleOwner,
            Observer { categories ->
                categorySpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories.map { it.name })
            })
    }

    override fun onStart() {
        super.onStart()
        submitWord.setOnClickListener {
            val theWord = wordField.text.toString()
            if (theWord == ""){
                Toast.makeText(context, "No Word was typed in", Toast.LENGTH_SHORT).show()
            } else {
                val theCategory = categorySpinner.selectedItem.toString()
                if (theCategory == "") {
                    Toast.makeText(context, "No Category was selected", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.addWord(Word(UUID.randomUUID(), theWord, categorySpinner.selectedItem.toString()))
                    wordField.setText("")
                }
            }
        }

        categorySubmitButton.setOnClickListener {
            val theCategory = categoryField.text.toString()
            if (theCategory == "") {
                Toast.makeText(context, "No Category was typed in", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addCategory(Category(theCategory))
                categoryField.setText("")
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(CustomListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
