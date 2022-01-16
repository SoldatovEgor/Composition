package ru.soldatov.android.composition.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.soldatov.android.composition.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}