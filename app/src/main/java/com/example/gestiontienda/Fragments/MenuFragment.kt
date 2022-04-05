package com.example.gestiontienda.Fragments

import android.app.Dialog
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.gestiontienda.R
import com.example.gestiontienda.Utilidades.DatabaseHelper
import com.example.gestiontienda.Utilidades.ExcelHelper
import com.example.gestiontienda.Utilidades.Prefs


class MenuFragment  () : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_menu, container, false)

        // Instanciar Base de Datos SQLite
        databaseHelper = DatabaseHelper(activity!!.applicationContext)
        db = databaseHelper.writableDatabase

        // Comprobar primera ejecución
        comprobarPrimeraEjecucion()

        val botonTienda = v.findViewById(R.id.ajustes_tienda) as Button
        botonTienda.setOnClickListener {
            ajustesTienda(v)
        }

        val botonCopiaSeguridad = v.findViewById(R.id.botonCopiaSeguridad) as Button
        botonCopiaSeguridad.setOnClickListener {

            val excelUtilities = ExcelHelper()
            val miExcel = excelUtilities.createWorkbook()
            excelUtilities.createExcelFile(miExcel , v.context.applicationContext)
            excelUtilities.updateCell(v.context.applicationContext)

        }


        return v
    }

    fun ajustesTienda(v: View) {

        val miTienda = databaseHelper.obtenerTienda(db)

        val dialog = Dialog(v.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.crear_tienda)

        val nombreTienda = dialog.findViewById(R.id.nombreTiendaET) as EditText
        val direccionTienda = dialog.findViewById(R.id.direccionTiendaET) as EditText
        val telefonoTienda = dialog.findViewById(R.id.telefonoTiendaET) as EditText
        val emailTienda = dialog.findViewById(R.id.emailTiendaET) as EditText
        val cifTienda = dialog.findViewById(R.id.cifTiendaET) as EditText
        val otrosTienda = dialog.findViewById(R.id.otrosdatosTiendaET) as EditText

        nombreTienda.setText(miTienda.nombreTienda)
        direccionTienda.setText(miTienda.direccionTienda)
        telefonoTienda.setText(miTienda.telefonoTienda)
        emailTienda.setText(miTienda.emailTienda)
        cifTienda.setText(miTienda.cifTienda)
        otrosTienda.setText(miTienda.otrosDatosTienda)

        val yesBtn = dialog.findViewById(R.id.boton_guardar_tienda) as Button
        yesBtn.setOnClickListener {

            databaseHelper.guardarTienda(db, nombreTienda.text.toString(), direccionTienda.text.toString() , telefonoTienda.text.toString() , emailTienda.text.toString() , cifTienda.text.toString() , otrosTienda.text.toString())
            dialog.dismiss()
        }
        dialog.show()
    }

    fun comprobarPrimeraEjecucion() {
        val prefs = Prefs(activity!!.applicationContext)
        if (prefs.primeraEjecucion!!) {
            prefs.primeraEjecucion = false

            val vista = activity!!.findViewById(R.id.pager) as ViewPager2
            ajustesTienda(vista)

            Log.d("Miapp" , "Primera Ejecución")
        }
    }

}