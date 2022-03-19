package com.example.gestiontienda.Fragments

import android.app.Dialog
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.gestiontienda.Adapters.RecyclerAdapter
import com.example.gestiontienda.Entidades.Producto
import com.example.gestiontienda.R
import com.example.gestiontienda.Utilidades.DatabaseHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

private lateinit var encabezadoTV : TextView
private lateinit var botonVentas : Button
private lateinit var botonAdd : FloatingActionButton
lateinit var mRecyclerView: RecyclerView
private lateinit var databaseHelper: DatabaseHelper
private lateinit var db: SQLiteDatabase
private lateinit var rutaImagenNuevoProducto : String
private lateinit var vistaFragment : Dialog
private lateinit var bitmapFoto : Bitmap
private lateinit var buscarNombre : EditText
private lateinit var buscarCodigo : EditText
private lateinit var imagenProductoSeleccionado : ImageView
private lateinit var nombreProductoSeleccionado : TextView
private lateinit var stockProductoSeleccionado : TextView
private lateinit var precioCompraProductoSeleccionado : EditText
private lateinit var carpetaTV : TextView
private lateinit var frameProductos : FrameLayout
private lateinit var floatingEntradaButton : FloatingActionButton
private lateinit var botonMas : Button
private lateinit var botonMenos : Button
private lateinit var cantidadET : EditText
val mAdapter: RecyclerAdapter = RecyclerAdapter()
var listaProductos: MutableList<Producto> = mutableListOf()
private lateinit var productoSeleccionado : Producto
var listaProductosEntrada : MutableList<Producto> = mutableListOf()
private var carpeta = true
private lateinit var miContexto : Context

class VentasFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_productos, container, false)

        // Views
        imagenProductoSeleccionado = v.findViewById(R.id.imagenProductoSeleccionado) as ImageView
        nombreProductoSeleccionado = v.findViewById(R.id.nombreProductoSeleccionado) as TextView
        stockProductoSeleccionado = v.findViewById(R.id.StockProductoSeleccionado) as TextView
        precioCompraProductoSeleccionado = v.findViewById(R.id.PrecioCompraProductoSeleccionado) as EditText
        carpetaTV = v.findViewById(R.id.carpetaTV) as TextView
        frameProductos = v.findViewById(R.id.frameProductos) as FrameLayout
        floatingEntradaButton = v.findViewById(R.id.floatingActionButtonEntrada) as FloatingActionButton
        botonMas = v.findViewById(R.id.botonMas) as Button
        botonMenos = v.findViewById(R.id.botonMenos) as Button
        cantidadET = v.findViewById(R.id.cantidadET) as EditText

        // Contexto
        miContexto = v.context

        encabezadoTV = v.findViewById(R.id.encabezadoTV) as TextView
        botonVentas = v.findViewById(R.id.botonEntrada) as Button
        botonAdd = v.findViewById(R.id.floatingActionButtonAdd) as FloatingActionButton

        encabezadoTV.setText("Ventas")
        botonVentas.setText("Vender")
        botonAdd.visibility = View.GONE

        return v
    }


}