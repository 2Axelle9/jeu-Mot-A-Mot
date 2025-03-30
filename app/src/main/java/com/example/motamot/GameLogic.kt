package com.example.motamot

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Serializable
import kotlin.random.Random


enum class CharFeedback { CORRECT, PRESENT, ABSENT }

class GameLogic(context: Context) : Serializable {


    // On détermine le mot secret (le mot à deviner)
    private val words: List<String> = loadWords(context, "motsSimplesFR.txt") // On charge les mots du fichier
    val secretWord: String = words.random() // On choisit un mot aléatoire parmi les mots de la liste
    init {
        Log.i("axelle", secretWord)
    }


    private var attempts = 0 // Nombre de tentatives au débuts de la partie
    private val maxAttempts = 6 // Nombre maximum de tentatives possiblent


    /**Fonction qui vérifie le mot passé en paramètre et retourne une liste (feedback) de CharFeedback correspondant au résultat de son analyse pour chaque lettre du mot**/
    fun checkWord(guess: String): List<CharFeedback> {
        val feedback = MutableList(secretWord.length) { CharFeedback.ABSENT }

        // On compte le nombre d'occurrences de chaque caractère dans le mot secret
        val secretCharCount = secretWord.groupingBy { it }.eachCount().toMutableMap()


        // On compare les lettres du mot de la tentative avec celles du mot secret
        for (i in guess.indices) {
            // Si la lettre est présente dans le mot secret et est bien placée, on met le feedback à CORRECT et on décrémente le compteur de la lettre dans le mot secret
            if (guess[i] == secretWord[i]) {
                feedback[i] = CharFeedback.CORRECT
                secretCharCount[guess[i]] = secretCharCount[guess[i]]!! - 1
            }
            // Si la lettre est présente mais mal placée, on met le feedback à PRESENT et on décrémente le compteur de la lettre dans le mot secret
            else if (feedback[i] == CharFeedback.ABSENT && secretCharCount.getOrDefault(guess[i], 0) > 0) {
                feedback[i] = CharFeedback.PRESENT
                secretCharCount[guess[i]] = secretCharCount[guess[i]]!! - 1
            }
        }

        attempts++
        return feedback
    }

    /*** Fonction qui charge les mots du fichier passé en paramètre et les retourne sous forme de liste **/
    fun loadWords(context: Context, filename: String): List<String> {
        val words = mutableListOf<String>()
        try {
            val inputStream = context.assets.open(filename) // On récupère le fichier dans le dossier assets
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.useLines { lines ->
                lines.forEach { word ->
                    if (word.length == 5 && word.all { it.isLetter() }) { // On ne garde que les mots de 5 lettres qui sont bien tous composés de lettres de l'alphabet
                        words.add(word.uppercase()) // Les mots sont convertis majuscule
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return words
    }

    /*** Fonction qui vérifie si le mot passé en paramètre est le mot secret **/
    fun isWordGuessed(guess: String): Boolean {
        return guess == secretWord
    }

    /*** Fonction qui vérifie si la partie est terminée **/
    fun isGameOver(): Boolean {
        return attempts >= maxAttempts
    }
}