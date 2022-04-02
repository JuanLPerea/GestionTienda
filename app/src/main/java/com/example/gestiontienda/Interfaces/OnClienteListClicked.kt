package com.example.gestiontienda.Interfaces

import android.view.View
import com.example.gestiontienda.Entidades.Cliente
import com.example.gestiontienda.Entidades.Producto

interface OnClienteListClicked {
    fun itemListClicked (cliente: Cliente, itemView: View)
}