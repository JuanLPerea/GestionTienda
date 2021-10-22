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
import com.example.gestiontienda.Fragments.MenuFragment
import com.example.gestiontienda.Fragments.ProductosFragment
import com.example.gestiontienda.Fragments.VentasFragment

class TiendaViewpagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {MenuFragment()}
            1 -> {VentasFragment()}
            2 -> {ProductosFragment()}
            else -> {MenuFragment()}
        }

    }
}


