package com.example.gestiontienda.Entidades
/*
        PRODUCTOS: ID
                   NOMBRE
                   CODIGO
                   RUTA_FOTO
                   STOCK
                   PRECIO_COMPRA
                   PRECIO_VENTA
                   IVA
                   MARGEN DE BENEFICIO DEL PRODUCTO

 */
class Producto ( nombreProducto : String, codigoProducto : String , rutafotoProducto : String, stockProducto : Int, precioCompraProducto : Float, precioVentaProducto : Float, ivaProducto : Int , margenProducto : Float) {

    var nombreProducto : String
    var codigoProducto : String
    var rutafotoProducto : String
    var stockProducto : Int
    var precioCompraProducto : Float
    var precioVentaProducto : Float
    var ivaProducto : Int
    var margenProducto : Float

    init {
        this.nombreProducto = nombreProducto
        this.codigoProducto = codigoProducto
        this.rutafotoProducto = rutafotoProducto
        this.stockProducto = stockProducto
        this.precioCompraProducto = precioCompraProducto
        this.precioVentaProducto = precioVentaProducto
        this.ivaProducto = ivaProducto
        this.margenProducto = margenProducto
    }

}