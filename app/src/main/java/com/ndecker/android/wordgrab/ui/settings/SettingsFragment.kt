package com.ndecker.android.wordgrab.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

import com.ndecker.android.wordgrab.R
import kotlinx.android.synthetic.main.main_fragment.view.*

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_screen, rootKey)
        findPreference<EditTextPreference>("gameTime")?.apply{
            summaryProvider = Preference.SummaryProvider<EditTextPreference> { preference ->
                if(preference.text.isEmpty()) "Not Set" else preference.text + " seconds"
            }
            setOnBindEditTextListener { editText ->
                editText.inputType = InputType.TYPE_CLASS_NUMBER
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        TODO("Not yet implemented")
    }

}
