package com.example.gestiontienda.Interfaces

import android.view.View
import android.widget.TextView


interface OnCopiaListClicked {
    fun itemListClicked (archivo: String, v: TextView)
}