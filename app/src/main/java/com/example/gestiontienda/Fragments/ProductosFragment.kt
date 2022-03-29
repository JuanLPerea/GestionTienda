package com.example.gestiontienda.Fragments

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestiontienda.Adapters.RecyclerAdapter
import com.example.gestiontienda.Entidades.Producto
import com.example.gestiontienda.Interfaces.OnItemListClicked
import com.example.gestiontienda.R
import com.example.gestiontienda.Utilidades.DatabaseHelper
import com.example.gestiontienda.Utilidades.ImagesHelper
import com.example.gestiontienda.Utilidades.Utilidades.Companion.hideKeyboard
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductosFragment : Fragment(), OnItemListClicked {

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
    private lateinit var floatingAddButton : FloatingActionButton
    private lateinit var floatingEntradaButton : FloatingActionButton
    private lateinit var botonEntrada : Button
    private lateinit var botonMas : Button
    private lateinit var botonMenos : Button
    private lateinit var cantidadET : EditText
    val mAdapter: RecyclerAdapter = RecyclerAdapter()
    var listaProductos: MutableList<Producto> = mutableListOf()
    private lateinit var productoSeleccionado : Producto
    var listaProductosEntrada : MutableList<Producto> = mutableListOf()
    private var carpeta = true
    private lateinit var miContexto : Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_productos, container, false)

        // Views
        imagenProductoSeleccionado = v.findViewById(R.id.imagenProductoSeleccionado) as ImageView
        nombreProductoSeleccionado = v.findViewById(R.id.nombreProductoSeleccionado) as TextView
        stockProductoSeleccionado = v.findViewById(R.id.StockProductoSeleccionado) as TextView
        precioCompraProductoSeleccionado = v.findViewById(R.id.PrecioCompraProductoSeleccionado) as EditText
        carpetaTV = v.findViewById(R.id.carpetaTV) as TextView
        frameProductos = v.findViewById(R.id.frameProductos) as FrameLayout
        floatingAddButton = v.findViewById(R.id.floatingActionButtonAdd) as FloatingActionButton
        floatingEntradaButton = v.findViewById(R.id.floatingActionButtonEntrada) as FloatingActionButton
        botonEntrada = v.findViewById(R.id.botonEntrada) as Button
        botonMas = v.findViewById(R.id.botonMas) as Button
        botonMenos = v.findViewById(R.id.botonMenos) as Button
        cantidadET = v.findViewById(R.id.cantidadET) as EditText

        // Contexto
        miContexto = v.context

        // Pestaña de la carpeta
        carpetaTV.setOnClickListener {
            if (carpeta) carpeta = false else carpeta = true
            carpetaClick()
        }

        // Instanciar Base de Datos SQLite
        databaseHelper = DatabaseHelper(activity!!.applicationContext)
        db = databaseHelper.writableDatabase
        bitmapFoto = BitmapFactory.decodeResource(activity!!.resources, R.drawable.nophoto)

        val botonNuevoProducto = v.findViewById(R.id.floatingActionButtonAdd) as FloatingActionButton
        botonNuevoProducto.setOnClickListener {
            editarProducto(v, true , "")
        }

        // Recycler View
        listaProductos = databaseHelper.obtenerProductos(db, "","")
        productoSeleccionado = listaProductos.first()
        actualizarProductoSeleccionado()

        mRecyclerView = v.findViewById(R.id.recycler_productosRV) as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(v.context)

        // Recycler adapter
        mAdapter.RecyclerAdapter(listaProductos, v.context, this)
        mRecyclerView.adapter = mAdapter

        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                Log.d("Miapp" , "Mover fila")
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (carpeta) {
                    if (direction == ItemTouchHelper.LEFT) {
                        // Swipe hacia la izquierda editar
                        editarProducto(v, false , listaProductos.get(position).codigoProducto)
                        carpetaClick()
                    } else {
                        // Swipe hacia la derecha borrar
                        showDialogConfirmarBorrar(position)
                        carpetaClick()
                    }
                } else {
                    if (direction == ItemTouchHelper.RIGHT) {
                        listaProductosEntrada.removeAt(position)
                        carpetaClick()
                    } else {
                        carpetaClick()
                    }
                }
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                var icon = ContextCompat.getDrawable(mAdapter.context, R.drawable.ic_baseline_delete_24);
                var background =  ColorDrawable(Color.RED);

                val itemView = viewHolder.itemView
                val backgroundCornerOffset = 20
                val iconMargin = (itemView.getHeight() - icon!!.getIntrinsicHeight()) / 2
                val iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2
                val iconBottom = iconTop + icon.getIntrinsicHeight()

                if (dX > 0) {
                    val iconLeft = itemView.getLeft() + iconMargin
                    val iconRight = itemView.getLeft() + icon.intrinsicWidth + iconMargin
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    background.setBounds(itemView.getLeft(), itemView.getTop(),itemView.getLeft() + (dX.toInt()) + backgroundCornerOffset, itemView.getBottom())
                } else if (dX < 0) { // Swiping to the left
                    background =  ColorDrawable(Color.GREEN);
                    if (carpeta) {
                        icon = ContextCompat.getDrawable(mAdapter.context, R.drawable.ic_baseline_create_24)
                    } else {
                        icon = ContextCompat.getDrawable(mAdapter.context, R.drawable.ic_baseline_cancel_24)
                    }

                    val iconLeft = itemView.getRight() - iconMargin - icon!!.getIntrinsicWidth()
                    val iconRight = itemView.getRight() - iconMargin
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    background.setBounds(itemView.getRight() + (dX.toInt()) - backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom())
                } else { // view is unSwiped
                    background.setBounds(0, 0, 0, 0);
                }
                background.draw(c);
                icon.draw(c);
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(mRecyclerView)

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

        // Boton entrada
        botonEntrada.setOnClickListener {
            carpeta = false
            if (productoSeleccionado.codigoProducto != "0") {
                val productoTmp = Producto(productoSeleccionado.nombreProducto, productoSeleccionado.codigoProducto, productoSeleccionado.rutafotoProducto, cantidadET.text.toString().toInt() , precioCompraProductoSeleccionado.text.toString().toFloat(), productoSeleccionado.precioVentaProducto, productoSeleccionado.ivaProducto , productoSeleccionado.margenProducto)
                listaProductosEntrada.add(productoTmp)
                carpeta = false
                hideKeyboard()
                carpetaClick()
            } else {
                Toast.makeText(context, "Selecciona un Producto" , Toast.LENGTH_LONG).show()
            }
        }

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

        // Boton Aceptar entrada
        floatingEntradaButton.setOnClickListener {
            aceptarEntrada()
        }

        return v
    }

    private fun showDialogConfirmarBorrar(position: Int) {
        val dialog = Dialog(miContexto)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.confirmar_layout)

        val textoConfirmar = dialog.findViewById(R.id.texto_confirmarTV) as TextView
        val botonConfirmarOK = dialog.findViewById(R.id.boton_confirmar_OK) as Button
        val botonConfirmarCancelar = dialog.findViewById(R.id.boton_confirmar_CANCELAR) as Button

        textoConfirmar.setText("Confirmar borrar el producto. (No se puede deshacer)")
        botonConfirmarOK.setOnClickListener {
            databaseHelper.borrarProducto(db, listaProductos.get(position))
            Toast.makeText(dialog.context, "Producto Borrado", Toast.LENGTH_LONG).show()
            listaProductos = databaseHelper.obtenerProductos(db, "", "")
            mAdapter.notifyDataSetChanged()
            carpetaClick()
            dialog.dismiss()
        }

        botonConfirmarCancelar.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun aceptarEntrada() {
        val dialog = Dialog(miContexto , android.R.style.Theme_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.entrada_productos)

        var proveedor = databaseHelper.obtenerProveedores(db).first()

        val tablaEntrada = dialog.findViewById(R.id.tablaProductos) as TableLayout
        val campo1 = dialog.findViewById(R.id.campo1_tabla) as TextView
        val campo2 = dialog.findViewById(R.id.campo2_tabla) as TextView
        val campo3 = dialog.findViewById(R.id.campo3_tabla) as TextView
        val campo4 = dialog.findViewById(R.id.campo4_tabla) as TextView
        val campo5 = dialog.findViewById(R.id.campo5_tabla) as TextView
        val botonBorrarEntrada = dialog.findViewById(R.id.boton_borrar_todo_entrada) as Button
        val botonDarEntrada = dialog.findViewById(R.id.boton_resumen_1) as Button
        botonDarEntrada.setText("DAR ENTRADA")
        val botonVolverEntrada = dialog.findViewById(R.id.boton_volver_entrada) as Button
        val totalProductos = dialog.findViewById(R.id.total_productos) as TextView
        val importeTotal = dialog.findViewById(R.id.importeTotalIVA) as TextView
        val seleccionarProveedorIB = dialog.findViewById(R.id.seleccionarProveedorBTN) as ImageButton
        val proveedorSeleccionado = dialog.findViewById(R.id.proveedorSeleccionado) as TextView
        proveedorSeleccionado.setText(proveedor.nombreProveedor)
        
        var campo1params = campo1.layoutParams
        var campo2params = campo2.layoutParams
        var campo3params = campo3.layoutParams
        var campo4params = campo4.layoutParams
        var campo5params = campo5.layoutParams

        var totalproductos = 0
        var importe_total = 0f
        var totaliva = 0f
        var totalpagar = 0f

        for (productoTMP in listaProductosEntrada) {
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

            // IVA
            val campo4 = TextView(dialog.context)
            campo4.layoutParams = campo4params
            campo4.setTextColor(resources.getColor(R.color.black))
            campo4.gravity = Gravity.RIGHT
            campo4.setPadding(10,10,20,10)
            val iva = ((productoTMP.ivaProducto * productoTMP.precioCompraProducto) / 100)
            totaliva += iva
            campo4.setText(String.format("%.2f", iva).replace('.',','))
            // SUBTOTAL
            val campo5 = TextView(dialog.context)
            campo5.layoutParams = campo5params
            campo5.setTextColor(resources.getColor(R.color.black))
            campo5.gravity = Gravity.RIGHT
            campo5.setPadding(10,10,20,10)
            val subtotal = productoTMP.precioCompraProducto + iva
            totalpagar += subtotal
            campo5.setText(String.format("%.2f", subtotal).replace('.',','))

            newRow.addView(campo1)
            newRow.addView(campo2)
            newRow.addView(campo3)
            newRow.addView(campo4)
            newRow.addView(campo5)

            totalproductos += productoTMP.stockProducto
            importe_total += (productoTMP.precioCompraProducto * productoTMP.stockProducto)
            tablaEntrada.addView(newRow)
        }
        
        totalProductos.setText(totalproductos.toString())
        importeTotal.setText(importe_total.toString())

        botonBorrarEntrada.setOnClickListener {
            listaProductosEntrada.clear()
            carpeta = true
            carpetaClick()
            dialog.dismiss()
        }

        botonVolverEntrada.setOnClickListener {
            dialog.dismiss()
        }

        botonDarEntrada.setOnClickListener { vista ->
            databaseHelper.darEntradaListaProductos(db, listaProductosEntrada, proveedor)
            listaProductos = databaseHelper.obtenerProductos(db, "","")
            listaProductosEntrada.clear()
            carpeta = true
            carpetaClick()
            dialog.dismiss()
        }

        seleccionarProveedorIB.setOnClickListener { vista ->
            val dialogSeleccionarProveedor = Dialog(vista.context)
            dialogSeleccionarProveedor.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogSeleccionarProveedor.setCancelable(false)
            dialogSeleccionarProveedor.setContentView(R.layout.dialogo_seleccionar)

            val listaProveedores = databaseHelper.obtenerProveedores(db)
            val picker = dialogSeleccionarProveedor.findViewById(R.id.tablaSeleccion) as TableLayout

            listaProveedores.forEach { proveedorTMP ->
                val nuevaFila = TableRow(vista.context)
                val rowProveedor = TextView(vista.context)
                rowProveedor.setText(proveedorTMP.nombreProveedor)
                rowProveedor.setOnClickListener {
                    proveedor = proveedorTMP
                    proveedorSeleccionado.setText(proveedor.nombreProveedor)
                    dialogSeleccionarProveedor.dismiss()
                }
                nuevaFila.addView(rowProveedor)
                picker.addView(nuevaFila)
            }
            dialogSeleccionarProveedor.show()
        }
        dialog.show()

    }

    private fun carpetaClick() {
        if (carpeta) {
            carpetaTV.gravity = Gravity.LEFT
            carpetaTV.setText("Productos en stock")
            carpetaTV.background = resources.getDrawable(R.drawable.carpeta1)
            frameProductos.setBackgroundColor( resources.getColor(R.color.crema))
            floatingAddButton.visibility = View.VISIBLE
            floatingEntradaButton.visibility = View.INVISIBLE
            listaProductos = databaseHelper.obtenerProductos(db, "","")
            mAdapter.RecyclerAdapter(listaProductos, miContexto , this)
            mRecyclerView.adapter = mAdapter
            mAdapter.notifyDataSetChanged()
        } else {
            carpetaTV.gravity = Gravity.RIGHT
            carpetaTV.setText("Dar entrada a stock")
            carpetaTV.background = resources.getDrawable(R.drawable.carpeta2)
            frameProductos.setBackgroundColor( resources.getColor(R.color.verdeclaro))
            floatingAddButton.visibility = View.INVISIBLE
            floatingEntradaButton.visibility = View.VISIBLE
            mAdapter.RecyclerAdapter(listaProductosEntrada, miContexto , this)
            mRecyclerView.adapter = mAdapter
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun actualizarProductoSeleccionado() {
        imagenProductoSeleccionado.setImageBitmap(ImagesHelper(activity!!.applicationContext).recuperarImagenMemoriaInterna(productoSeleccionado.rutafotoProducto))
        nombreProductoSeleccionado.text = productoSeleccionado.nombreProducto
        stockProductoSeleccionado.text = productoSeleccionado.stockProducto.toString()
        precioCompraProductoSeleccionado.setText( productoSeleccionado.precioCompraProducto.toString())
    }

    private fun editarProducto(v : View, nuevo : Boolean, codigoProductoEditado : String) {
        val dialog = Dialog(v.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.crear_producto)

        vistaFragment = dialog

        val tituloDialogoProducto = dialog.findViewById(R.id.titulo_dialogo_producto) as TextView
        val nombreProducto = dialog.findViewById(R.id.nombreProductoET) as EditText
        val codigoProducto = dialog.findViewById(R.id.codigoProductoET) as EditText
        val precioCompra = dialog.findViewById(R.id.precioCompraET) as EditText
        val precioVenta = dialog.findViewById(R.id.precioVentaET) as EditText
        val ivaProducto = dialog.findViewById(R.id.ivaET) as EditText
        val margenProducto = dialog.findViewById(R.id.margenET) as EditText

        val botonGuardar = dialog.findViewById(R.id.boton_guardar_producto) as Button
        val botonFoto = dialog.findViewById(R.id.fotoProductoIB) as ImageButton
        val botonCancelar = dialog.findViewById(R.id.boton_cancelar_producto) as Button
        val imagenProducto = dialog.findViewById(R.id.productoNuevoIV) as ImageView
        var stockProducto = 0

        tituloDialogoProducto.setText("Nuevo Producto")
        if (!nuevo) {
            val productoTMP = databaseHelper.obtenerProducto(db, codigoProductoEditado)
            tituloDialogoProducto.setText("Editar Producto")
            nombreProducto.setText(productoTMP.nombreProducto)
            codigoProducto.setText(productoTMP.codigoProducto)
            precioCompra.setText(productoTMP.precioCompraProducto.toString())
            precioVenta.setText(productoTMP.precioVentaProducto.toString())
            ivaProducto.setText(productoTMP.ivaProducto.toString())
            margenProducto.setText(productoTMP.margenProducto.toString().replace('.',','))
            bitmapFoto = ImagesHelper(activity!!.applicationContext).recuperarImagenMemoriaInterna(productoTMP.rutafotoProducto) as Bitmap
            imagenProducto.setImageBitmap(bitmapFoto)
            rutaImagenNuevoProducto = productoTMP.rutafotoProducto
            stockProducto = productoTMP.stockProducto
        }

        margenProducto.setOnFocusChangeListener { view, b ->

            if (precioCompra.text.toString() != "" && ivaProducto.text.toString() != "" && margenProducto.text.toString() != "" && !b) {
                val precio_compra = precioCompra.text.toString().replace(',','.').toFloat()
                val iva = precio_compra * ivaProducto.text.toString().replace(',','.').toInt() / 100
                val margen = precio_compra * margenProducto.text.toString().replace(',','.').toFloat() / 100
                val precio_venta = precio_compra + iva + margen
                precioVenta.setText(precio_venta.toString().format("%.2f", iva).replace('.',','))

            }
        }

        ivaProducto.setOnFocusChangeListener {view, b ->
            if (precioCompra.text.toString() != "" && ivaProducto.text.toString() != "" && margenProducto.text.toString() != "" && !b) {
                if (precioCompra.text.toString() != "" && ivaProducto.text.toString() != "" && margenProducto.text.toString() != "") {
                    val precio_compra = precioCompra.text.toString().replace(',','.').toFloat()
                    val iva = precio_compra * ivaProducto.text.toString().replace(',','.').toInt() / 100
                    val margen = precio_compra * margenProducto.text.toString().replace(',','.').toFloat() / 100
                    val precio_venta = precio_compra + iva + margen
                    precioVenta.setText(precio_venta.toString().format("%.2f", iva).replace('.',','))
                }
            }

        precioVenta.setOnFocusChangeListener {view, b ->
            if (precioCompra.text.toString() != ""&& precioVenta.text.toString() != "" && ivaProducto.text.toString() != "" && margenProducto.text.toString() != "" && !b) {
                val precio_compra = precioCompra.text.toString().replace(',','.').toFloat()
                val precio_venta = precioVenta.text.toString().replace(',','.').toFloat()
                val iva = precio_compra * ivaProducto.text.toString().replace(',','.').toFloat() / 100
                val margen = ((((precio_venta - iva) * 100) / precio_compra)-100).toString().format("%.2f", iva).replace('.', ',')
                margenProducto.setText(margen.format("%.2f", iva).replace('.',','))

            }
       }
    }

        botonGuardar.setOnClickListener {
            if (nombreProducto.text.toString() == "" || codigoProducto.text.toString() == "" || precioCompra.text.toString() == "" || precioVenta.text.toString() == "" || ivaProducto.text.toString() == "") {
                Toast.makeText(v.context, "Debes completar todos los datos", Toast.LENGTH_LONG).show()
            } else {
                if (databaseHelper.productoExiste(db, codigoProducto.text.toString()) && nuevo) {
                    Toast.makeText(v.context, "Ya existe un producto con este código", Toast.LENGTH_LONG).show()
                } else {
                    val precioCompraTmp = precioCompra.text.toString().replace(',','.').toFloat()
                    val precioVentaTmp =precioVenta.text.toString().replace(',','.').toFloat()
                    val ivaTmp = ivaProducto.text.toString().replace(',','.').toInt()
                    val margenTmp = margenProducto.text.toString().replace(',','.').toFloat()

                    if (nuevo) {
                        // Guardar imagen con el codigo de producto
                        val imagesHelper = ImagesHelper(activity!!.applicationContext)
                        // guardamos la imagen en la memoria interna
                        val rutaImagen = imagesHelper.guardarBitmapEnMemoria(activity!!.applicationContext, bitmapFoto, codigoProducto.text.toString())
                        // Convertimos la ruta del archivo a String y lo guardamos en la BD
                        rutaImagenNuevoProducto = rutaImagen.toString()
                    }

                    val miProducto = Producto(nombreProducto.text.toString(), codigoProducto.text.toString(), rutaImagenNuevoProducto,stockProducto, precioCompraTmp, precioVentaTmp, ivaTmp, margenTmp)
                    databaseHelper.crearProducto(db, miProducto)
                    if (nuevo) {
                        Toast.makeText(v.context, "Nuevo producto creado", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(v.context, "Producto Actualizado", Toast.LENGTH_LONG).show()
                    }
                    carpetaClick()
                    dialog.dismiss()
                }

            }

        }

        botonCancelar.setOnClickListener {
            carpetaClick()
            dialog.dismiss()
        }

        botonFoto.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (activity!!.applicationContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    //permission already granted
                    dispatchTakePictureIntent()
                }
            } else {
                //system OS is < Marshmallow
                dispatchTakePictureIntent()
            }
        }

        dialog.show()
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;

        //Permission code
        private val PERMISSION_CODE = 1001;

        // Image capture
        private val REQUEST_IMAGE_CAPTURE = 1

    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.applicationContext.packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }



    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    dispatchTakePictureIntent()
                } else {
                    //permission from popup denied
                    Toast.makeText(activity!!.applicationContext, "Autoriza los permisos de la aplicación por favor", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val imagesHelper = ImagesHelper(activity!!.applicationContext)

            // Convertimos el URI de la imagen seleccionada en un Bitmap
            val imageURI = data?.data
            val bitmap = imagesHelper.obtenerBitmap(activity!!.applicationContext, imageURI)
            // redimensionamos la imagen
            bitmapFoto = Bitmap.createScaledBitmap(bitmap!!, 256, 192, false)

            // filtro a la imagen
            //  resizedBitmap = EfectosImagen.sketch(resizedBitmap)
            //  imagenCrearAventura.setImageBitmap(resizedBitmap)

            val imagenProducto = vistaFragment.findViewById(R.id.productoNuevoIV) as ImageView
            imagenProducto.setImageBitmap(imagesHelper.recuperarImagenMemoriaInterna(rutaImagenNuevoProducto))

        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == -1) {
            val imagesHelper = ImagesHelper(activity!!.applicationContext)
            Log.d("Miapp" , resultCode.toString())
            val imageBitmap = data!!.extras!!.get("data") as Bitmap

            // redimensionamos la imagen
            bitmapFoto = Bitmap.createScaledBitmap(imageBitmap, 256, 192, false)

            // guardamos la imagen en la memoria interna
            val rutaImagen = imagesHelper.guardarBitmapEnMemoria(activity!!.applicationContext, bitmapFoto, "codigoproducto")

            // Convertimos la ruta del archivo a String y lo guardamos en la BD
            rutaImagenNuevoProducto = rutaImagen.toString()

            val imagenProducto = vistaFragment.findViewById(R.id.productoNuevoIV) as ImageView
            imagenProducto.setImageBitmap(imageBitmap)
        }

    }

    override fun itemListClicked(producto: Producto, itemView: View) {
      //  Toast.makeText(activity!!.applicationContext, "Pulsado el producto: " + producto.nombreProducto, Toast.LENGTH_LONG).show()
        productoSeleccionado = producto
        actualizarProductoSeleccionado()
    }

    override fun onResume() {
        super.onResume()
        carpetaClick()
    }
}