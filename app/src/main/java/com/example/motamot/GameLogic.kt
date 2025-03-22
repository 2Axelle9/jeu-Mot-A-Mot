package com.example.motamot

import kotlin.random.Random


enum class CharFeedback { CORRECT, PRESENT, ABSENT }


class GameLogic(val secretWord: String) {

    private var attempts = 0
    private val maxAttempts = 6

    // Fonction qui vérifie le mot passé en paramètre et retourne une liste (feedback) de CharFeedback correspondant au résultat de son analyse pour chaque lettre du mot
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

    fun isWordGuessed(guess: String): Boolean {
        return guess == secretWord
    }

    fun isGameOver(): Boolean {
        return attempts >= maxAttempts
    }
}