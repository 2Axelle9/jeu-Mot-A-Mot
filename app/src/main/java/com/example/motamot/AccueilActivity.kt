package com.example.motamot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.PreferenceManager

var score : Int = 0;
var bestScore : Int = 0

class AccueilActivity : AppCompatActivity() {

    private lateinit var btnStart : Button
    private lateinit var btnRules : Button
    private lateinit var btnCredits : Button
    private lateinit var tvBestScore : TextView


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


        // On cherche le fichier de préférence dans lequel la valeur de la plus longue suite de mot est stockée
        val preferences = PreferenceManager.getDefaultSharedPreferences (this)
        // extraire la valeur
        val saveScore = preferences. getString ("bestScore", "")
        // si sa valeur n'est pas égale à "" (qui est la valeur par défault)
        if (!saveScore. equals("")) {
            // alors on initialise la variable bestWordSequence avec cette valeur
            bestScore = saveScore?.toInt()!!
        } else {
            bestScore = 0
        }
        tvBestScore = findViewById(R.id.tvBestScore)
        tvBestScore.setText("Meilleur score\n" + bestScore.toString() + " mots de suite")
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