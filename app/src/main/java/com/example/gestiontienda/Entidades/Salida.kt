package com.example.gestiontienda.Entidades
/*
        SALIDAS: ID
                 FECHA
                 TIPO SALIDA (VENTA, AJUSTE_STOCK)
                 PRODUCTO
                 UNIDADES
                 IMPORTE
                 PRECIO_VENTA
                 CARGO (BANCO, CAJA, VALOR_STOCK)
                 IVA
 */
class Salida (idSalida : String, fechaSalida : String, tipoSalida : String, proveedorSalida : String, productoSalida : String, unidadesSalida : Float, precioSalida : Float, cargo : String){
    var idSalida : String
    var fechaSalida : String
    var tipoSalida : String
    var proveedorSalida : String
    var productoSalida : String
    var unidadesSalida : Float
    var precioSalida : Float
    var cargo : String

    init {
        this.idSalida = idSalida
        this.fechaSalida = fechaSalida
        this.tipoSalida = tipoSalida
        this.proveedorSalida = proveedorSalida
        this.productoSalida = productoSalida
        this.unidadesSalida = unidadesSalida
        this.precioSalida = precioSalida
        this.cargo = cargo
    }

}