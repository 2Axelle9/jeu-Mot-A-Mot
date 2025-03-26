package com.example.motamot

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import java.nio.file.Files.size



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

    // Cette fonction initialise la grille en définissant les champs editText de chaque ligne (= tentative) dans une liste
    private fun setupEditTexts() {
        // Initialisation de la grille d'EditText
        editTextGrid = listOf(
            listOf(findViewById(R.id.etA1Letter1), findViewById(R.id.etA1Letter2), findViewById(R.id.etA1Letter3), findViewById(R.id.etA1Letter4), findViewById(R.id.etA1Letter5)),
            listOf(findViewById(R.id.etA2Letter1), findViewById(R.id.etA2Letter2), findViewById(R.id.etA2Letter3), findViewById(R.id.etA2Letter4), findViewById(R.id.etA2Letter5)),
            listOf(findViewById(R.id.etA3Letter1), findViewById(R.id.etA3Letter2), findViewById(R.id.etA3Letter3), findViewById(R.id.etA3Letter4), findViewById(R.id.etA3Letter5)),
            listOf(findViewById(R.id.etA4Letter1), findViewById(R.id.etA4Letter2), findViewById(R.id.etA4Letter3), findViewById(R.id.etA4Letter4), findViewById(R.id.etA4Letter5)),
            listOf(findViewById(R.id.etA5Letter1), findViewById(R.id.etA5Letter2), findViewById(R.id.etA5Letter3), findViewById(R.id.etA5Letter4), findViewById(R.id.etA5Letter5)),
            listOf(findViewById(R.id.etA6Letter1), findViewById(R.id.etA6Letter2), findViewById(R.id.etA6Letter3), findViewById(R.id.etA6Letter4), findViewById(R.id.etA6Letter5)))

        // Initialiser le focus sur le premier EditText
        editTextGrid[0][0].requestFocus()


        // Ajout du TextWatcher pour chaque EditText
        for (rowIndex in editTextGrid.indices) {
            for (colIndex in editTextGrid[rowIndex].indices) {
                val editText = editTextGrid[rowIndex][colIndex]
                editText.addTextChangedListener(object : TextWatcher {

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        // Implémentation vide
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        Log.e("axelle", "onTextChanged")
                        // Si le texte est rempli (taille 1) et ce n'est pas le dernier champ de la ligne
                        if (s.length == 1) {
                            // Déplace le focus vers le champ suivant après un léger délai
                            editTextGrid[rowIndex][colIndex + 1].post {
                                editTextGrid[rowIndex][colIndex + 1].requestFocus()
                            }
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {
                        // Implémentation vide
                    }
                })
            }
        }
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

        if (gameLogic.isWordGuessed(guess) || gameLogic.isGameOver()) {
            val resultMessage: String = when {
                gameLogic.isWordGuessed(guess) -> "Trouvé !"
                gameLogic.isGameOver() -> "Dommage"
                else -> "Erreur"
            }
            val intent = Intent(this, EndGameActivity::class.java).apply {
                putExtra("resultMessage", resultMessage)
                putExtra("secretWord", gameLogic.secretWord)
            }
            startActivity(intent)
        }

        else {
            currentAttempt++
        }
    }

    // Cette fonction affiche la correction de la tentative de l'utilisateur
    private fun updateGridUI(feedback: List<CharFeedback>) {
        for (i in feedback.indices) {
            val editText = editTextGrid[currentAttempt][i]
            when (feedback[i]) {
                CharFeedback.CORRECT -> editText.setBackgroundResource(R.color.correct)
                CharFeedback.PRESENT -> editText.setBackgroundResource(R.color.present)
                CharFeedback.ABSENT -> editText.setBackgroundResource(R.color.absent)
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