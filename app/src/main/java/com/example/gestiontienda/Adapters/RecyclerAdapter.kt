package com.example.gestiontienda.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestiontienda.Entidades.Producto
import com.example.gestiontienda.Interfaces.OnItemListClicked
import com.example.gestiontienda.R
import com.example.gestiontienda.Utilidades.ImagesHelper

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    var productos: MutableList<Producto> = ArrayList()
    lateinit var context: Context
    lateinit var listener : OnItemListClicked

    fun RecyclerAdapter(productos: MutableList<Producto>, context: Context, onItemListClicked: OnItemListClicked) {
        this.productos = productos
        this.context = context
        this.listener = onItemListClicked
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val item = productos.get(position)
        holder.bind(item , context)
        holder.itemView.setOnClickListener {
            listener.itemListClicked(item , holder.itemView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewHolder = ViewHolder(layoutInflater.inflate(R.layout.fila_producto, parent, false))
        return viewHolder
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)   {
        val nombreProducto = view.findViewById(R.id.nombre_cliente_proveedor_fila) as TextView
        val precioCompraProducto = view.findViewById(R.id.preciocompra_fila) as TextView
        val precioVentaProducto = view.findViewById(R.id.precioventa_fila) as TextView
        val stockProducto = view.findViewById(R.id.stockproducto_fila) as TextView
        val ivaProducto = view.findViewById(R.id.ivaproducto_fila) as TextView
        val imagenProducto = view.findViewById(R.id.imagen_fila) as ImageView
        val codigoProducto = view.findViewById(R.id.codigo_cliente_proveedorTV) as TextView
      //  val linearBase = view.findViewById(R.id.linear_base_fila) as LinearLayout


        fun bind(producto: Producto, context: Context) {
            val imagesHelper = ImagesHelper(context)
            nombreProducto.text = producto.nombreProducto
            precioCompraProducto.text = String.format("%.2f" , producto.precioCompraProducto).replace('.',',')
            precioVentaProducto.text = String.format("%.2f" , producto.precioVentaProducto).replace('.',',')
            stockProducto.text =  String.format("%.3f" , producto.stockProducto).replace('.',',')
            ivaProducto.text = producto.ivaProducto.toString()
            codigoProducto.text = producto.codigoProducto

            val imagen = imagesHelper.recuperarImagenMemoriaInterna(producto.rutafotoProducto)
            if (imagen == null) {
                imagenProducto.setImageResource(R.drawable.nophoto)
            } else {
                imagenProducto.setImageBitmap(imagen)
            }

           // linearBase.setBackgroundColor(Color.rgb((0..255).shuffled().last(),(0..255).shuffled().last(),(0..255).shuffled().last()))
        }
    }

}
