package com.example.gestiontienda.Interfaces
import android.view.View
import com.example.gestiontienda.Entidades.Producto

interface OnItemListClicked {
    fun itemListClicked (producto : Producto, itemView : View)
}