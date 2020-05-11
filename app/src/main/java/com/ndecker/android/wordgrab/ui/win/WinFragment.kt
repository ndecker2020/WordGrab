package com.ndecker.android.wordgrab.ui.win

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.ndecker.android.wordgrab.R

class WinFragment : Fragment() {

    private lateinit var winnerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_win, container, false)
        winnerTextView = view.findViewById(R.id.winnerTextView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            winnerTextView.text = WinFragmentArgs.fromBundle(it).winner + " Wins!"
        }
    }

}
