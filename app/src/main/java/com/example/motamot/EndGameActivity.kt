package com.example.motamot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.PreferenceManager

class EndGameActivity : AppCompatActivity() {

    private lateinit var navigator: Navigator
    private lateinit var gameLogic: GameLogic

    private lateinit var tvResult : TextView
    private lateinit var tvSecretWord : TextView
    private lateinit var btnPlay : Button
    private lateinit var btnStop : Button
    private lateinit var tvActualScore : TextView

    lateinit var capteurManager: CapteurManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_end_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        navigator = Navigator(this) // On initialise le navigateur

        gameLogic = intent.getSerializableExtra("gameLogic") as GameLogic // On récupère l'objet gameLogic qui a été envoyé à la page

        tvResult = findViewById(R.id.tvResult)
        tvActualScore = findViewById(R.id.tvActualScore)
        btnPlay = findViewById(R.id.btnPlay)

        capteurManager = CapteurManager(this)

        // On regarde si le mot a été trouvé ou non
        if (gameLogic.isGameOver()) {
            tvResult.text = "Dommage"
            tvActualScore.text = "Votre score\n $score"
            btnPlay.text = "Rejouer"
            btnPlay.setOnClickListener {
                score = 0
                navigator.navigateTo(MainActivity::class.java) // On retourne à la page de jeu
            }
        } else {
            score += 1
            tvResult.text = "Trouvé !"
            tvActualScore.text = "Votre score actuel\n $score"
            btnPlay.text = "Continuer"
            btnPlay.setOnClickListener {
                navigator.navigateTo(MainActivity::class.java) // On retourne à la page de jeu
            }
        }

        // On affiche le mot qui était à deviner
        tvSecretWord = findViewById(R.id.tvSecretWord)
        tvSecretWord.text = gameLogic.secretWord

        btnStop = findViewById(R.id.btnStop)
        btnStop.setOnClickListener {
            score = 0
            navigator.navigateTo(AccueilActivity::class.java)
        }

    }



    /**********************/
    /*******Capteur*******/
    /**********************/

    // On démarre les capteurs quand l'activité est en premier plan
    override fun onResume() {
        super.onResume()
        capteurManager.start()
    }

    // On arrête les capteurs quand l'activité est en arrière plan
    override fun onPause() {
        super.onPause()
        capteurManager.stop()
    }

    // Si le téléphone est secoué, on rejoue ou on continue
    fun motionDetected() {
        navigator.navigateTo(MainActivity::class.java) // On retourne à la page de jeu
    }




}