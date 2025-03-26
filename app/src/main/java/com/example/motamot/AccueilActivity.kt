package com.example.motamot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AccueilActivity : AppCompatActivity() {

    private lateinit var btnStart : Button
    private lateinit var btnRules : Button
    private lateinit var btnCredits : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_accueil)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnStart = findViewById(R.id.btnStart)
        btnStart.setOnClickListener {
            ouvrirMainActivity()
        }

        btnRules = findViewById(R.id.btnRules)
        btnRules.setOnClickListener {
            ouvrirRulesActivity()
        }

        btnCredits = findViewById(R.id.btnCredits)
        btnCredits.setOnClickListener {
            ouvrirCreditsActivity()
        }
    }

    fun ouvrirMainActivity() {
        // lancer Activity Main
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun ouvrirRulesActivity() {
        // lancer Activity Rules
        val intent = Intent(this, RulesActivity::class.java)
        startActivity(intent)
    }

    fun ouvrirCreditsActivity() {
        // lancer Activity Credits
        val intent = Intent(this, CreditsActivity::class.java)
        startActivity(intent)
    }
}