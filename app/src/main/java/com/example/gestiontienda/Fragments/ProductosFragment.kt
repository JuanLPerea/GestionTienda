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
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
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
            nuevoProducto(v)
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
                val productoTmp = Producto(productoSeleccionado.nombreProducto, productoSeleccionado.codigoProducto, productoSeleccionado.rutafotoProducto, cantidadET.text.toString().toInt() , precioCompraProductoSeleccionado.text.toString().toFloat(), productoSeleccionado.precioVentaProducto, productoSeleccionado.ivaProducto )
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

    private fun aceptarEntrada() {
        val dialog = Dialog(miContexto , android.R.style.Theme_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.entrada_productos)

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
        
        var campo1params = campo1.layoutParams
        var campo2params = campo2.layoutParams
        var campo3params = campo3.layoutParams

        var totalproductos = 0
        var importe_total = 0f

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
            listaProductosEntrada.clear()
            carpeta = true
            carpetaClick()
            dialog.dismiss()
        }

        botonVolverEntrada.setOnClickListener {
            dialog.dismiss()
        }

        botonDarEntrada.setOnClickListener { vista ->


            databaseHelper.darEntradaListaProductos(db, listaProductosEntrada)
        }

        seleccionarProveedorIB.setOnClickListener { vista ->
            val dialogSeleccionarProveedor = Dialog(vista.context)
            dialogSeleccionarProveedor.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogSeleccionarProveedor.setCancelable(false)
            dialogSeleccionarProveedor.setContentView(R.layout.dialogo_seleccionar)

            val botonAceptarSeleccion = dialogSeleccionarProveedor.findViewById(R.id.aceptar_seleccion) as Button

            val listaStrings = databaseHelper.obtenerProveedoresString(db)
            val picker = dialogSeleccionarProveedor.findViewById(R.id.picker) as NumberPicker
            picker.minValue = 0
            picker.maxValue = listaStrings.size-1
            picker.displayedValues = listaStrings.toTypedArray()
            picker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            botonAceptarSeleccion.setOnClickListener {
                Log.d("Miapp" , "Proveedor: " + picker.value)
                dialogSeleccionarProveedor.dismiss()
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
        precioCompraProductoSeleccionado.setText( productoSeleccionado.precioVentaProducto.toString())
    }

    private fun nuevoProducto(v : View) {
        val dialog = Dialog(v.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.crear_producto)

        vistaFragment = dialog

        val nombreProducto = dialog.findViewById(R.id.nombreProductoET) as EditText
        val codigoProducto = dialog.findViewById(R.id.codigoProductoET) as EditText
        val precioCompra = dialog.findViewById(R.id.precioCompraET) as EditText
        val precioVenta = dialog.findViewById(R.id.precioVentaET) as EditText
        val ivaProducto = dialog.findViewById(R.id.ivaET) as EditText

        val botonGuardar = dialog.findViewById(R.id.boton_guardar_producto) as Button
        val botonFoto = dialog.findViewById(R.id.fotoProductoIB) as ImageButton
        val botonCancelar = dialog.findViewById(R.id.boton_cancelar_producto) as Button

        botonGuardar.setOnClickListener {

            if (nombreProducto.text.toString() == "" || codigoProducto.text.toString() == "" || precioCompra.text.toString() == "" || precioVenta.text.toString() == "" || ivaProducto.text.toString() == "") {
                Toast.makeText(v.context, "Debes completar todos los datos", Toast.LENGTH_LONG).show()
            } else {
                if (databaseHelper.productoExiste(db, codigoProducto.text.toString())) {
                    Toast.makeText(v.context, "Ya existe un producto con este código", Toast.LENGTH_LONG).show()
                } else {
                    val precioCompraTmp = precioCompra.text.toString().toFloat()
                    val precioVentaTmp =precioVenta.text.toString().toFloat()
                    val ivaTmp = ivaProducto.text.toString().toInt()

                    // Guardar imagen con el codigo de producto
                    val imagesHelper = ImagesHelper(activity!!.applicationContext)
                    // guardamos la imagen en la memoria interna
                    val rutaImagen = imagesHelper.guardarBitmapEnMemoria(activity!!.applicationContext, bitmapFoto, codigoProducto.text.toString())
                    // Convertimos la ruta del archivo a String y lo guardamos en la BD
                    rutaImagenNuevoProducto = rutaImagen.toString()
                    val miProducto = Producto(nombreProducto.text.toString(), codigoProducto.text.toString(), rutaImagenNuevoProducto,0, precioCompraTmp, precioVentaTmp, ivaTmp)
                    databaseHelper.crearProducto(db, miProducto)
                    Toast.makeText(v.context, "Nuevo producto creado", Toast.LENGTH_LONG).show()
                    listaProductos.add(miProducto)
                    mAdapter.notifyDataSetChanged()
                    dialog.dismiss()
                }

            }

        }

        botonCancelar.setOnClickListener {
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


}