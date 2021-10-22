package com.example.gestiontienda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private val adapter by lazy { TiendaViewpagerAdapter(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pager = findViewById(R.id.pager) as ViewPager2
        val tabLayout = findViewById(R.id.tabLayout) as TabLayout

        pager.adapter = adapter

        val tabLayoutMediator = TabLayoutMediator(tabLayout, pager ,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0-> tab.text = "Gestión"
                    1-> tab.text = "Ventas"
                    2-> tab.text = "Productos"
                }

            })

        tabLayoutMediator.attach()

    }
}