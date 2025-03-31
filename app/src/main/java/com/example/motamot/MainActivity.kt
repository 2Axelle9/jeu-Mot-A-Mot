package com.example.motamot

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
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
import androidx.preference.PreferenceManager
import java.nio.file.Files.size




class MainActivity : AppCompatActivity() {

    private lateinit var navigator: Navigator
    private lateinit var gameLogic: GameLogic
    private lateinit var editTextGrid: List<List<EditText>>
    private var currentAttempt = 0
    private val WORD_LENGTH = 5
    private lateinit var btnValidate : Button
    private lateinit var btnHelp : Button
    private lateinit var btnQuit : Button
    private val correctLetters = MutableList(WORD_LENGTH) { ' ' }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        navigator = Navigator(this) // On initialise le navigateur

        gameLogic = GameLogic(this) // On démarre une nouvelle partie

        // Préparation de la grille
        setupEditTexts()
        updateEditTextState()

        btnHelp = findViewById(R.id.btnHelp)
        btnHelp.setOnClickListener {
            //navigator.navigateTo(RulesActivity::class.java)
            val intent = Intent(this, RulesActivity::class.java)
            intent.putExtra("fromMainActivity", true) // On indique que l'activité d'origine est MainActivity
            startActivity(intent)
        }

        btnQuit = findViewById(R.id.btnQuit)
        btnQuit.setOnClickListener {
            navigator.navigateTo(AccueilActivity::class.java)
        }

        btnValidate = findViewById(R.id.btnValidate)
        btnValidate.setOnClickListener {
            validateGuess()
        }

    }

    /** Fonction qui initialise la grille en définissant les champs editText de chaque ligne (= tentative) dans une liste **/
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


        // On parcours tous les EditText de notre grille
        for (rowIndex in editTextGrid.indices) {
            for (colIndex in editTextGrid[rowIndex].indices) {
                val editText = editTextGrid[rowIndex][colIndex]

                // Passage à l'EditText suivant après avoit entré une seule lettre
                editText.addTextChangedListener(object : TextWatcher {

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        // Si le texte est rempli (taille 1) et ce n'est pas le dernier champ de la ligne on passe au suivant
                        if (s.length == 1 && colIndex < WORD_LENGTH - 1) {
                            editTextGrid[rowIndex][colIndex + 1].requestFocus()
                        }
                    }
                    override fun afterTextChanged(s: Editable) {
                    }

                })


                editText.setOnKeyListener { v, keyCode, event ->
                    // Validation de la tentative en appuyant sur la touche entrée
                    if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        validateGuess()
                        true
                    } else {
                        false
                    }
                    // Effacement du texte de la case précédente et déplacement à cette case précédent en appuyant sur la touche retour (effacer)
                    if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && editText.text.isEmpty() && colIndex > 0) {
                        editTextGrid[rowIndex][colIndex - 1].setText("")
                        editTextGrid[rowIndex][colIndex - 1].requestFocus()
                        true
                    } else {
                        false
                    }
                }
            }
        }
    }


    /** fonction appelée après chaque tentative pour la vérifier */
    private fun validateGuess() {
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

        // Si la partie est terminé on arrête la partie en passant à la page de fin de partie
        if (gameLogic.isWordGuessed(guess) || gameLogic.isGameOver(guess)) {
            val intent = Intent(this, EndGameActivity::class.java).apply {
                putExtra("gameLogic", gameLogic)
                putExtra("lastGuess", guess)
            }
            startActivity(intent)
        }
        else { // Sinon on passe à la tentative suivante
            currentAttempt++
            updateEditTextState()
        }
    }


    /** fonction qui affiche la correction de la tentative de l'utilisateur **/
    private fun updateGridUI(feedback: List<CharFeedback>) {
        for (i in feedback.indices) {
            val editText = editTextGrid[currentAttempt][i]
            when (feedback[i]) {
                CharFeedback.CORRECT -> {
                    editText.setBackgroundResource(R.color.correct)
                    correctLetters[i] = editText.text.toString()[0] //On enregistre la lettre correcte pour l'afficher à la prochaine tenttative
                }
                CharFeedback.PRESENT -> editText.setBackgroundResource(R.color.present)
                CharFeedback.ABSENT -> editText.setBackgroundResource(R.color.absent)
            }
        }
    }


    /** Fonction qui modifie l'état des EditText : éditable ou non, affiche les lettres trouvées et affiche la premère lettre du mot **/
    private fun updateEditTextState() {
        // Désactivation des champs pour que seul la ligne de la tentative actuelle soit modifiable
        for (rowIndex in editTextGrid.indices) {
            for (colIndex in editTextGrid[rowIndex].indices) {
                editTextGrid[rowIndex][colIndex].isEnabled = rowIndex == currentAttempt
                // Si la lettre correcte de cette position avait été trouvée lors des tenatives précédentes, on l'affiche
                if (rowIndex == currentAttempt && correctLetters[colIndex] != ' ') {
                    editTextGrid[rowIndex][colIndex].hint = correctLetters[colIndex].toString() // On affiche la lettre sous forme d'un "placeholder"
                }
            }
        }
        // Afficher la première lettre du mot à chaque tentative
        val firstLetter = gameLogic.secretWord[0].toString()
        val firstEditText = editTextGrid[currentAttempt][0]
        firstEditText.hint= firstLetter
    }


    /************************************************************************/
    /*******Sauvegarde du score (la plus longue suite de mots trouvés)*******/
    /************************************************************************/
    override fun onStop() {
        //avant de fermer l'application on cherche le fichier de préférence pour l'éditer
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = preferences.edit()

        Log.d("axelle", "bestScore = $bestScore")
        Log.d("axelle", "score = $score")
        if (score > bestScore) {
            bestScore = score
            //ajouter le couple "Score" avec sa valeur actuelle
            editor.putString("bestScore", bestScore.toString())

            //enregistrer le fichier de préférérence
            editor.apply()
            Log.d("axelle", "je sauvegarde le nouveau score $bestScore")
        }
        else{
            Log.d("axelle", "je n'enregistre pas de nouveau score car $score <= $bestScore")
        }
        super.onStop()
    }

}