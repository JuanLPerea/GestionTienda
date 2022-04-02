package com.example.gestiontienda.Fragments

import android.app.Dialog
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestiontienda.Adapters.RecyclerAdapterClientes
import com.example.gestiontienda.Adapters.RecyclerAdapterProveedores
import com.example.gestiontienda.Entidades.Cliente
import com.example.gestiontienda.Entidades.Proveedor
import com.example.gestiontienda.Interfaces.OnProveedorListClicked
import com.example.gestiontienda.R
import com.example.gestiontienda.Utilidades.DatabaseHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProveedoresFragment : Fragment() , OnProveedorListClicked {
    
    lateinit var mRecyclerView: RecyclerView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var vistaFragment : Dialog
    private lateinit var bitmapFoto : Bitmap
    private lateinit var buscarNombre : EditText
    private lateinit var buscarCodigo : EditText
    private lateinit var nombreProveedorSeleccionado : TextView
    private lateinit var codigoProveedorSeleccionado : TextView
    private lateinit var frameProveedores : FrameLayout
    private lateinit var floatingAddButton : FloatingActionButton

    val mAdapter: RecyclerAdapterProveedores = RecyclerAdapterProveedores()
    var listaProveedores: MutableList<Proveedor> = mutableListOf()
    private lateinit var proveedorSeleccionado : Proveedor
    private lateinit var miContexto : Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_proveedores, container, false)

        // Views
        nombreProveedorSeleccionado = v.findViewById(R.id.nombreProveedorSeleccionado) as TextView
        codigoProveedorSeleccionado = v.findViewById(R.id.codigoProveedorSeleccionado) as TextView
        frameProveedores = v.findViewById(R.id.frameProveedores) as FrameLayout
        floatingAddButton = v.findViewById(R.id.floatingActionButtonAdd) as FloatingActionButton

        // Contexto
        miContexto = v.context

        // Instanciar Base de Datos SQLite
        databaseHelper = DatabaseHelper(activity!!.applicationContext)
        db = databaseHelper.writableDatabase
        bitmapFoto = BitmapFactory.decodeResource(activity!!.resources, R.drawable.nophoto)

        // Recycler View
        listaProveedores = databaseHelper.obtenerProveedores(db,"","")
        proveedorSeleccionado = listaProveedores.first()
        //actualizarProductoSeleccionado()

        mRecyclerView = v.findViewById(R.id.recycler_proveedoresRV) as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(v.context)

        // Recycler adapter
        mAdapter.RecyclerAdapter(listaProveedores, v.context, this)
        mRecyclerView.adapter = mAdapter

        // Boton añadir proveedor
        floatingAddButton.setOnClickListener {
            editarProveedor(v, true, Proveedor("","","","","","",0,"","","",""))
        }


        // Buscar por nombre producto
        buscarNombre = v.findViewById(R.id.buscarNombreProveedor) as EditText
        buscarNombre.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                listaProveedores.clear()
                listaProveedores.addAll(databaseHelper.obtenerProveedores(db, buscarNombre.text.toString(), ""))
                mAdapter.notifyDataSetChanged()
                proveedorSeleccionado = listaProveedores.first()
                actualizarProveedorSeleccionado(proveedorSeleccionado)
            }
        })

        buscarCodigo = v.findViewById(R.id.buscarCodigoProveedor) as EditText
        buscarCodigo.addTextChangedListener(object  : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                listaProveedores.clear()
                listaProveedores.addAll(databaseHelper.obtenerProveedores(db, "", buscarCodigo.text.toString()))
                mAdapter.notifyDataSetChanged()
                proveedorSeleccionado = listaProveedores.first()
                actualizarProveedorSeleccionado(proveedorSeleccionado)
            }
        })


        // Swipe para borrar o editar proveedores
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                Log.d("Miapp" , "Mover fila")
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (direction == ItemTouchHelper.LEFT) {
                    // Swipe hacia la izquierda editar
                    editarProveedor(v, false, listaProveedores.get(position))
                } else {
                    // Swipe hacia la derecha borrar
                    showDialogConfirmarBorrar(position)

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
                    icon = ContextCompat.getDrawable(mAdapter.context, R.drawable.ic_baseline_create_24)
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

        return v
    }

    private fun editarProveedor(v: View?, nuevo: Boolean, proveedor: Proveedor) {
        val dialog = Dialog(miContexto , android.R.style.Theme_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.crear_proveedor)

        val encabezadoTV = dialog.findViewById(R.id.titulo_dialogo_proveedor) as TextView
        val codigoProveedorTV = dialog.findViewById(R.id.codigoProveedorTV) as TextView
        val nombreProveedorET = dialog.findViewById(R.id.nombreProveedorET) as EditText
        val apellidosProveedorET = dialog.findViewById(R.id.apellidosProveedorET) as EditText
        val direccionProveedorET = dialog.findViewById(R.id.direccionProveedorET) as EditText
        val ciudadProveedorET = dialog.findViewById(R.id.ciudadProveedorET) as EditText
        val provinciaProveedorET = dialog.findViewById(R.id.provinciaProveedorET) as EditText
        val cpProveedorET = dialog.findViewById(R.id.cpProveedorET) as EditText
        val telefonoProveedorET = dialog.findViewById(R.id.telefonoProveedorET) as EditText
        val CIFProveedorET = dialog.findViewById(R.id.CIFProveedorET) as EditText
        val emailProveedorET = dialog.findViewById(R.id.emailProveedorET) as EditText
        val referenciaProveedorET = dialog.findViewById(R.id.referenciaProveedorET) as EditText
        val botonAceptar = dialog.findViewById(R.id.boton_guardar_proveedor) as Button
        val botonCancelar = dialog.findViewById(R.id.boton_cancelar_proveedor) as Button

        var codigoProveedor = 0
        var existe : Boolean

        if (nuevo) {
            // DAR UN CODIGO DE CLIENTE QUE NO EXISTA EN LA BD
            do {
                codigoProveedor++
                existe = false
                for (n in 0..listaProveedores.size-1) {
                    if (listaProveedores.get(n).idProveedor.toInt() == codigoProveedor) {
                        existe = true
                        break
                    }
                }
            } while (existe)
            encabezadoTV.setText("Nuevo Proveedor")
            proveedor.idProveedor = codigoProveedor.toString()
            codigoProveedorTV.setText(codigoProveedor.toString())
        } else {
            encabezadoTV.setText("Editar Proveedor")
            codigoProveedorTV.setText(proveedor.idProveedor)
            nombreProveedorET.setText(proveedor.nombreProveedor)
            apellidosProveedorET.setText(proveedor.nombre2Proveedor)
            direccionProveedorET.setText(proveedor.direccionProveedor)
            ciudadProveedorET.setText(proveedor.ciudadProveedor)
            provinciaProveedorET.setText(proveedor.provinciaProveedor)
            cpProveedorET.setText(proveedor.cpProveedor.toString())
            telefonoProveedorET.setText(proveedor.telefonoProveedor.toString())
            CIFProveedorET.setText(proveedor.cifProveedor.toString())
            emailProveedorET.setText(proveedor.emailProveedor)
            referenciaProveedorET.setText(proveedor.referenciaProveedor)
        }

        botonAceptar.setOnClickListener {
            if (nombreProveedorET.text.toString() != "" && telefonoProveedorET.text.toString() != "" )  {
                // Guardar el proveedor
                if (apellidosProveedorET.text.toString() == "") apellidosProveedorET.setText(".")
                if (direccionProveedorET.text.toString() == "") direccionProveedorET.setText(".")
                if (ciudadProveedorET.text.toString() == "") ciudadProveedorET.setText(".")
                if (provinciaProveedorET.text.toString() == "") provinciaProveedorET.setText(".")
                if (cpProveedorET.text.toString() == "") cpProveedorET.setText("28000")
                if (CIFProveedorET.text.toString() == "") CIFProveedorET.setText("0")
                if (emailProveedorET.text.toString() == "") emailProveedorET.setText("mail@mail.com")
                if (referenciaProveedorET.text.toString() == "") referenciaProveedorET.setText(".")
                var proveedorTMP = Proveedor (proveedor.idProveedor , nombreProveedorET.text.toString(),apellidosProveedorET.text.toString() , direccionProveedorET.text.toString() , ciudadProveedorET.text.toString() ,provinciaProveedorET.text.toString() ,cpProveedorET.text.toString().toInt() ,telefonoProveedorET.text.toString() , CIFProveedorET.text.toString() ,emailProveedorET.text.toString() , referenciaProveedorET.text.toString())
                databaseHelper.guardarProveedor(db, proveedorTMP)
                actualizarListaProveedores()
                dialog.dismiss()
            } else {
                Toast.makeText(dialog.context, "Al menos rellena el nombre y teléfono del proveedor" , Toast.LENGTH_LONG).show()
            }
        }

        botonCancelar.setOnClickListener {
            mAdapter.notifyDataSetChanged()
            dialog.dismiss()
        }

        dialog.show()

    }

    fun actualizarListaProveedores(){
        listaProveedores.clear()
        listaProveedores.addAll(databaseHelper.obtenerProveedores(db,"",""))
        mAdapter.notifyDataSetChanged()
    }
    
    override fun itemListClicked(proveedor: Proveedor, itemView: View) {
        actualizarProveedorSeleccionado(proveedor)
    }
    
    
    private fun actualizarProveedorSeleccionado(proveedor : Proveedor) {
        proveedorSeleccionado = proveedor
        nombreProveedorSeleccionado.setText(proveedor.nombreProveedor + " " + proveedor.nombre2Proveedor)
        codigoProveedorSeleccionado.setText(proveedor.idProveedor)
    }


    private fun showDialogConfirmarBorrar(position: Int) {
        val dialog = Dialog(miContexto)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.confirmar_layout)

        val textoConfirmar = dialog.findViewById(R.id.texto_confirmarTV) as TextView
        val botonConfirmarOK = dialog.findViewById(R.id.boton_confirmar_OK) as Button
        val botonConfirmarCancelar = dialog.findViewById(R.id.boton_confirmar_CANCELAR) as Button

        textoConfirmar.setText("Confirmar borrar el proveedor. (No se puede deshacer)")
        botonConfirmarOK.setOnClickListener {
            databaseHelper.borrarProveedor(db, listaProveedores.get(position))
            Toast.makeText(dialog.context, "Proveedor Borrado", Toast.LENGTH_LONG).show()
            listaProveedores.remove(listaProveedores.get(position))
            mAdapter.notifyItemRemoved(position)
            dialog.dismiss()
        }

        botonConfirmarCancelar.setOnClickListener {
            mAdapter.notifyDataSetChanged()
            dialog.dismiss()
        }

        dialog.show()
    }


}