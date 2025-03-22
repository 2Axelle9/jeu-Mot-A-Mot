package com.example.motamot

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var gameLogic: GameLogic
    private lateinit var editTextGrid: List<List<EditText>>
    private var currentAttempt = 0
    private val MAX_ATTEMPTS = 6
    private val WORD_LENGTH = 5
    private lateinit var btnValidate : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        gameLogic = GameLogic("APPLE") // Exemple de mot à deviner
        setupEditTexts()


        btnValidate = findViewById(R.id.btnValidate)

        btnValidate.setOnClickListener {
            validateGuess()
        }

    }

    // Cette fonction initialise la grille en définissant les champs editText de chaque ligne dans une liste
    private fun setupEditTexts() {
        editTextGrid = listOf(
            listOf(findViewById(R.id.etA1Letter1), findViewById(R.id.etA1Letter2), findViewById(R.id.etA1Letter3), findViewById(R.id.etA1Letter4), findViewById(R.id.etA1Letter5)),
            listOf(findViewById(R.id.etA2Letter1), findViewById(R.id.etA2Letter2), findViewById(R.id.etA2Letter3), findViewById(R.id.etA2Letter4), findViewById(R.id.etA2Letter5)),
            listOf(findViewById(R.id.etA3Letter1), findViewById(R.id.etA3Letter2), findViewById(R.id.etA3Letter3), findViewById(R.id.etA3Letter4), findViewById(R.id.etA3Letter5)),
            listOf(findViewById(R.id.etA4Letter1), findViewById(R.id.etA4Letter2), findViewById(R.id.etA4Letter3), findViewById(R.id.etA4Letter4), findViewById(R.id.etA4Letter5)),
            listOf(findViewById(R.id.etA5Letter1), findViewById(R.id.etA5Letter2), findViewById(R.id.etA5Letter3), findViewById(R.id.etA5Letter4), findViewById(R.id.etA5Letter5)),
            listOf(findViewById(R.id.etA6Letter1), findViewById(R.id.etA6Letter2), findViewById(R.id.etA6Letter3), findViewById(R.id.etA6Letter4), findViewById(R.id.etA6Letter5))
        )
    }


    // Cette fonction est appelée à chaque tentative de l'utilisateur
    private fun validateGuess() {
        if (currentAttempt >= MAX_ATTEMPTS) return

        // On récupère le mot entré par l'utilisateur
        val guess = editTextGrid[currentAttempt].joinToString("") { it.text.toString().uppercase() }

        // On vérifie que le mot a la bonne longueur
        if (guess.length != WORD_LENGTH) {
            Toast.makeText(this, "Entrez un mot de $WORD_LENGTH lettres", Toast.LENGTH_SHORT).show()
            return
        }

        // On vérifie la tentative (dans GameLogic)
        val feedback = gameLogic.checkWord(guess)

        // Afficher la correction de la tentative
        updateGridUI(feedback)

        // Vérifier si le mot est trouvé ou si la partie est terminée
        if (gameLogic.isWordGuessed(guess)) {
            Toast.makeText(this, "Bravo ! 🎉", Toast.LENGTH_LONG).show()
            disableInputs()
        } else if (gameLogic.isGameOver()) {
            Toast.makeText(this, "Perdu ! Le mot était ${gameLogic.secretWord}", Toast.LENGTH_LONG).show()
            disableInputs()
        } else {
            currentAttempt++
        }
    }

    // Cette fonction affiche la correction de la tentative de l'utilisateur
    private fun updateGridUI(feedback: List<CharFeedback>) {
        for (i in feedback.indices) {
            val editText = editTextGrid[currentAttempt][i]
            when (feedback[i]) {
                CharFeedback.CORRECT -> editText.setBackgroundResource(R.color.green)
                CharFeedback.PRESENT -> editText.setBackgroundResource(R.color.yellow)
                CharFeedback.ABSENT -> editText.setBackgroundResource(R.color.gray)
            }
        }
    }

    // Cette fonction désactive tous les champs editText après la fin de la partie
    private fun disableInputs() {
        for (row in editTextGrid) {
            for (editText in row) {
                editText.isEnabled = false
            }
        }
    }
}