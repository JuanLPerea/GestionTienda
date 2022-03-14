package com.example.gestiontienda.Utilidades

import android.app.Dialog
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import com.example.gestiontienda.Entidades.Producto
import com.example.gestiontienda.Entidades.Tienda
import com.example.gestiontienda.R

/*
        TABLAS:
        ------------------------------------------------------------------------------------------

        TIENDA:    NOMBRE_TIENDA
                   DIRECCION
                   TELEFONO
                   EMAIL
                   CIF
                   OTROS_DATOS

        PRODUCTOS:
                   NOMBRE
                   CODIGO
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

class DatabaseHelper (context: Context) : SQLiteOpenHelper(context, "DB_TIENDA", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("Miapp", "Crear BD")
        val CREATE_TABLE_TIENDA = "CREATE TABLE TIENDA (NOMBRETIENDA TEXT, DIRECCION TEXT, TELEFONO TEXT, EMAIL TEXT, CIF TEXT, OTROSDATOS TEXT)"
        val CREATE_TABLE_PRODUCTOS = "CREATE TABLE PRODUCTOS (NOMBREPRODUCTO TEXT, CODIGOPRODUCTO TEXT, RUTAFOTOPRODUCTO TEXT, STOCK INTEGER, PRECIOCOMPRAPRODUCTO INTEGER, PRECIOVENTAPRODUCTO INTEGER, IVAPRODUCTO INTEGER)"
        val CREATE_TABLE_CLIENTES = "CREATE TABLE CLIENTES (IDCLIENTE ID, NOMBRECLIENTE TEXT, APELLIDOSCLIENTE TEXT, DIRECCONCLIENTE TEXT, CIUDADCLIENTE TEXT, PROVINCIACLIENTE TEXT, CODIGOPOSTALCLIENTE TEXT, CIFCLIENTE TEXT, TELEFONOCLIENTE TEXT, EMAILCLIENTE TEXT, REFERENCIACLIENTE TEXT)"
        val CREATE_TABLE_PROVEEDORES = "CREATE TABLE PROVEEDORES (IDPROVEEDOR ID, NOMBREPROVEEDOR TEXT, APELLIDOSPROVEEDOR TEXT, DIRECCIONPROVEEDOR TEXT, CIUDADPROVEEDOR TEXT, PROVINCIAPROVEEDOR TEXT, CODIGOPOSTALPROVEEDOR TEXT, CIFPROVEEDOR TEXT, TELEFONOPROVEDOR TEXT, EMAILPROVEEDOR TEXT, REFERENCIAPROVEEDOR TEXT)"
        val CREATE_TABLE_SALIDAS = "CREATE TABLE SALIDAS (IDSALIDA ID, TIPOSALIDA TEXT, PRODUCTOID TEXT, UNIDADES INTEGER, IMPORTE FLOAT, PRECIOVENTA FLOAT, CARGO TEXT, IVA INTEGER)"
        val CREATE_TABLE_ENTRADAS = "CREATE TABLE ENTRADAS (IDENTRADA ID, TIPOENTRADA TEXT, PRODUCTOID TEXT, UNIDADES INTEGER, IMPORTE FLOAT, PRECIOCOMPRA FLOAT, CARGO TEXT, IVA INTEGER )"
        val CREATE_TABLE_CAJA = "CREATE TABLE CAJA (CAJAID ID, SALDO FLOAT)"
        val CREATE_TABLE_BANCO = "CREATE TABLE BANCO (BANCOID ID, SALDO FLOAT)"
        db!!.execSQL(CREATE_TABLE_TIENDA)
        db!!.execSQL(CREATE_TABLE_PRODUCTOS)
        db!!.execSQL(CREATE_TABLE_CLIENTES)
        db!!.execSQL(CREATE_TABLE_PROVEEDORES)
        db!!.execSQL(CREATE_TABLE_SALIDAS)
        db!!.execSQL(CREATE_TABLE_ENTRADAS)
        db!!.execSQL(CREATE_TABLE_CAJA)
        db!!.execSQL(CREATE_TABLE_BANCO)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE_TIENDA = "DROP TABLE IF EXISTS TIENDA"
        db!!.execSQL(DROP_TABLE_TIENDA)
        val DROP_TABLE_PRODUCTOS = "DROP TABLE IF EXISTS PRODUCTOS"
        db!!.execSQL(DROP_TABLE_PRODUCTOS)
        val DROP_TABLE_CLIENTES = "DROP TABLE IF EXISTS CLIENTES"
        db!!.execSQL(DROP_TABLE_CLIENTES)
        val DROP_TABLE_PROVEEDORES = "DROP TABLE IF EXISTS PROVEEDORES"
        db!!.execSQL(DROP_TABLE_PROVEEDORES)
        val DROP_TABLE_ENTRADAS = "DROP TABLE IF EXISTS ENTRADAS"
        db!!.execSQL(DROP_TABLE_ENTRADAS)
        val DROP_TABLE_SALIDAS = "DROP TABLE IF EXISTS SALIDAS"
        db!!.execSQL(DROP_TABLE_SALIDAS)
        val DROP_TABLE_CAJA = "DROP TABLE IF EXISTS CAJA"
        db!!.execSQL(DROP_TABLE_CAJA)
        val DROP_TABLE_BANCO = "DROP TABLE IF EXISTS BANCO"
        db!!.execSQL(DROP_TABLE_BANCO)
        onCreate(db)
    }

    fun guardarTienda (db: SQLiteDatabase, nombreTienda : String , direccionTienda : String , telefonoTienda : String, emailTienda : String, cifTienda : String, otrosDatosTienda : String ) {
        val datosBruto = db.rawQuery("SELECT * FROM TIENDA", null)
        if (datosBruto!!.moveToFirst()) {
            val nombreTiendaTmp = datosBruto.getString(datosBruto.getColumnIndex("NOMBRETIENDA"))
            val UPDATE_TIENDA = "UPDATE TIENDA SET NOMBRETIENDA = '" + nombreTienda + "' , DIRECCION = '" + direccionTienda + "' , TELEFONO = '" +  telefonoTienda + "' , EMAIL = '" + emailTienda +"', CIF = '" + cifTienda + "' , OTROSDATOS = '"  + otrosDatosTienda  + "' WHERE NOMBRETIENDA = '" + nombreTiendaTmp + "'"
            db!!.execSQL(UPDATE_TIENDA)
        } else {

            val CREATE_TIENDA = "INSERT INTO TIENDA VALUES('" + nombreTienda + "' , '" + direccionTienda + "' , '"  + telefonoTienda + "' , '" + emailTienda + "' , '" + cifTienda + "' , '" + otrosDatosTienda + "')"
            db!!.execSQL(CREATE_TIENDA)
        }
    }

    fun crearProducto (db: SQLiteDatabase, producto: Producto) {
        val CREATE_TIENDA = "INSERT INTO PRODUCTOS VALUES('" + producto.nombreProducto + "' , '" + producto.codigoProducto + "' , '"  + producto.rutafotoProducto + "' , '" + producto.stockProducto + "' , '" + producto.precioCompraProducto + "' , '" + producto.precioVentaProducto + "' , '" + producto.ivaProducto + "')"
        db!!.execSQL(CREATE_TIENDA)
        Log.d("Miapp" , "Nuevo producto añadido")
    }

    fun productoExiste (db: SQLiteDatabase, codigoProducto : String) : Boolean {
        var existe = false
        val datosBruto = db.rawQuery("SELECT * FROM PRODUCTOS WHERE CODIGOPRODUCTO ='" + codigoProducto + "'", null)
        if (datosBruto!!.moveToFirst()) {
            Log.d("Miapp" , "El producto ya existe")
            existe = true
        }
        return existe
    }



    fun obtenerTienda(db: SQLiteDatabase): Tienda {
        var mitienda = Tienda("","","","","","")

        val datosBruto = db.rawQuery("SELECT * FROM TIENDA", null)
        if (datosBruto!!.moveToFirst()) {
            val nombreTiendaTmp = datosBruto.getString(datosBruto.getColumnIndex("NOMBRETIENDA"))
            val direccionTiendaTmp = datosBruto.getString(datosBruto.getColumnIndex("DIRECCION"))
            val telefonoTiendaTmp = datosBruto.getString(datosBruto.getColumnIndex("TELEFONO"))
            val emailTiendaTmp = datosBruto.getString(datosBruto.getColumnIndex("EMAIL"))
            val cifTiendaTmp = datosBruto.getString(datosBruto.getColumnIndex("CIF"))
            val otrosdatosTiendaTmp = datosBruto.getString(datosBruto.getColumnIndex("OTROSDATOS"))

            mitienda.nombreTienda = nombreTiendaTmp
            mitienda.direccionTienda = direccionTiendaTmp
            mitienda.telefonoTienda = telefonoTiendaTmp
            mitienda.emailTienda = emailTiendaTmp
            mitienda.cifTienda = cifTiendaTmp
            mitienda.otrosDatosTienda = otrosdatosTiendaTmp
        }
        return mitienda
    }

    fun obtenerProductos (db : SQLiteDatabase, nombreFiltrar : String, codigoFiltrar : String) : MutableList<Producto> {
        var misProductos : MutableList<Producto> = mutableListOf()

        val datosBruto = db.rawQuery("SELECT * FROM PRODUCTOS", null)

        //(NOMBREPRODUCTO TEXT, CODIGOPRODUCTO TEXT, RUTAFOTOPRODUCTO TEXT, STOCK INTEGER, PRECIOCOMPRAPRODUCTO INTEGER, PRECIOVENTAPRODUCTO INTEGER, IVAPRODUCTO INTEGER)"

        if (datosBruto!!.moveToFirst()) {
            do {
                val nombreTMP = datosBruto.getString(datosBruto.getColumnIndex("NOMBREPRODUCTO"))
                val codigoTMP = datosBruto.getString(datosBruto.getColumnIndex("CODIGOPRODUCTO"))
                val rutafotoTMP = datosBruto.getString(datosBruto.getColumnIndex("RUTAFOTOPRODUCTO"))
                val precioCompraTMP = datosBruto.getInt(datosBruto.getColumnIndex("PRECIOCOMPRAPRODUCTO"))
                val precioVentaTMP = datosBruto.getInt(datosBruto.getColumnIndex("PRECIOVENTAPRODUCTO"))
                val ivaTMP = datosBruto.getInt(datosBruto.getColumnIndex("IVAPRODUCTO"))

                val productoTMP = Producto("","","",0,0f,0f, 0)
                productoTMP.nombreProducto = nombreTMP
                productoTMP.codigoProducto = codigoTMP
                productoTMP.rutafotoProducto = rutafotoTMP
                productoTMP.precioCompraProducto = precioCompraTMP.toFloat()
                productoTMP.precioVentaProducto = precioVentaTMP.toFloat()
                productoTMP.ivaProducto = ivaTMP

                // Filtrar ...........................
                //
                var filtrarAdd = false
                // Si no filtramos por ningún criterio, añadir siempre
                if (nombreFiltrar.equals("") && codigoFiltrar.equals("")) {
                    filtrarAdd = true
                } else {
                    // si filtramos por nombre, añadir si contiene el texto buscado
                    if (!nombreFiltrar.equals("") && productoTMP.nombreProducto.uppercase().contains(nombreFiltrar.uppercase())) {
                        filtrarAdd = true
                    } else {
                        // si filtramos por codigo producto, añadir si contiene el texto buscado
                        if (!codigoFiltrar.equals("") && productoTMP.codigoProducto.uppercase().contains(codigoFiltrar.uppercase())) {
                            filtrarAdd = true
                        }
                    }

                }
                if (filtrarAdd) misProductos.add(productoTMP)
            } while (datosBruto.moveToNext())
        }
        datosBruto.close()

        if (misProductos.size == 0) {
            misProductos.add(Producto("No se encuentra Producto", "","",0,00f,0f,0))
        }

        return misProductos
    }


}