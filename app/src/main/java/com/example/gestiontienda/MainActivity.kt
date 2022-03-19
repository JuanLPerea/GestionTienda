package com.example.gestiontienda

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.example.gestiontienda.Entidades.Tienda
import com.example.gestiontienda.Utilidades.DatabaseHelper
import com.example.gestiontienda.Utilidades.ImagesHelper
import com.example.gestiontienda.Utilidades.Prefs
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private val adapter by lazy { TiendaViewpagerAdapter(this)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ImagesHelper(applicationContext).desactivarModoEstricto()

        val pager = findViewById(R.id.pager) as ViewPager2
        val tabLayout = findViewById(R.id.tabLayout) as TabLayout

        pager.adapter = adapter

        val tabLayoutMediator = TabLayoutMediator(tabLayout, pager ,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0-> {
                        tab.icon = resources.getDrawable(R.drawable.ic_baseline_home_24)
                    }
                    1-> {
                        tab.icon = resources.getDrawable((R.drawable.ic_baseline_shopping_cart_24))
                    }
                    2-> {
                        tab.icon = resources.getDrawable((R.drawable.ic_baseline_store_24))
                    }
                    3-> {
                        tab.icon = resources.getDrawable((R.drawable.ic_baseline_person_24))
                    }
                    4 -> {
                        tab.icon = resources.getDrawable((R.drawable.ic_baseline_local_shipping_24))
                    }
                }

            })

        tabLayoutMediator.attach()

    }


}