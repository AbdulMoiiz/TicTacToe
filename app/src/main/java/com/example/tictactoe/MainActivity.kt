package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var dataBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dataBinding.playBtn.setOnClickListener {
            dataBinding.playBtn.visibility = View.INVISIBLE
            dataBinding.singlePlayBtn.visibility = View.VISIBLE
            dataBinding.multiPlayBtn.visibility = View.VISIBLE
        }
        dataBinding.singlePlayBtn.setOnClickListener {
            val intent = Intent(this, GameScreen::class.java)
            intent.putExtra("singlePlayer", true)
            intent.putExtra("multiPlayer", false)
            startActivity(intent)
        }
        dataBinding.multiPlayBtn.setOnClickListener {
            val intent = Intent(this, GameScreen::class.java)
            intent.putExtra("singlePlayer", false)
            intent.putExtra("multiPlayer", true)
            startActivity(intent)
        }
    }
}