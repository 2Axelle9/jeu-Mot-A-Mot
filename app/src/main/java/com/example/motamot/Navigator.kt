package com.example.motamot

import android.content.Context
import android.content.Intent

class Navigator (private val context: Context) {

    /**Fonction qui permet de naviguer vers une autre activité, elle prend en paramètre n'importe quelle classe (*) **/
    fun navigateTo(targetActivity: Class<*>) {
        val intent = Intent(context, targetActivity)
        context.startActivity(intent)
    }
}