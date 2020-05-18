package com.ndecker.android.wordgrab.gameUi

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

import com.ndecker.android.wordgrab.R
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
    private lateinit var skipButton: Button
    private var teamOneScore =0
    private var teamTwoScore = 0
    private lateinit var hintTextView: TextView
    private lateinit var viewModel: GameViewModel
    private var navigateAway = false

    private var timesUp: Boolean=true

    private var timeLeft: Long = 10000
    private var countDownTime:Long = timeLeft



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
        skipButton = view.findViewById(R.id.skip_button)
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

        viewModel.wordsLiveData.observe(viewLifecycleOwner,
            Observer {
                newWord -> viewModel.currentWord = newWord[Random.nextInt(0, newWord.size)].word
            })

        viewModel.categoriesLiveData.observe(viewLifecycleOwner,
            Observer { categories -> arguments
            })

        viewModel.hintLiveData.observe(viewLifecycleOwner, Observer {
            if(it == null || it.definitions.isNullOrEmpty()) return@Observer
            hintTextView.text = it.definitions.first().definition
        })
    }

    override fun onStart() {
        super.onStart()
        teamOnePoints.text= getString(R.string.team_1_points,teamOneScore)
        teamTwoPoints.text= getString(R.string.team_2_points,teamTwoScore)

        nextWordButton.setOnClickListener{

            viewModel.wordsLiveData.observe(viewLifecycleOwner,
                Observer {
                        newWord -> viewModel.currentWord = newWord[Random.nextInt(0, newWord.size)].word
                })
            wordText.text = viewModel.currentWord
        }

        nextRoundButton.apply {
            isEnabled = timesUp
            setOnClickListener {
                restartRound()

            }
        }

        skipButton.setOnClickListener {
            viewModel.wordsLiveData.observe(viewLifecycleOwner,
                Observer {
                        newWord -> viewModel.currentWord = newWord[Random.nextInt(0, newWord.size)].word
                })
            wordText.text = viewModel.currentWord
        }

        hintButton.setOnClickListener {
            //TODO use the current word here
            viewModel.loadDefinition(viewModel.currentWord)
        }

    }

    fun restartRound(){
        timer.start()
        timesUp = false
        nextWordButton.isEnabled =true
        skipButton.isEnabled = true
        hintButton.isEnabled = true
        nextRoundButton.isEnabled = false

    }

    fun finishedTimer(){
        timesUp=true
        nextWordButton.isEnabled=false
        skipButton.isEnabled=false
        hintButton.isEnabled=false
        teamOnePoints.isEnabled=true
        teamTwoPoints.isEnabled=true


        teamOnePoints.setOnClickListener {
            teamOneScore += 1
            teamOnePoints.text= getString(R.string.team_1_points,teamOneScore)
            teamOnePoints.isEnabled=false
            teamTwoPoints.isEnabled=false
            nextRoundButton.isEnabled = true

        }
        teamTwoPoints.setOnClickListener {
            teamTwoScore += 1
            teamTwoPoints.text= getString(R.string.team_2_points,teamTwoScore)
            teamOnePoints.isEnabled=false
            teamTwoPoints.isEnabled=false
            nextRoundButton.isEnabled = true
        }
        Toast.makeText(context,"time is up",Toast.LENGTH_SHORT).show()
        timeLeft = 10000
        countDownTime = timeLeft

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
    }

    //just call with the name of the winning team.
    private fun onTeamWin(team: String){
        val action = GameFragmentDirections.actionGameFragmentToWinFragment(team)
        findNavController().navigate(action)
    }

    private var timer:CountDownTimer =
        object : CountDownTimer(timeLeft, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
            }

            override fun onFinish() {
                finishedTimer()
            }


        }
}