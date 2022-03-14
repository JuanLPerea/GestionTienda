package com.example.gestiontienda.Entidades

import android.app.Dialog
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import com.example.gestiontienda.R
import com.example.gestiontienda.Utilidades.DatabaseHelper

class Tienda (nombreTienda : String , direccionTienda : String , telefonoTienda : String, emailTienda : String, cifTienda : String, otrosDatosTienda : String ) {
/*
        TIENDA:    NOMBRE_TIENDA
                   DIRECCION
                   TELEFONO
                   EMAIL
                   CIF
                   OTROS_DATOS
 */

    var nombreTienda : String
    var direccionTienda : String
    var telefonoTienda : String
    var emailTienda : String
    var cifTienda : String
    var otrosDatosTienda : String

    init {
        this.nombreTienda = nombreTienda
        this.direccionTienda = direccionTienda
        this.telefonoTienda = telefonoTienda
        this.emailTienda = emailTienda
        this.cifTienda = cifTienda
        this.otrosDatosTienda = otrosDatosTienda
    }


}