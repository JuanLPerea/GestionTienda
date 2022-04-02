package com.example.gestiontienda.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestiontienda.Entidades.Cliente
import com.example.gestiontienda.Interfaces.OnClienteListClicked
import com.example.gestiontienda.R

class RecyclerAdapterClientes: RecyclerView.Adapter<RecyclerAdapterClientes.ViewHolder>() {

    var clientes: MutableList<Cliente> = ArrayList()
    lateinit var context: Context
    lateinit var listener : OnClienteListClicked

    fun RecyclerAdapter(clientes: MutableList<Cliente>, context: Context, onClienteListClicked: OnClienteListClicked) {
        this.clientes = clientes
        this.context = context
        this.listener = onClienteListClicked
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewHolder = RecyclerAdapterClientes.ViewHolder(layoutInflater.inflate(R.layout.fila_cliente_proveedor, parent, false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = clientes.get(position)
        holder.bind(item , context)
        holder.itemView.setOnClickListener {
            listener.itemListClicked(item , holder.itemView)
        }
    }

    override fun getItemCount(): Int {
        return clientes.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)   {
        val nombreCliente = view.findViewById(R.id.nombre_cliente_proveedor_fila) as TextView
        val idCliente = view.findViewById(R.id.codigo_cliente_proveedorTV) as TextView

        fun bind(cliente: Cliente, context: Context) {
            nombreCliente.text = cliente.nombreCliente + " " + cliente.nombre2Cliente
            idCliente.text = cliente.idCliente
        }
    }
}