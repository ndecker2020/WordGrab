package com.ndecker.android.wordgrab.gameUi

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener2
import android.hardware.SensorManager
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
import androidx.preference.PreferenceManager
import com.ndecker.android.wordgrab.MainActivity

import com.ndecker.android.wordgrab.R
import com.ndecker.android.wordgrab.ui.main.MainFragment
import com.ndecker.android.wordgrab.ui.main.MainFragmentDirections
import kotlinx.android.synthetic.main.main_fragment.*
import kotlin.math.abs
import kotlin.random.Random


class GameFragment: Fragment(), SensorEventListener2{
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

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private val shakeSensitivity = 30.0
    private var lastShake: Long = 0

    private var shakeToSkip = false

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

        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        shakeToSkip = sharedPref.getBoolean(getString(R.string.shake_enabled_key), false)
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
            nextWord()
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

    private fun nextWord(){
        if(viewModel.wordsStack.isEmpty()) {
            viewModel.wordsLiveData.observe(viewLifecycleOwner,
                Observer { newWordList ->
                    viewModel.setUpList(newWordList.shuffled())
                })
        }
        viewModel.currentWord = viewModel.wordsStack.pop().word
        wordText.text = viewModel.currentWord
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

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //unused
    }

    override fun onFlushCompleted(sensor: Sensor?) {
        //unused
    }

    override fun onSensorChanged(event: SensorEvent?) {
        //when the accelerometer has a change
        //return if shaking is disabled
        if(!shakeToSkip) return
        //return if something is empty
        if(event?.sensor == null) return
        //if no magnitudes are more than the threshold then ignore the motion
        if(abs(event.values[0]) < shakeSensitivity && abs(event.values[1]) < shakeSensitivity && abs(event.values[2]) < shakeSensitivity) return
        //ignore if previous shake was within a second
        if(System.currentTimeMillis() - lastShake < 1000) return
        lastShake = System.currentTimeMillis()
        nextWord()
    }
}