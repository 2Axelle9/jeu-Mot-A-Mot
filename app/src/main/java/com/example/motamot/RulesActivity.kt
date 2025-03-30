package com.example.motamot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RulesActivity : AppCompatActivity() {

    private lateinit var navigator: Navigator
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

        navigator = Navigator(this) // On initialise le navigateur

        btnBackRules = findViewById(R.id.btnBackRules)
        btnBackRules.setOnClickListener {
            val fromMainActivity = intent.getBooleanExtra("fromMainActivity", false)
            // Si on vient de MainActivity, on réutilise l'instance de MainActivity qui existe déjà
            if (fromMainActivity) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
            } else { // Sinon on retourne à la page précédente
                finish()
            }
        }
    }
}