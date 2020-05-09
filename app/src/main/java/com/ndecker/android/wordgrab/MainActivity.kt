package com.ndecker.android.wordgrab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ndecker.android.wordgrab.categoryDatabase.CategoryRepository
import com.ndecker.android.wordgrab.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        CategoryRepository.initialize(this)
    }
}
