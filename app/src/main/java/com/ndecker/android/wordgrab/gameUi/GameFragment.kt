package com.ndecker.android.wordgrab.gameUi

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ndecker.android.wordgrab.MainActivity

import com.ndecker.android.wordgrab.R
import com.ndecker.android.wordgrab.ui.main.MainFragment
import com.ndecker.android.wordgrab.ui.main.MainFragmentDirections
import kotlinx.android.synthetic.main.main_fragment.*
import kotlin.random.Random


class GameFragment: Fragment() {
    private lateinit var nextRoundButton: Button
    private lateinit var nextWordButton: Button
    private lateinit var hintButton: Button
    private lateinit var wordText: TextView
    private lateinit var teamOnePoints: Button
    private lateinit var teamTwoPoints: Button

    private var teamOneScore =0
    private var teamTwoScore = 0
    private lateinit var hintTextView: TextView
    private lateinit var viewModel: GameViewModel
    private var navigateAway = false
    private  var scoreLimit=0
    private var timesUp: Boolean=true

    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.game_fragment, container, false)
        nextRoundButton = view.findViewById(R.id.next_round_button)
        nextWordButton = view.findViewById(R.id.next_word_button)
        hintButton = view.findViewById(R.id.hint_button)
        wordText = view.findViewById(R.id.word_view)
        teamOnePoints=view.findViewById(R.id.team_one_points)
        teamTwoPoints=view.findViewById(R.id.team_two_points)

        hintTextView = view.findViewById(R.id.hint_text)

        return view

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(GameViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO use game arguments from main menu
        //here is how to get them, maybe save them in the viewModel?
        arguments?.let {
            viewModel.setUpWords(GameFragmentArgs.fromBundle(requireArguments()).category)
            Log.d("GameFragment", "Category: " + viewModel.selectedCategoryString)
           // Log.d("GameFragment", "Players: " + GameFragmentArgs.fromBundle(requireArguments()).players)

        }
        scoreLimit = GameFragmentArgs.fromBundle(requireArguments()).score*2


        viewModel.wordsLiveData.observe(viewLifecycleOwner,
            Observer { newWord ->
                viewModel.setUpList(newWord)
                viewModel.currentWord = viewModel.wordsStack.pop().word
                wordText.text = viewModel.currentWord
            })

        viewModel.categoriesLiveData.observe(viewLifecycleOwner,
            Observer { categories -> arguments
            })

        viewModel.hintLiveData.observe(viewLifecycleOwner, Observer {
            if(it == null || it.definitions.isNullOrEmpty()) {
                hintTextView.text = ""
                return@Observer
            }
            hintTextView.text = it.definitions.first().definition
        })
    }

    override fun onStart() {
        super.onStart()



        teamOnePoints.text= getString(R.string.team_1_points,teamOneScore)
        teamTwoPoints.text= getString(R.string.team_2_points,teamTwoScore)

        nextWordButton.setOnClickListener{
            if(viewModel.wordsStack.isEmpty()) {
                viewModel.wordsLiveData.observe(viewLifecycleOwner,
                    Observer { newWordList ->
                        viewModel.setUpList(newWordList.shuffled())
                    })
            }
            viewModel.currentWord = viewModel.wordsStack.pop().word
            wordText.text = viewModel.currentWord
        }

        nextRoundButton.apply {
            isEnabled = timesUp
            setOnClickListener {
                restartRound()
            }
        }


        hintButton.setOnClickListener {
            //TODO use the current word here
            viewModel.loadDefinition(viewModel.currentWord)
        }

    }

    private fun restartRound(){
        timer.start()
        timesUp = false
        nextWordButton.isEnabled =true
        hintButton.isEnabled = true
        nextRoundButton.isEnabled = false
    }

    fun finishedTimer(){
        timesUp=true
        nextWordButton.isEnabled=false
        hintButton.isEnabled=false
        teamOnePoints.isEnabled=true
        teamTwoPoints.isEnabled=true


        teamOnePoints.setOnClickListener {
            teamOneScore += 1
            if (teamOneScore>= scoreLimit){
                onTeamWin(getString(R.string.team_one_win))
            }
            teamOnePoints.text= getString(R.string.team_1_points,teamOneScore)
            teamOnePoints.isEnabled=false
            teamTwoPoints.isEnabled=false
            nextRoundButton.isEnabled = true

        }
        teamTwoPoints.setOnClickListener {
            teamTwoScore += 1
            if (teamTwoScore>=scoreLimit){
                onTeamWin(getString(R.string.team_two_win))
            }
            teamTwoPoints.text= getString(R.string.team_2_points,teamTwoScore)
            teamOnePoints.isEnabled=false
            teamTwoPoints.isEnabled=false
            nextRoundButton.isEnabled = true
        }
        Toast.makeText(context,"time is up",Toast.LENGTH_SHORT).show()



    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
        navigateAway=true

    }

    override fun onResume() {
        super.onResume()
        if (navigateAway) {
            Toast.makeText(context, "Round Ended because of screen navigation.", Toast.LENGTH_SHORT)
                .show()
        }
        restartRound()
    }
    override fun onStop() {
        super.onStop()
        timer.cancel()
        navigateAway = true
        viewModel.clearDefinition()
        hintTextView.text = ""
    }

    //just call with the name of the winning team.
    private fun onTeamWin(team: String){
        val action = GameFragmentDirections.actionGameFragmentToWinFragment(team)
        findNavController().navigate(action)
    }

    private var timer:CountDownTimer =
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                finishedTimer()
            }
        }
}