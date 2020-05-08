package com.ndecker.android.wordgrab.customList

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.ndecker.android.wordgrab.R

class CustomListFragment : Fragment() {
    private lateinit var categorySpinner: Spinner
    private lateinit var wordField: EditText
    private lateinit var submitWord: Button

    companion object {
        fun newInstance() = CustomListFragment()
    }

    private lateinit var viewModel: CustomListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.custom_list_fragment, container, false)

        categorySpinner = view.findViewById(R.id.category_spinner) as Spinner
        wordField = view.findViewById(R.id.enter_word) as EditText
        submitWord = view.findViewById(R.id.submit_word) as Button

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CustomListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
