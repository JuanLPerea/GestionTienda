package com.example.gestiontienda.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestiontienda.Entidades.Proveedor
import com.example.gestiontienda.Interfaces.OnProveedorListClicked
import com.example.gestiontienda.R

class RecyclerAdapterProveedores: RecyclerView.Adapter<RecyclerAdapterProveedores.ViewHolder>() {

    var proveedores: MutableList<Proveedor> = ArrayList()
    lateinit var context: Context
    lateinit var listener : OnProveedorListClicked

    fun RecyclerAdapter(proveedores: MutableList<Proveedor>, context: Context, onProveedorListClicked: OnProveedorListClicked) {
        this.proveedores = proveedores
        this.context = context
        this.listener = onProveedorListClicked
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewHolder = RecyclerAdapterProveedores.ViewHolder(layoutInflater.inflate(R.layout.fila_cliente_proveedor, parent, false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = proveedores.get(position)
        holder.bind(item , context)
        holder.itemView.setOnClickListener {
            listener.itemListClicked(item , holder.itemView)
        }
    }

    override fun getItemCount(): Int {
        return proveedores.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)   {
        val nombreProveedor = view.findViewById(R.id.nombre_cliente_proveedor_fila) as TextView
        val idProveedor = view.findViewById(R.id.codigo_cliente_proveedorTV) as TextView

        fun bind(cliente: Proveedor, context: Context) {
            nombreProveedor.text = cliente.nombreProveedor + " " + cliente.nombre2Proveedor
            idProveedor.text = cliente.idProveedor
        }
    }
}