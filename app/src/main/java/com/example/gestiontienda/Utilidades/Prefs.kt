package com.example.gestiontienda.Utilidades

import android.content.Context
import android.content.SharedPreferences

class Prefs (context : Context){
    val PREFS_NAME = "gestiontienda.sharedpreferences"
    val SHARED_NAME = "primera_ejecucion"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)
    var primeraEjecucion: Boolean?
        get() = prefs.getBoolean(SHARED_NAME, true)
        set(value) = prefs.edit().putBoolean(SHARED_NAME, value!!).apply()

    var notificacion:String?
        get() = prefs.getString("NOTIFICACION", "")
        set(value) = prefs.edit().putString("NOTIFICACION" , value!!).apply()
}