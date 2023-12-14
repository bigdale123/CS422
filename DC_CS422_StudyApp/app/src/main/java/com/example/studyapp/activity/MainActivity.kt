package com.example.studyapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.studyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Creates the Main Activity
    // Handler loads the bookActivity after a delay, making mainActivity behave as a loading screen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            val intent = Intent(this, BooksActivity::class.java)
            startActivity(intent)
            finish()
        }, 3500)
    }

    // Used to make database name accessible throughout activities
    companion object {
        const val DATABASE_NAME = "app_database"
    }
}