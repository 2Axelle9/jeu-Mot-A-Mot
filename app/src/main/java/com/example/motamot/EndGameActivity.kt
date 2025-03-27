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

    private lateinit var gameLogic: GameLogic

    private lateinit var tvResult : TextView
    private lateinit var tvSecretWord : TextView
    private lateinit var btnPlay : Button
    private lateinit var btnStop : Button

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

        gameLogic = intent.getSerializableExtra("gameLogic") as GameLogic // On récupère l'objet gameLogic qui a été envoyé à la page

        tvResult = findViewById(R.id.tvResult)
        btnPlay = findViewById(R.id.btnPlay)

        capteurManager = CapteurManager(this)

        // On regarde si le mot a été trouvé ou non
        if (gameLogic.isGameOver()) {
            tvResult.text = "Dommage"
            btnPlay.text = "Rejouer"
            btnPlay.setOnClickListener {
                rejouer()
            }
        } else {
            tvResult.text = "Trouvé !"
            btnPlay.text = "Continuer"
            btnPlay.setOnClickListener {
                continuer()
            }
        }

        // On affiche le mot qui était à deviner
        tvSecretWord = findViewById(R.id.tvSecretWord)
        tvSecretWord.text = gameLogic.secretWord

        btnStop = findViewById(R.id.btnStop)
        btnStop.setOnClickListener {
            ouvrirAccueilActivity()
        }

    }

    fun rejouer() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun continuer() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun ouvrirAccueilActivity() {
        val intent = Intent(this, AccueilActivity::class.java)
        startActivity(intent)
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
            if (gameLogic.isGameOver()) {
                rejouer()
            } else {
                continuer()
            }
    }



    /************************************************/
    /*******Sauvegarde de la plus longue suite*******/
    /************************************************/
    override fun onStop() {
        //avant de fermer l'application on cherche le fichier de préférence pour l'éditer
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = preferences.edit()

        //ajouter le couple "Score" avec sa valeur actuelle
        editor.putString("score", bestScore.toString())

        //enregistrer le fichier de préférérence
        editor.apply()

        super.onStop()
    }

}