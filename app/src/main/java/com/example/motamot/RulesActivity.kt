package com.example.motamot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RulesActivity : AppCompatActivity() {

    private lateinit var btnBackRules : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_rules)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnBackRules = findViewById(R.id.btnBackRules)
        btnBackRules.setOnClickListener {
            ouvrirAccueilActivity()
        }
    }

    fun ouvrirAccueilActivity() {
        // lancer Activity Accueil
        val intent = Intent(this, AccueilActivity::class.java)
        startActivity(intent)
    }
}