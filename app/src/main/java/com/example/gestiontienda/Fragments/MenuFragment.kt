package com.example.gestiontienda.Fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.gestiontienda.Adapters.RecyclerCopiaAdapter
import com.example.gestiontienda.Interfaces.OnCopiaListClicked
import com.example.gestiontienda.Interfaces.OnItemListClicked
import com.example.gestiontienda.R
import com.example.gestiontienda.Utilidades.DatabaseHelper
import com.example.gestiontienda.Utilidades.ExcelHelper
import com.example.gestiontienda.Utilidades.Prefs
import com.example.gestiontienda.Utilidades.Utilidades.Companion.listaArchivosCopia
import org.apache.poi.sl.draw.geom.Context


class MenuFragment() : Fragment(), OnCopiaListClicked {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var archivoCopiaSeguridad: String
    private lateinit var excelUtilities : ExcelHelper
    private lateinit var textoArchivoCopiaSeguridad : TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_menu, container, false)

        // Instanciar Base de Datos SQLite
        databaseHelper = DatabaseHelper(activity!!.applicationContext)
        db = databaseHelper.writableDatabase

        // Excel Utilities
        excelUtilities = ExcelHelper()

        // Comprobar primera ejecución
        comprobarPrimeraEjecucion()

        val botonTienda = v.findViewById(R.id.ajustes_tienda) as Button
        botonTienda.setOnClickListener {
            ajustesTienda(v)
        }

        val botonCopiaSeguridad = v.findViewById(R.id.botonCopiaSeguridad) as Button
        botonCopiaSeguridad.setOnClickListener {

            // Dialog para poder cargar o guardar copia de seguridad
            // la copia de seguridad se guardará en 'Descargas'

            val dialog = Dialog(v.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.copia_seguridad_dialog)

            val botonCargar = dialog.findViewById(R.id.cargar_copia) as Button
            val botonSalvar = dialog.findViewById(R.id.guardar_copia) as Button

            botonCargar.setOnClickListener {
                // cargar archivo de copia de seguridad
                // recycler view con lista de copias guardadas internamente en la APP
                // Botón para cargar un archivo de copia de seguridad en cualquier carpeta
                val dialogLista = Dialog(v.context)
                dialogLista.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialogLista.setCancelable(true)
                dialogLista.setContentView(R.layout.cargar_copia_dialog)

                val recyclerLista = dialogLista.findViewById(R.id.recycler_copiaRV) as RecyclerView
                val botonSeleccionar =
                    dialogLista.findViewById(R.id.seleccionarArchivoCopiaBTN) as Button
                val botonRestaurarCopia = dialogLista.findViewById(R.id.restaurarCopiaBTN) as Button
                textoArchivoCopiaSeguridad = dialogLista.findViewById(R.id.archivoSeleccionadoTV) as TextView

                recyclerLista.setHasFixedSize(true)
                recyclerLista.layoutManager = LinearLayoutManager(v.context)

                // Recuperar lista de archivos de copia de seguridad en la memoria interna
                var listaArchivos: MutableList<String> = mutableListOf()
                listaArchivos = listaArchivosCopia()

                val adapterArchivo = RecyclerCopiaAdapter()
                adapterArchivo.RecyclerCopiaAdapter(listaArchivos, this, textoArchivoCopiaSeguridad)
                recyclerLista.adapter = adapterArchivo

                botonSeleccionar.setOnClickListener {
                    // Request code for selecting a ZIP document.
                    val PICK_ZIP_FILE = 2
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "application/zip"
                        // Optionally, specify a URI for the file that should appear in the
                        // system file picker when it loads.
                        putExtra(DocumentsContract.EXTRA_INITIAL_URI, 2)
                    }
                    startActivityForResult(intent, PICK_ZIP_FILE)
                }

                botonRestaurarCopia.setOnClickListener {
                    val dialogConfirmar = Dialog(v.context)
                    dialogConfirmar.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialogConfirmar.setCancelable(true)
                    dialogConfirmar.setContentView(R.layout.confirmar_layout)

                    val textoConfirmar = dialogConfirmar.findViewById(R.id.texto_confirmarTV) as TextView
                    val aceptarConfirmar = dialogConfirmar.findViewById(R.id.boton_confirmar_OK) as Button
                    val cancelarConfirmar = dialogConfirmar.findViewById(R.id.boton_confirmar_CANCELAR) as Button

                    textoConfirmar.text = "¿Seguro? "

                    aceptarConfirmar.setOnClickListener{
                        // Restaurar copia de seguridad con el archivo seleccionado
                        excelUtilities.restaurarCopia(archivoCopiaSeguridad, v.context, db)
                        Toast.makeText(context, "Copia Restaurada", Toast.LENGTH_LONG).show()
                        dialogConfirmar.dismiss()
                        dialogLista.dismiss()
                        dialog.dismiss()
                    }

                    cancelarConfirmar.setOnClickListener {
                        // Cancelar
                        dialogConfirmar.dismiss()
                        dialog.dismiss()
                    }
                    dialogConfirmar.show()
                }
                dialogLista.show()
            }

            botonSalvar.setOnClickListener {

                val miExcel = excelUtilities.createWorkbook(databaseHelper)
                excelUtilities.createExcelFile(miExcel, v.context.applicationContext)
                Toast.makeText(
                    v.context,
                    "Copia Creada, el archivo se ha guardado en 'Descargas'",
                    Toast.LENGTH_LONG
                ).show()

                // TODO mandar un mail a la dirección indicada en ajustes con el archivo de copia de seguridad
                dialog.dismiss()
            }

            dialog.show()
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

            databaseHelper.guardarTienda(
                db,
                nombreTienda.text.toString(),
                direccionTienda.text.toString(),
                telefonoTienda.text.toString(),
                emailTienda.text.toString(),
                cifTienda.text.toString(),
                otrosTienda.text.toString()
            )
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

            Log.d("Miapp", "Primera Ejecución")
        }
    }


    override fun itemListClicked(archivo: String) {
        // Guardar el path del archivo que hemos seleccionado
        textoArchivoCopiaSeguridad.text = archivo
        archivoCopiaSeguridad = "/data/user/0/com.example.gestiontienda/app_Copia/" + archivo
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?
    ) {
        if (requestCode == 2
            && resultCode == Activity.RESULT_OK
        ) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            resultData?.data?.also { uri ->
                // Perform operations on the document using its URI.
                var path = uri.lastPathSegment
                val pos = path!!.indexOf(':') + 1
                path = path.substring(pos, path.length)
                Log.d("Miapp", "Has seleccionado el archivo: " + path)
                archivoCopiaSeguridad = path
                textoArchivoCopiaSeguridad.text = path

            }
        }
    }
}