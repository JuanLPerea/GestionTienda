package com.example.gestiontienda.Utilidades

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.gestiontienda.Entidades.*
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

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
        val CREATE_TABLE_PRODUCTOS = "CREATE TABLE PRODUCTOS (NOMBREPRODUCTO TEXT, CODIGOPRODUCTO TEXT, RUTAFOTOPRODUCTO TEXT, STOCK TEXT, PRECIOCOMPRAPRODUCTO TEXT, PRECIOVENTAPRODUCTO TEXT, IVAPRODUCTO INTEGER, MARGENPRODUCTO TEXT)"
        val CREATE_TABLE_CLIENTES = "CREATE TABLE CLIENTES (IDCLIENTE ID, NOMBRECLIENTE TEXT, APELLIDOSCLIENTE TEXT, DIRECCIONCLIENTE TEXT, CIUDADCLIENTE TEXT, PROVINCIACLIENTE TEXT, CODIGOPOSTALCLIENTE TEXT, CIFCLIENTE TEXT, TELEFONOCLIENTE TEXT, EMAILCLIENTE TEXT, REFERENCIACLIENTE TEXT)"
        val CREATE_TABLE_SALIDAS = "CREATE TABLE SALIDAS (IDSALIDA ID, FECHA TEXT, TIPOSALIDA TEXT, CLIENTEID TEXT , PRODUCTOID TEXT, UNIDADES INTEGER, PRECIOVENTA FLOAT, CARGO TEXT, IVA INTEGER)"
        val CREATE_TABLE_PROVEEDORES = "CREATE TABLE PROVEEDORES (IDPROVEEDOR ID, NOMBREPROVEEDOR TEXT, APELLIDOSPROVEEDOR TEXT, DIRECCIONPROVEEDOR TEXT, CIUDADPROVEEDOR TEXT, PROVINCIAPROVEEDOR TEXT, CODIGOPOSTALPROVEEDOR TEXT, CIFPROVEEDOR TEXT, TELEFONOPROVEDOR TEXT, EMAILPROVEEDOR TEXT, REFERENCIAPROVEEDOR TEXT)"
        val CREATE_TABLE_ENTRADAS = "CREATE TABLE ENTRADAS (IDENTRADA TEXT, FECHA TEXT, TIPOENTRADA TEXT, PROVEEDORID TEXT, PRODUCTOID TEXT, UNIDADES INTEGER, PRECIOCOMPRA FLOAT, CARGO TEXT, IVA INTEGER)"
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

        // CREAR UN PRODUCTO MODELO
        val CREAR_PRODUCTO = "INSERT INTO PRODUCTOS VALUES('PRODUCTO' , '0' , '-' , '0' , '0' , '0' , '0', '0')"
        db!!.execSQL(CREAR_PRODUCTO)

        // CREAR UN PROVEEDOR MODELO
        val CREAR_PROVEEDOR = "INSERT INTO PROVEEDORES VALUES('1' , 'PROVEEDOR' , '-' , '0' , '0' , '0' , '0', '0', '0', '0', '0')"
        db!!.execSQL(CREAR_PROVEEDOR)

        // CREAR UN Cliente MODELO
        val CREAR_CLIENTE = "INSERT INTO CLIENTES VALUES('1' , 'CLIENTE TICKET' , '-' , '0' , '0' , '0' , '0', '0', '0', '0', '0')"
        db!!.execSQL(CREAR_CLIENTE)

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
        if (productoExiste(db, producto.codigoProducto)) {
            actualizarProducto(db, producto)
        }  else {
            val CREAR_PRODUCTO = "INSERT INTO PRODUCTOS VALUES('" + producto.nombreProducto + "' , '" + producto.codigoProducto + "' , '"  + producto.rutafotoProducto + "' , '" + producto.stockProducto + "' , '" + producto.precioCompraProducto + "' , '" + producto.precioVentaProducto + "' , '" + producto.ivaProducto+ "' , '" + producto.margenProducto + "')"
            db!!.execSQL(CREAR_PRODUCTO)
        }
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

    fun obtenerProducto (db: SQLiteDatabase, codigoProducto: String) : Producto {
        var productoTMP = Producto("","","",0f,0f,0f,0, 0f)
        val datosBruto = db.rawQuery("SELECT * FROM PRODUCTOS WHERE CODIGOPRODUCTO ='" + codigoProducto + "'", null)
        if (datosBruto!!.moveToFirst()) {
            val nombreTMP = datosBruto.getString(datosBruto.getColumnIndex("NOMBREPRODUCTO"))
            val codigoTMP = datosBruto.getString(datosBruto.getColumnIndex("CODIGOPRODUCTO"))
            val rutafotoTMP = datosBruto.getString(datosBruto.getColumnIndex("RUTAFOTOPRODUCTO"))
            val stockProductoTMP = datosBruto.getFloat(datosBruto.getColumnIndex("STOCK"))
            val precioCompraTMP = datosBruto.getString(datosBruto.getColumnIndex("PRECIOCOMPRAPRODUCTO"))
            val precioVentaTMP = datosBruto.getString(datosBruto.getColumnIndex("PRECIOVENTAPRODUCTO"))
            val ivaTMP = datosBruto.getInt(datosBruto.getColumnIndex("IVAPRODUCTO"))
            val margenTMP = datosBruto.getString(datosBruto.getColumnIndex("MARGENPRODUCTO"))
            productoTMP.nombreProducto = nombreTMP
            productoTMP.codigoProducto = codigoTMP
            productoTMP.rutafotoProducto = rutafotoTMP
            productoTMP.stockProducto = stockProductoTMP
            productoTMP.precioCompraProducto = precioCompraTMP.toFloat()
            productoTMP.precioVentaProducto = precioVentaTMP.toFloat()
            productoTMP.ivaProducto = ivaTMP
            productoTMP.margenProducto = margenTMP.toFloat()
        }
        return productoTMP
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
                val stockTMP = datosBruto.getFloat(datosBruto.getColumnIndex("STOCK"))
                val precioCompraTMP = datosBruto.getString(datosBruto.getColumnIndex("PRECIOCOMPRAPRODUCTO"))
                val precioVentaTMP = datosBruto.getString(datosBruto.getColumnIndex("PRECIOVENTAPRODUCTO"))
                val ivaTMP = datosBruto.getInt(datosBruto.getColumnIndex("IVAPRODUCTO"))
                val margenTMP = datosBruto.getString(datosBruto.getColumnIndex("MARGENPRODUCTO"))

                val productoTMP = Producto("","","",0f,0f,0f, 0, 0f)
                productoTMP.nombreProducto = nombreTMP
                productoTMP.codigoProducto = codigoTMP
                productoTMP.rutafotoProducto = rutafotoTMP
                productoTMP.stockProducto = stockTMP
                productoTMP.precioCompraProducto = precioCompraTMP.toFloat()
                productoTMP.precioVentaProducto = precioVentaTMP.toFloat()
                productoTMP.ivaProducto = ivaTMP
                productoTMP.margenProducto = margenTMP.toFloat()

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
            misProductos.add(Producto("No se encuentra Producto", "","",0f,00f,0f,0, 0f))
        }

        return misProductos
    }



    fun obtenerFecha () : String {
        val dateFormat = ZonedDateTime.now (ZoneId.of ("Europe/Madrid")).format (
            DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale("es", "ES")))
        return dateFormat
    }

    fun actualizarProducto(db: SQLiteDatabase, producto: Producto) {
        // "CREATE TABLE PRODUCTOS (NOMBREPRODUCTO TEXT, CODIGOPRODUCTO TEXT, RUTAFOTOPRODUCTO TEXT, STOCK INTEGER, PRECIOCOMPRAPRODUCTO TEXT, PRECIOVENTAPRODUCTO TEXT, IVAPRODUCTO INTEGER)"
        val ACTUALIZAR_PRODUCTO = "UPDATE PRODUCTOS SET NOMBREPRODUCTO = '" + producto.nombreProducto + "' , CODIGOPRODUCTO = '" + producto.codigoProducto + "' , RUTAFOTOPRODUCTO = '" + producto.rutafotoProducto + "' , STOCK = '" + producto.stockProducto  + "' , PRECIOCOMPRAPRODUCTO = '" + producto.precioCompraProducto + "' , PRECIOVENTAPRODUCTO = '" + producto.precioVentaProducto + "' , IVAPRODUCTO = '" + producto.ivaProducto + "' , MARGENPRODUCTO = '" + producto.margenProducto  + "' WHERE CODIGOPRODUCTO = '" + producto.codigoProducto + "'"
        db!!.execSQL(ACTUALIZAR_PRODUCTO)
    }

    fun obtenerProveedores(db : SQLiteDatabase, nombreFiltrar : String, codigoFiltrar : String): MutableList<Proveedor> {
        var listaProveedores : MutableList<Proveedor> = arrayListOf()
        val datosBruto = db.rawQuery("SELECT * FROM PROVEEDORES", null)
        if (datosBruto!!.moveToFirst()) {
            do {
                val idProveedor = datosBruto.getString(datosBruto.getColumnIndex("IDPROVEEDOR"))
                val nombreProveedor = datosBruto.getString(datosBruto.getColumnIndex("NOMBREPROVEEDOR"))
                val nombre2Proveedor = datosBruto.getString(datosBruto.getColumnIndex("APELLIDOSPROVEEDOR"))
                val direccionProveedor = datosBruto.getString(datosBruto.getColumnIndex("DIRECCIONPROVEEDOR"))
                val ciudadProveedor = datosBruto.getString(datosBruto.getColumnIndex("CIUDADPROVEEDOR"))
                val provinciaProveedor = datosBruto.getString(datosBruto.getColumnIndex("PROVINCIAPROVEEDOR"))
                val cpProveedor = datosBruto.getString(datosBruto.getColumnIndex("CODIGOPOSTALPROVEEDOR"))
                val telefonoProveedor = datosBruto.getString(datosBruto.getColumnIndex("TELEFONOPROVEDOR"))
                val cifProveedor = datosBruto.getString(datosBruto.getColumnIndex("CIFPROVEEDOR"))
                val emailProveedor = datosBruto.getString(datosBruto.getColumnIndex("EMAILPROVEEDOR"))
                val referenciaProveedor = datosBruto.getString(datosBruto.getColumnIndex("REFERENCIAPROVEEDOR"))

                var proveedorTMP = Proveedor(idProveedor, nombreProveedor,nombre2Proveedor,direccionProveedor,ciudadProveedor,provinciaProveedor,cpProveedor.toInt(),telefonoProveedor,cifProveedor,emailProveedor,referenciaProveedor)

                // Filtrar ...........................
                //
                var filtrarAdd = false
                // Si no filtramos por ningún criterio, añadir siempre
                if (nombreFiltrar.equals("") && codigoFiltrar.equals("")) {
                    filtrarAdd = true
                } else {
                    // si filtramos por nombre, añadir si contiene el texto buscado
                    if (!nombreFiltrar.equals("") && (proveedorTMP.nombreProveedor.uppercase().contains(nombreFiltrar.uppercase()) || proveedorTMP.nombre2Proveedor.uppercase().contains(nombreFiltrar.uppercase()))) {
                        filtrarAdd = true
                    } else {
                        // si filtramos por codigo producto, añadir si contiene el texto buscado
                        if (!codigoFiltrar.equals("") && proveedorTMP.idProveedor.uppercase().contains(codigoFiltrar.uppercase())) {
                            filtrarAdd = true
                        }
                    }

                }
                if (filtrarAdd) listaProveedores.add(proveedorTMP)

                if (listaProveedores.size == 0) {
                    listaProveedores.add(Proveedor("No se encuentra Proveedor", "","","","","",0, "", "", "", ""))
                }

            } while (datosBruto.moveToNext())
        }
        datosBruto.close()
        return listaProveedores
    }

    fun borrarProducto(db: SQLiteDatabase, producto: Producto) {
        val DELETE_PRODUCTO = "DELETE FROM PRODUCTOS WHERE CODIGOPRODUCTO = '" + producto.codigoProducto + "'"
        db!!.execSQL(DELETE_PRODUCTO)
    }

    fun obtenerClientes(db: SQLiteDatabase, nombreFiltrar : String, codigoFiltrar : String): MutableList<Cliente> {
        val listaclientesTMP = mutableListOf<Cliente>()
        val datosBruto = db.rawQuery("SELECT * FROM CLIENTES", null)
        if (datosBruto!!.moveToFirst()) {
            do {
                val idCliente = datosBruto.getString(datosBruto.getColumnIndex("IDCLIENTE"))
                val nombreCliente = datosBruto.getString(datosBruto.getColumnIndex("NOMBRECLIENTE"))
                val nombre2Cliente = datosBruto.getString(datosBruto.getColumnIndex("APELLIDOSCLIENTE"))
                val direccionCliente = datosBruto.getString(datosBruto.getColumnIndex("DIRECCIONCLIENTE"))
                val ciudadCliente = datosBruto.getString(datosBruto.getColumnIndex("CIUDADCLIENTE"))
                val provinciaCliente = datosBruto.getString(datosBruto.getColumnIndex("PROVINCIACLIENTE"))
                val cpCliente = datosBruto.getString(datosBruto.getColumnIndex("CODIGOPOSTALCLIENTE"))
                val telefonoCliente = datosBruto.getString(datosBruto.getColumnIndex("CIFCLIENTE"))
                val cifCliente = datosBruto.getString(datosBruto.getColumnIndex("TELEFONOCLIENTE"))
                val emailCliente = datosBruto.getString(datosBruto.getColumnIndex("EMAILCLIENTE"))
                val referenciaCliente = datosBruto.getString(datosBruto.getColumnIndex("REFERENCIACLIENTE"))
                var clienteTMP = Cliente(idCliente, nombreCliente,nombre2Cliente,direccionCliente,ciudadCliente,provinciaCliente,cpCliente.toInt(),telefonoCliente,cifCliente,emailCliente,referenciaCliente)

                // Filtrar ...........................
                //
                var filtrarAdd = false
                // Si no filtramos por ningún criterio, añadir siempre
                if (nombreFiltrar.equals("") && codigoFiltrar.equals("")) {
                    filtrarAdd = true
                } else {
                    // si filtramos por nombre, añadir si contiene el texto buscado
                    if (!nombreFiltrar.equals("") && (clienteTMP.nombreCliente.uppercase().contains(nombreFiltrar.uppercase()) || clienteTMP.nombre2Cliente.uppercase().contains(nombreFiltrar.uppercase()))) {
                        filtrarAdd = true
                    } else {
                        // si filtramos por codigo producto, añadir si contiene el texto buscado
                        if (!codigoFiltrar.equals("") && clienteTMP.idCliente.uppercase().contains(codigoFiltrar.uppercase())) {
                            filtrarAdd = true
                        }
                    }

                }
                if (filtrarAdd) listaclientesTMP.add(clienteTMP)

            } while (datosBruto.moveToNext())
        }
        datosBruto.close()

        if (listaclientesTMP.size == 0) {
            listaclientesTMP.add(Cliente("No se encuentra Cliente", "","","","","",0, "", "", "", ""))
        }


        return listaclientesTMP
    }

    fun venderProductos(db: SQLiteDatabase, listaProductosVenta: MutableList<Producto>, cliente: Cliente, modopago : String, tipoEntrada : String) {

        val hoy = obtenerFecha()
        val ventaID = System.currentTimeMillis().toString()

        // Actualizar cada producto sumando al stock las unidades que entran y el precio de compra al último
        listaProductosVenta.forEach { producto ->
            val stockFinal =  obtenerProducto(db,producto.codigoProducto).stockProducto - producto.stockProducto
            val UPDATE_PRODUCTO = "UPDATE PRODUCTOS SET STOCK = '" + stockFinal + "' , PRECIOVENTAPRODUCTO = '" + producto.precioVentaProducto + "' WHERE CODIGOPRODUCTO = '" + producto.codigoProducto + "'"
            db!!.execSQL(UPDATE_PRODUCTO)

            // Añadir venta en la BD
            val UPDATE_VENTA = "INSERT INTO SALIDAS VALUES ('" + ventaID + "' , '" + hoy + "' , '" + tipoEntrada + "' , '" + cliente.idCliente + "' , '" + producto.codigoProducto + "' , '" + producto.stockProducto + "' , '" + producto.precioVentaProducto + "' , '" + modopago + "' , '" + producto.ivaProducto + "')"
            db!!.execSQL(UPDATE_VENTA)

        }

    }

    fun darEntradaListaProductos(db:SQLiteDatabase , listaProductosEntrada: MutableList<Producto>, proveedor : Proveedor, cargo : String, tipoEntrada : String) {

        val hoy = obtenerFecha()
        val entradaID = System.currentTimeMillis().toString()

        // Actualizar cada producto sumando al stock las unidades que entran y el precio de compra al último
        listaProductosEntrada.forEach { producto ->
            val stockFinal = producto.stockProducto + obtenerProducto(db,producto.codigoProducto).stockProducto
            val UPDATE_PRODUCTO = "UPDATE PRODUCTOS SET STOCK = '" + stockFinal + "' , PRECIOCOMPRAPRODUCTO = '" + producto.precioCompraProducto + "' WHERE CODIGOPRODUCTO = '" + producto.codigoProducto + "'"
            db!!.execSQL(UPDATE_PRODUCTO)

            // Añadir entrada en la BD
            val UPDATE_ENTRADA = "INSERT INTO ENTRADAS VALUES ('" + entradaID + "' , '" + hoy + "' , '" + tipoEntrada + "' , '" + proveedor.idProveedor + "' , '" + producto.codigoProducto + "' , '" + producto.stockProducto + "' , '" + producto.precioCompraProducto + "' , '" + cargo + "' , '" + producto.ivaProducto  + "')"
            db!!.execSQL(UPDATE_ENTRADA)
        }

    }

    fun guardarCliente(db: SQLiteDatabase, cliente: Cliente) {
        if (clienteExiste(db, cliente.idCliente)) {
            actualizarCliente(db, cliente)
        }  else {
            // val CREATE_TABLE_CLIENTES = "CREATE TABLE CLIENTES (IDCLIENTE ID, NOMBRECLIENTE TEXT, APELLIDOSCLIENTE TEXT, DIRECCIONCLIENTE TEXT, CIUDADCLIENTE TEXT, PROVINCIACLIENTE TEXT, CODIGOPOSTALCLIENTE TEXT, CIFCLIENTE TEXT, TELEFONOCLIENTE TEXT, EMAILCLIENTE TEXT, REFERENCIACLIENTE TEXT)"
            val CREAR_CLIENTE = "INSERT INTO CLIENTES VALUES('" + cliente.idCliente + "' , '" + cliente.nombreCliente + "' , '"  + cliente.nombre2Cliente + "' , '" + cliente.direccionCliente + "' , '" + cliente.ciudadCliente + "' , '" + cliente.provinciaCliente + "' , '" + cliente.cpCliente + "' , '" + cliente.cifCliente + "' , '"  + cliente.telefonoCliente + "' , '"  + cliente.emailCliente + "' , '" + cliente.referenciaCliente  + "')"
            db!!.execSQL(CREAR_CLIENTE)
        }
    }

    fun clienteExiste (db: SQLiteDatabase, codigoCliente : String) : Boolean {
        var existe = false
        val datosBruto = db.rawQuery("SELECT * FROM CLIENTES WHERE IDCLIENTE ='" + codigoCliente + "'", null)
        if (datosBruto!!.moveToFirst()) {
            Log.d("Miapp" , "El producto ya existe")
            existe = true
        }
        return existe
    }

    fun actualizarCliente(db: SQLiteDatabase, cliente: Cliente) {
       val ACTUALIZAR_CLIENTE = "UPDATE CLIENTES SET IDCLIENTE = '" + cliente.idCliente + "' , NOMBRECLIENTE = '" + cliente.nombreCliente + "' , APELLIDOSCLIENTE = '" + cliente.nombre2Cliente + "' , DIRECCIONCLIENTE = '" + cliente.direccionCliente  + "' , CIUDADCLIENTE = '" + cliente.ciudadCliente + "' , PROVINCIACLIENTE = '" + cliente.provinciaCliente + "' , CODIGOPOSTALCLIENTE = '" + cliente.cpCliente + "' , CIFCLIENTE = '" + cliente.cifCliente + "' , TELEFONOCLIENTE = '" + cliente.telefonoCliente + "' , EMAILCLIENTE = '" + cliente.emailCliente + "' , REFERENCIACLIENTE = '" + cliente.referenciaCliente+ "' WHERE IDCLIENTE = '" + cliente.idCliente + "'"
        db!!.execSQL(ACTUALIZAR_CLIENTE)
    }

    fun borrarCliente(db: SQLiteDatabase, cliente: Cliente) {
        val DELETE_CLIENTE = "DELETE FROM CLIENTES WHERE IDCLIENTE = '" + cliente.idCliente + "'"
        db!!.execSQL(DELETE_CLIENTE)
    }

    fun guardarProveedor(db: SQLiteDatabase, proveedor: Proveedor) {
        if (proveedorExiste(db, proveedor.idProveedor)) {
            actualizarProveedor(db, proveedor)
        }  else {
            // val CREATE_TABLE_CLIENTES = "CREATE TABLE CLIENTES (IDCLIENTE ID, NOMBRECLIENTE TEXT, APELLIDOSCLIENTE TEXT, DIRECCIONCLIENTE TEXT, CIUDADCLIENTE TEXT, PROVINCIACLIENTE TEXT, CODIGOPOSTALCLIENTE TEXT, CIFCLIENTE TEXT, TELEFONOCLIENTE TEXT, EMAILCLIENTE TEXT, REFERENCIACLIENTE TEXT)"
            val CREAR_PROVEEDOR = "INSERT INTO PROVEEDORES VALUES('" + proveedor.idProveedor + "' , '" + proveedor.nombreProveedor + "' , '"  + proveedor.nombre2Proveedor + "' , '" + proveedor.direccionProveedor + "' , '" + proveedor.ciudadProveedor + "' , '" + proveedor.provinciaProveedor + "' , '" + proveedor.cpProveedor + "' , '" + proveedor.cifProveedor + "' , '"  + proveedor.telefonoProveedor + "' , '"  + proveedor.emailProveedor + "' , '" + proveedor.referenciaProveedor  + "')"
            db!!.execSQL(CREAR_PROVEEDOR)
        }
    }

    fun proveedorExiste (db: SQLiteDatabase, codigoProveedor : String) : Boolean {
        var existe = false
        val datosBruto = db.rawQuery("SELECT * FROM PROVEEDORES WHERE IDPROVEEDOR ='" + codigoProveedor + "'", null)
        if (datosBruto!!.moveToFirst()) {
            Log.d("Miapp" , "El proveedor ya existe")
            existe = true
        }
        return existe
    }

    fun actualizarProveedor(db: SQLiteDatabase, proveedor: Proveedor) {
        val ACTUALIZAR_PROVEEDOR = "UPDATE PROVEEDORES SET IDPROVEEDOR = '" + proveedor.idProveedor + "' , NOMBREPROVEEDOR = '" + proveedor.nombreProveedor + "' , APELLIDOSPROVEEDOR = '" + proveedor.nombre2Proveedor + "' , DIRECCIONPROVEEDOR = '" + proveedor.direccionProveedor  + "' , CIUDADPROVEEDOR = '" + proveedor.ciudadProveedor + "' , PROVINCIAPROVEEDOR = '" + proveedor.provinciaProveedor + "' , CODIGOPOSTALPROVEEDOR = '" + proveedor.cpProveedor + "' , CIFPROVEEDOR = '" + proveedor.cifProveedor + "' , TELEFONOPROVEEDOR = '" + proveedor.telefonoProveedor + "' , EMAILPROVEEDOR = '" + proveedor.emailProveedor + "' , REFERENCIAPROVEEDOR = '" + proveedor.referenciaProveedor+ "' WHERE IDPROVEEDOR = '" + proveedor.idProveedor + "'"
        db!!.execSQL(ACTUALIZAR_PROVEEDOR)
    }

    fun borrarProveedor(db: SQLiteDatabase, proveedor: Proveedor) {
        val DELETE_PROVEEDOR = "DELETE FROM PROVEEDORES WHERE IDPROVEEDOR = '" + proveedor.idProveedor + "'"
        db!!.execSQL(DELETE_PROVEEDOR)
    }


    fun obtenerEntradas (db : SQLiteDatabase) : MutableList<Entrada> {
        var misEntradas : MutableList<Entrada> = mutableListOf()

        val datosBruto = db.rawQuery("SELECT * FROM ENTRADAS", null)

        if (datosBruto!!.moveToFirst()) {
            do {
                val idEntradaTMP = datosBruto.getString(datosBruto.getColumnIndex("IDENTRADA"))
                val fechaTMP = datosBruto.getString(datosBruto.getColumnIndex("FECHA"))
                val tipoEntradaTMP = datosBruto.getString(datosBruto.getColumnIndex("TIPOENTRADA"))
                val proveedorIDTMP = datosBruto.getString(datosBruto.getColumnIndex("PROVEEDORID"))
                val productoIDTMP = datosBruto.getString(datosBruto.getColumnIndex("PRODUCTOID"))
                val unidadesTMP = datosBruto.getFloat(datosBruto.getColumnIndex("UNIDADES"))
                val precioCompraTMP = datosBruto.getFloat(datosBruto.getColumnIndex("PRECIOCOMPRA"))
                val cargoTMP = datosBruto.getString(datosBruto.getColumnIndex("CARGO"))
                val ivaTMP = datosBruto.getInt(datosBruto.getColumnIndex("IVA"))

                val entradaTMP = Entrada("","","","","",0f, 0f, "", 0)
                entradaTMP.idEntrada = idEntradaTMP
                entradaTMP.fechaEntrada = fechaTMP
                entradaTMP.tipoEntrada = tipoEntradaTMP
                entradaTMP.proveedorEntrada = proveedorIDTMP
                entradaTMP.productoEntrada = productoIDTMP
                entradaTMP.unidadesEntrada = unidadesTMP
                entradaTMP.precioEntrada = precioCompraTMP
                entradaTMP.cargo = cargoTMP
                entradaTMP.iva = ivaTMP

                misEntradas.add(entradaTMP)
            } while (datosBruto.moveToNext())
        }
        datosBruto.close()

        return misEntradas
    }

    fun obtenerSalidas (db : SQLiteDatabase) : MutableList<Salida> {
        var misSalidas : MutableList<Salida> = mutableListOf()
        val datosBruto = db.rawQuery("SELECT * FROM SALIDAS", null)
        if (datosBruto!!.moveToFirst()) {
            do {
                val idSalidaTMP = datosBruto.getString(datosBruto.getColumnIndex("IDSALIDA"))
                val fechaTMP = datosBruto.getString(datosBruto.getColumnIndex("FECHA"))
                val tipoSalidaTMP = datosBruto.getString(datosBruto.getColumnIndex("TIPOSALIDA"))
                val proveedorIDTMP = datosBruto.getString(datosBruto.getColumnIndex("CLIENTEID"))
                val productoIDTMP = datosBruto.getString(datosBruto.getColumnIndex("PRODUCTOID"))
                val unidadesTMP = datosBruto.getFloat(datosBruto.getColumnIndex("UNIDADES"))
                val precioCompraTMP = datosBruto.getFloat(datosBruto.getColumnIndex("PRECIOVENTA"))
                val cargoTMP = datosBruto.getString(datosBruto.getColumnIndex("CARGO"))
                val ivaTMP = datosBruto.getInt(datosBruto.getColumnIndex("IVA"))

                val salidaTMP = Salida("","","","","",0f, 0f, "", 0)
                salidaTMP.idSalida = idSalidaTMP
                salidaTMP.fechaSalida = fechaTMP
                salidaTMP.tipoSalida = tipoSalidaTMP
                salidaTMP.proveedorSalida = proveedorIDTMP
                salidaTMP.productoSalida = productoIDTMP
                salidaTMP.unidadesSalida = unidadesTMP
                salidaTMP.precioSalida = precioCompraTMP
                salidaTMP.cargo = cargoTMP
                salidaTMP.iva = ivaTMP

                misSalidas.add(salidaTMP)
            } while (datosBruto.moveToNext())
        }
        datosBruto.close()

        return misSalidas
    }


}