package com.ndecker.android.wordgrab.gameUi

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.ndecker.android.wordgrab.R


class GameFragment: Fragment() {
    private lateinit var nextRoundButton: Button
    private lateinit var nextWordButton: Button
    private lateinit var hintButton: Button
    private lateinit var wordText: TextView
    private lateinit var teamOnePoints: TextView
    private lateinit var teamTwoPoints: TextView

    private var timesUp: Boolean=true
    private var countDownTime:Long = 10000



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
        teamOnePoints = view.findViewById(R.id.team_one)
        teamTwoPoints = view.findViewById(R.id.team_two_points)
        return view

    }

    override fun onStart() {
        super.onStart()
        nextWordButton.setOnClickListener{
            //get word from database and change wordText
            if (wordText.text =="oldWord"){
                wordText.text = "newWord"
            }
            else{
                wordText.text ="oldWord"
            }


        }
        nextRoundButton.apply {
            isEnabled = timesUp
            setOnClickListener {
                timesUp = false
                timer.start()

            }
        }

    }
    private var timer:CountDownTimer =
        object : CountDownTimer(countDownTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                timesUp=true
                Toast.makeText(context,"time is up",Toast.LENGTH_SHORT).show()



            }
        }
}