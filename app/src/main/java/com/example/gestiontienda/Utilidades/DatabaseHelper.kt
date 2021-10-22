package com.example.gestiontienda.Utilidades

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/*
        TABLAS:
        ------------------------------------------------------------------------------------------

        TIENDA:    NOMBRE_TIENDA
                   DIRECCION
                   TELEFONO
                   EMAIL
                   CIF
                   OTROS_DATOS

        PRODUCTOS: ID
                   NOMBRE
                   RUTA_FOTO
                   STOCK
                   PRECIO_COMPRA
                   PRECIO_VENTA
                   IVA

        CLIENTES: ID
                  NOMBRE
                  APELLIDOS
                  DIRECCION
                  CIUDAD
                  PROVINCIA
                  CODIGO_POSTAL
                  CIF
                  TELEFONO
                  EMAIL
                  REFERENCIA

        PROVEEDORES: ID
                  NOMBRE
                  APELLIDOS
                  DIRECCION
                  CIUDAD
                  PROVINCIA
                  CODIGO_POSTAL
                  CIF
                  TELEFONO
                  EMAIL
                  REFERENCIA

        ENTRADAS: ID
                  TIPO_ENTRADA (COMPRA, DEVOLUCIÓN, AJUSTE_STOCK)
                  PRODUCTO
                  UNIDADES
                  PRECIO_ENTRADA
                  IMPORTE
                  CARGO (BANCO, CAJA, VALOR_STOCK)
                  IVA

        SALIDAS: ID
                 TIPO SALIDA (VENTA, AJUSTE_STOCK)
                 PRODUCTO
                 UNIDADES
                 IMPORTE
                 PRECIO_VENTA
                 CARGO (BANCO, CAJA, VALOR_STOCK)
                 IVA

        CAJA: SALDO

        BANCO: SALDO



 */

class DatabaseHelper (context: Context) : SQLiteOpenHelper(context, "DB_AVENTURAS", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("MIAPP", "Crear BD")
        val CREATE_TABLE_PRODUCTOS = "CREATE TABLE PRODUCTOS (ID TEXT, NOMBRE TEXT, CREADOR TEXT, NOTA INTEGER, PUBLICADO BOOLEAN, VISITAS INTEGER, USUARIO TEXT)"
        val CREATE_TABLE_SALIDAS = "CREATE TABLE SALIDAS ( IDAVENTURA TEXT, ID TEXT, CAPITULOPADRE TEXT, CAPITULO1 TEXT, TEXTOOPCION1 TEXT,CAPITULO2 TEXT, TEXTOOPCION2 TEXT, TEXTOCAPITULO TEXT, IMAGENCAPITULO TEXT, FINHISTORIA BOOLEAN)"
        val CREATE_TABLE_ENTRADAS = "CREATE TABLE ENTRADAS (IDAVENTURA TEXT)"
        db!!.execSQL(CREATE_TABLE_PRODUCTOS)
        db!!.execSQL(CREATE_TABLE_SALIDAS)
        db!!.execSQL(CREATE_TABLE_ENTRADAS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}