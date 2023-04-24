package com.example.gestiontienda.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestiontienda.Interfaces.OnCopiaListClicked
import com.example.gestiontienda.R

class RecyclerCopiaAdapter : RecyclerView.Adapter<RecyclerCopiaAdapter.ViewHolder>() {

    var archivos: MutableList<String> = ArrayList()
    lateinit var v: TextView
    lateinit var listener : OnCopiaListClicked

    fun RecyclerCopiaAdapter(archivos: MutableList<String>, onItemListClicked: OnCopiaListClicked, v : TextView) {
        this.archivos = archivos
        this.listener = onItemListClicked
        this.v = v
    }

    override fun onBindViewHolder(holder: RecyclerCopiaAdapter.ViewHolder, position: Int) {
        val item = archivos.get(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener.itemListClicked(item, v)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerCopiaAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewHolder = ViewHolder(layoutInflater.inflate(R.layout.fila_archivo_copia, parent, false))
        return viewHolder
    }

    override fun getItemCount(): Int {
        return archivos.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)   {
        val nombreArchivo = view.findViewById(R.id.archivo_item) as TextView

        fun bind(archivo: String) {
            nombreArchivo.text = archivo
        }
    }

}