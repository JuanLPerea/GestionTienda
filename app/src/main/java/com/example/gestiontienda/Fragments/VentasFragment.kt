package com.example.gestiontienda.Fragments

import android.app.Dialog
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestiontienda.Adapters.RecyclerAdapterVentas
import com.example.gestiontienda.Entidades.Producto
import com.example.gestiontienda.Interfaces.OnItemListClicked
import com.example.gestiontienda.R
import com.example.gestiontienda.Utilidades.DatabaseHelper
import com.example.gestiontienda.Utilidades.ImagesHelper
import com.example.gestiontienda.Utilidades.Utilidades.Companion.hideKeyboard
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
private lateinit var floatingVentaButton : FloatingActionButton
private lateinit var botonMas : Button
private lateinit var botonMenos : Button
private lateinit var cantidadET : EditText
val mAdapter: RecyclerAdapterVentas = RecyclerAdapterVentas()
var listaProductos: MutableList<Producto> = mutableListOf()
private lateinit var productoSeleccionado : Producto
var listaProductosVenta : MutableList<Producto> = mutableListOf()
private var carpeta = true
private lateinit var miContexto : Context

class VentasFragment : Fragment(), OnItemListClicked {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_productos, container, false)

        // Views
        imagenProductoSeleccionado = v.findViewById(R.id.imagenProductoSeleccionado) as ImageView
        nombreProductoSeleccionado = v.findViewById(R.id.nombreProductoSeleccionado) as TextView
        stockProductoSeleccionado = v.findViewById(R.id.StockProductoSeleccionado) as TextView
        precioCompraProductoSeleccionado = v.findViewById(R.id.PrecioCompraProductoSeleccionado) as EditText
        carpetaTV = v.findViewById(R.id.carpetaTV) as TextView
        frameProductos = v.findViewById(R.id.frameProductos) as FrameLayout
        floatingVentaButton = v.findViewById(R.id.floatingActionButtonEntrada) as FloatingActionButton
        floatingVentaButton.visibility = View.VISIBLE
        botonMas = v.findViewById(R.id.botonMas) as Button
        botonMenos = v.findViewById(R.id.botonMenos) as Button
        cantidadET = v.findViewById(R.id.cantidadET) as EditText
        floatingVentaButton.visibility = View.INVISIBLE

        // Contexto
        miContexto = v.context

        // Pestaña de la carpeta
        carpetaTV.setOnClickListener {
            if (carpeta) carpeta = false else carpeta = true
            carpetaClick()
        }

        encabezadoTV = v.findViewById(R.id.encabezadoTV) as TextView
        botonVentas = v.findViewById(R.id.botonEntrada) as Button
        botonAdd = v.findViewById(R.id.floatingActionButtonAdd) as FloatingActionButton

        encabezadoTV.setText("Ventas")
        botonVentas.setText("Vender")
        botonAdd.visibility = View.GONE

        // Instanciar Base de Datos SQLite
        databaseHelper = DatabaseHelper(activity!!.applicationContext)
        db = databaseHelper.writableDatabase
        bitmapFoto = BitmapFactory.decodeResource(activity!!.resources, R.drawable.nophoto)

        // Recycler View
        listaProductos = databaseHelper.obtenerProductos(db, "","")
        productoSeleccionado = listaProductos.first()


        mRecyclerView = v.findViewById(R.id.recycler_productosRV) as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(v.context)

        // Recycler adapter
        mAdapter.RecyclerAdapter(listaProductos, v.context, this)
        mRecyclerView.adapter = mAdapter

        // Botón Mas
        botonMas.setOnClickListener {
            var cantidad = cantidadET.text.toString().toInt()
            cantidad++
            cantidadET.setText(cantidad.toString())
        }

        // Botón Menos
        botonMenos.setOnClickListener {
            var cantidad = cantidadET.text.toString().toInt()
            cantidad--
            cantidadET.setText(cantidad.toString())
        }


        // Buscar por nombre producto
        buscarNombre = v.findViewById(R.id.buscarNombreProducto) as EditText
        buscarNombre.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                carpeta = true
                carpetaClick()
                listaProductos.clear()
                listaProductos.addAll(databaseHelper.obtenerProductos(db, buscarNombre.text.toString(), ""))
                mAdapter.notifyDataSetChanged()
                productoSeleccionado = listaProductos.first()
                actualizarProductoSeleccionado()
            }

        })

        buscarCodigo = v.findViewById(R.id.buscarCodigoProducto) as EditText
        buscarCodigo.addTextChangedListener(object  : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                carpeta = true
                carpetaClick()
                listaProductos.clear()
                listaProductos.addAll(databaseHelper.obtenerProductos(db, "", buscarCodigo.text.toString()))
                mAdapter.notifyDataSetChanged()
                productoSeleccionado = listaProductos.first()
                actualizarProductoSeleccionado()
            }
        })


        // Boton Vender
        botonVentas.setOnClickListener {
            carpeta = false
            if (productoSeleccionado.codigoProducto != "0") {
                if (productoSeleccionado.stockProducto >= cantidadET.text.toString().toInt()){
                    val productoTmp = Producto(productoSeleccionado.nombreProducto, productoSeleccionado.codigoProducto, productoSeleccionado.rutafotoProducto, cantidadET.text.toString().toInt() , precioCompraProductoSeleccionado.text.toString().toFloat(), productoSeleccionado.precioVentaProducto, productoSeleccionado.ivaProducto )
                    listaProductosVenta.add(productoTmp)
                    carpeta = false
                    hideKeyboard()
                    carpetaClick()
                } else {
                    Toast.makeText(context, "No Hay suficiente stock de este producto." , Toast.LENGTH_LONG).show()
                }

            } else {
                Toast.makeText(context, "Selecciona un Producto" , Toast.LENGTH_LONG).show()
            }
        }

        // Boton Aceptar entrada
        floatingVentaButton.setOnClickListener {
            aceptarVenta()
        }

        return v
    }

    private fun aceptarVenta() {
        val dialog = Dialog(miContexto , android.R.style.Theme_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.entrada_productos)

        var cliente = databaseHelper.obtenerClientes(db).first()

        val tablaEntrada = dialog.findViewById(R.id.tablaProductos) as TableLayout
        val campo1 = dialog.findViewById(R.id.campo1_tabla) as TextView
        val campo2 = dialog.findViewById(R.id.campo2_tabla) as TextView
        val campo3 = dialog.findViewById(R.id.campo3_tabla) as TextView
        val botonBorrarEntrada = dialog.findViewById(R.id.boton_borrar_todo_entrada) as Button
        val botonDarEntrada = dialog.findViewById(R.id.boton_dar_entrada) as Button
        val botonVolverEntrada = dialog.findViewById(R.id.boton_volver_entrada) as Button
        val totalProductos = dialog.findViewById(R.id.total_productos) as TextView
        val importeTotal = dialog.findViewById(R.id.importeTotalProductos) as TextView
        val seleccionarProveedorIB = dialog.findViewById(R.id.seleccionarProveedorBTN) as ImageButton
        val proveedorSeleccionado = dialog.findViewById(R.id.proveedorSeleccionado) as TextView
        proveedorSeleccionado.setText(cliente.nombreCliente)

        var campo1params = campo1.layoutParams
        var campo2params = campo2.layoutParams
        var campo3params = campo3.layoutParams

        var totalproductos = 0
        var importe_total = 0f

        for (productoTMP in listaProductosVenta) {
            val newRow = TableRow(dialog.context)

            val campo1 = TextView(dialog.context)
            campo1.layoutParams = campo1params
            campo1.setTextColor(resources.getColor(R.color.black))
            campo1.setPadding(10,10,10,10)
            campo1.setText(productoTMP.nombreProducto)

            val campo2 = TextView(dialog.context)
            campo2.layoutParams = campo2params
            campo2.setTextColor(resources.getColor(R.color.black))
            campo2.gravity = Gravity.RIGHT
            campo2.setPadding(10,10,20,10)
            campo2.setText(productoTMP.stockProducto.toString())

            val campo3 = TextView(dialog.context)
            campo3.layoutParams = campo3params
            campo3.setTextColor(resources.getColor(R.color.black))
            campo3.gravity = Gravity.RIGHT
            campo3.setPadding(10,10,20,10)
            campo3.setText(productoTMP.precioCompraProducto.toString())

            newRow.addView(campo1)
            newRow.addView(campo2)
            newRow.addView(campo3)

            totalproductos += productoTMP.stockProducto
            importe_total += productoTMP.precioCompraProducto
            tablaEntrada.addView(newRow)
        }

        totalProductos.setText(totalproductos.toString())
        importeTotal.setText(importe_total.toString())

        botonBorrarEntrada.setOnClickListener {
            listaProductosVenta.clear()
            carpeta = true
            carpetaClick()
            dialog.dismiss()
        }

        botonVolverEntrada.setOnClickListener {
            dialog.dismiss()
        }

        botonDarEntrada.setOnClickListener { vista ->
            databaseHelper.venderProductos(db, listaProductosVenta, cliente)
            listaProductos = databaseHelper.obtenerProductos(db, "","")
            listaProductosVenta.clear()
            carpeta = true
            carpetaClick()
            dialog.dismiss()
        }

        seleccionarProveedorIB.setOnClickListener { vista ->
            val dialogSeleccionarCliente = Dialog(vista.context)
            dialogSeleccionarCliente.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogSeleccionarCliente.setCancelable(false)
            dialogSeleccionarCliente.setContentView(R.layout.dialogo_seleccionar)

            val listaClientes = databaseHelper.obtenerClientes(db)
            val picker = dialogSeleccionarCliente.findViewById(R.id.tablaSeleccion) as TableLayout

            listaClientes.forEach { clienteTMP ->
                val nuevaFila = TableRow(vista.context)
                val rowProveedor = TextView(vista.context)
                rowProveedor.setText(clienteTMP.nombreCliente)
                rowProveedor.setOnClickListener {
                    cliente = clienteTMP
                    proveedorSeleccionado.setText(cliente.nombreCliente)
                    dialogSeleccionarCliente.dismiss()
                }
                nuevaFila.addView(rowProveedor)
                picker.addView(nuevaFila)
            }
            dialogSeleccionarCliente.show()
        }
        dialog.show()
    }

    override fun itemListClicked(producto: Producto, itemView: View) {
        productoSeleccionado = producto
        actualizarProductoSeleccionado()
    }

    private fun actualizarProductoSeleccionado() {
        imagenProductoSeleccionado.setImageBitmap(ImagesHelper(activity!!.applicationContext).recuperarImagenMemoriaInterna(productoSeleccionado.rutafotoProducto))
        nombreProductoSeleccionado.text = productoSeleccionado.nombreProducto
        stockProductoSeleccionado.text = productoSeleccionado.stockProducto.toString()
        precioCompraProductoSeleccionado.setText( productoSeleccionado.precioVentaProducto.toString())
    }


    private fun carpetaClick() {
        if (carpeta) {
            floatingVentaButton.visibility = View.INVISIBLE
            carpetaTV.gravity = Gravity.LEFT
            carpetaTV.setText("Productos en stock")
            carpetaTV.background = resources.getDrawable(R.drawable.carpeta1)
            frameProductos.setBackgroundColor( resources.getColor(R.color.crema))
            listaProductos = databaseHelper.obtenerProductos(db, "","")
            mAdapter.RecyclerAdapter(listaProductos, miContexto , this)
            mRecyclerView.adapter = mAdapter
            mAdapter.notifyDataSetChanged()
        } else {
            floatingVentaButton.visibility = View.VISIBLE
            carpetaTV.gravity = Gravity.RIGHT
            carpetaTV.setText("Cesta de la compra 🛒")
            carpetaTV.background = resources.getDrawable(R.drawable.carpeta2)
            frameProductos.setBackgroundColor( resources.getColor(R.color.verdeclaro))
            mAdapter.RecyclerAdapter(listaProductosVenta, miContexto , this)
            mRecyclerView.adapter = mAdapter
            mAdapter.notifyDataSetChanged()
        }
    }


    override fun onResume() {
        super.onResume()
        carpetaClick()
    }
}