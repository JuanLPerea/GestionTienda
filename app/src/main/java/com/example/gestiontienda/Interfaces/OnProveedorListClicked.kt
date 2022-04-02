package com.example.gestiontienda.Interfaces

import android.view.View
import com.example.gestiontienda.Entidades.Cliente
import com.example.gestiontienda.Entidades.Producto
import com.example.gestiontienda.Entidades.Proveedor

interface OnProveedorListClicked {
    fun itemListClicked (proveedor: Proveedor, itemView: View)
}