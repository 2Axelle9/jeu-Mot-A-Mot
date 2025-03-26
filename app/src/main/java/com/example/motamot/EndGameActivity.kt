package com.example.motamot

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EndGameActivity : AppCompatActivity() {

    private lateinit var tvResult : TextView
    private lateinit var tvSecretWord : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_end_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // On affiche le résultat
        tvResult = findViewById(R.id.tvResult)
        tvResult.text = intent.getStringExtra("resultMessage") // On récupère les message de résultat qui a été envoyé à la page

        // On affiche le mot secret
        tvSecretWord = findViewById(R.id.tvSecretWord)
        tvSecretWord.text = intent.getStringExtra("secretWord") // On récupère le mot secret qui a été envoyé à la page
    }
}