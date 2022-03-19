package com.example.gestiontienda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gestiontienda.Fragments.*

class TiendaViewpagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {MenuFragment()}
            1 -> {VentasFragment()}
            2 -> {ProductosFragment()}
            3 -> {ClientesFragment()}
            4 -> {ProveedoresFragment()}
            else -> {MenuFragment()}
        }

    }
}


