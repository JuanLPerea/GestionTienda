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
import com.example.gestiontienda.R
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestiontienda.Adapters.RecyclerAdapterClientes
import com.example.gestiontienda.Entidades.Cliente
import com.example.gestiontienda.Interfaces.OnClienteListClicked
import com.example.gestiontienda.Utilidades.DatabaseHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ClientesFragment : Fragment() , OnClienteListClicked {

    lateinit var mRecyclerView: RecyclerView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var vistaFragment : Dialog
    private lateinit var bitmapFoto : Bitmap
    private lateinit var buscarNombre : EditText
    private lateinit var buscarCodigo : EditText
    private lateinit var nombreClienteSeleccionado : TextView
    private lateinit var codigoClienteSeleccionado : TextView
    private lateinit var frameClientes : FrameLayout
    private lateinit var floatingAddButton : FloatingActionButton

    val mAdapter: RecyclerAdapterClientes = RecyclerAdapterClientes()
    var listaClientes: MutableList<Cliente> = mutableListOf()
    private lateinit var clienteSeleccionado : Cliente
    private lateinit var miContexto : Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v =  inflater.inflate(R.layout.fragment_clientes, container, false)

        // Views
        nombreClienteSeleccionado = v.findViewById(R.id.nombreClienteSeleccionado) as TextView
        codigoClienteSeleccionado = v.findViewById(R.id.codigoClienteSeleccionado) as TextView
        frameClientes = v.findViewById(R.id.frameClientes) as FrameLayout
        floatingAddButton = v.findViewById(R.id.floatingActionButtonAdd) as FloatingActionButton

        // Contexto
        miContexto = v.context

        // Instanciar Base de Datos SQLite
        databaseHelper = DatabaseHelper(activity!!.applicationContext)
        db = databaseHelper.writableDatabase
        bitmapFoto = BitmapFactory.decodeResource(activity!!.resources, R.drawable.nophoto)

        // Recycler View
        listaClientes = databaseHelper.obtenerClientes(db,"","")
        clienteSeleccionado = listaClientes.first()
        //actualizarProductoSeleccionado()

        mRecyclerView = v.findViewById(R.id.recycler_clientesRV) as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(v.context)

        // Recycler adapter
        mAdapter.RecyclerAdapter(listaClientes, v.context, this)
        mRecyclerView.adapter = mAdapter

        // Boton añadir cliente
        floatingAddButton.setOnClickListener {
            editarCliente(v, true, Cliente("","","","","","",0,"","","",""))
        }

        // Buscar por nombre producto
        buscarNombre = v.findViewById(R.id.buscarNombreCliente) as EditText
        buscarNombre.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                listaClientes.clear()
                listaClientes.addAll(databaseHelper.obtenerClientes(db, buscarNombre.text.toString(), ""))
                mAdapter.notifyDataSetChanged()
                clienteSeleccionado = listaClientes.first()
                actualizarClienteSeleccionado(clienteSeleccionado)
            }
        })

        buscarCodigo = v.findViewById(R.id.buscarCodigoCliente) as EditText
        buscarCodigo.addTextChangedListener(object  : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                listaClientes.clear()
                listaClientes.addAll(databaseHelper.obtenerClientes(db, "", buscarCodigo.text.toString()))
                mAdapter.notifyDataSetChanged()
                clienteSeleccionado = listaClientes.first()
                actualizarClienteSeleccionado(clienteSeleccionado)
            }
        })

        // Swipe para borrar o editar clientes
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                Log.d("Miapp" , "Mover fila")
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (direction == ItemTouchHelper.LEFT) {
                    // Swipe hacia la izquierda editar
                    editarCliente(v, false, listaClientes.get(position))
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

    private fun showDialogConfirmarBorrar(position: Int) {
        val dialog = Dialog(miContexto)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.confirmar_layout)

        val textoConfirmar = dialog.findViewById(R.id.texto_confirmarTV) as TextView
        val botonConfirmarOK = dialog.findViewById(R.id.boton_confirmar_OK) as Button
        val botonConfirmarCancelar = dialog.findViewById(R.id.boton_confirmar_CANCELAR) as Button

        textoConfirmar.setText("Confirmar borrar el cliente. (No se puede deshacer)")
        botonConfirmarOK.setOnClickListener {
            databaseHelper.borrarCliente(db, listaClientes.get(position))
            Toast.makeText(dialog.context, "Cliente Borrado", Toast.LENGTH_LONG).show()
            listaClientes.remove(listaClientes.get(position))
            mAdapter.notifyItemRemoved(position)
            dialog.dismiss()
        }

        botonConfirmarCancelar.setOnClickListener {
            mAdapter.notifyDataSetChanged()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun editarCliente(v: View?, nuevo: Boolean, cliente: Cliente) {
        val dialog = Dialog(miContexto , android.R.style.Theme_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.crear_cliente)

        val encabezadoTV = dialog.findViewById(R.id.titulo_dialogo_cliente) as TextView
        val codigoClienteTV = dialog.findViewById(R.id.codigoClienteTV) as TextView
        val nombreClienteET = dialog.findViewById(R.id.nombreClienteET) as EditText
        val apellidosClienteET = dialog.findViewById(R.id.apellidosClienteET) as EditText
        val direccionClienteET = dialog.findViewById(R.id.direccionClienteET) as EditText
        val ciudadClienteET = dialog.findViewById(R.id.ciudadClienteET) as EditText
        val provinciaClienteET = dialog.findViewById(R.id.provinciaClienteET) as EditText
        val cpClienteET = dialog.findViewById(R.id.cpClienteET) as EditText
        val telefonoClienteET = dialog.findViewById(R.id.telefonoClienteET) as EditText
        val CIFClienteET = dialog.findViewById(R.id.CIFClienteET) as EditText
        val emailClienteET = dialog.findViewById(R.id.emailClienteET) as EditText
        val referenciaClienteET = dialog.findViewById(R.id.referenciaClienteET) as EditText
        val botonAceptar = dialog.findViewById(R.id.boton_guardar_cliente) as Button
        val botonCancelar = dialog.findViewById(R.id.boton_cancelar_cliente) as Button

        var codigoCliente = 0
        var existe : Boolean

        if (nuevo) {
            // DAR UN CODIGO DE CLIENTE QUE NO EXISTA EN LA BD
            do {
                codigoCliente++
                existe = false
                for (n in 0..listaClientes.size-1) {
                    if (listaClientes.get(n).idCliente.toInt() == codigoCliente) {
                        existe = true
                        break
                    }
                }
            } while (existe)
            encabezadoTV.setText("Nuevo Cliente")
            cliente.idCliente = codigoCliente.toString()
            codigoClienteTV.setText(codigoCliente.toString())
        } else {
            encabezadoTV.setText("Editar Cliente")
             codigoClienteTV.setText(cliente.idCliente)
             nombreClienteET.setText(cliente.nombreCliente)
             apellidosClienteET.setText(cliente.nombre2Cliente)
             direccionClienteET.setText(cliente.direccionCliente)
             ciudadClienteET.setText(cliente.ciudadCliente)
             provinciaClienteET.setText(cliente.provinciaCliente)
             cpClienteET.setText(cliente.cpCliente.toString())
             telefonoClienteET.setText(cliente.telefonoCliente.toString())
             CIFClienteET.setText(cliente.cifCliente.toString())
             emailClienteET.setText(cliente.emailCliente)
             referenciaClienteET.setText(cliente.referenciaCliente)
        }

        botonAceptar.setOnClickListener {
            if (nombreClienteET.text.toString() != "" && telefonoClienteET.text.toString() != "" )  {
                // Guardar el cliente
                    if (apellidosClienteET.text.toString() == "") apellidosClienteET.setText(".")
                    if (direccionClienteET.text.toString() == "") direccionClienteET.setText(".")
                    if (ciudadClienteET.text.toString() == "") ciudadClienteET.setText(".")
                    if (provinciaClienteET.text.toString() == "") provinciaClienteET.setText(".")
                    if (cpClienteET.text.toString() == "") cpClienteET.setText("28000")
                    if (CIFClienteET.text.toString() == "") CIFClienteET.setText("0")
                    if (emailClienteET.text.toString() == "") emailClienteET.setText("mail@mail.com")
                    if (referenciaClienteET.text.toString() == "") referenciaClienteET.setText(".")
                var clienteTMP = Cliente (cliente.idCliente , nombreClienteET.text.toString(),apellidosClienteET.text.toString() , direccionClienteET.text.toString() , ciudadClienteET.text.toString() ,provinciaClienteET.text.toString() ,cpClienteET.text.toString().toInt() ,telefonoClienteET.text.toString() , CIFClienteET.text.toString() ,emailClienteET.text.toString() , referenciaClienteET.text.toString())
                databaseHelper.guardarCliente(db, clienteTMP)
                actualizarListaClientes()
                dialog.dismiss()
            } else {
                Toast.makeText(dialog.context, "Al menos rellena el nombre y teléfono del cliente" , Toast.LENGTH_LONG).show()
            }
        }

        botonCancelar.setOnClickListener {
            mAdapter.notifyDataSetChanged()
            dialog.dismiss()
        }

        dialog.show()

    }

    private fun actualizarClienteSeleccionado(cliente : Cliente) {
        clienteSeleccionado = cliente
        nombreClienteSeleccionado.setText(cliente.nombreCliente + " " + cliente.nombre2Cliente)
        codigoClienteSeleccionado.setText(cliente.idCliente)
    }



    override fun itemListClicked(cliente: Cliente, itemView: View) {
        actualizarClienteSeleccionado(cliente)
    }

    fun actualizarListaClientes(){
        listaClientes.clear()
        listaClientes.addAll(databaseHelper.obtenerClientes(db,"",""))
        mAdapter.notifyDataSetChanged()
    }

}