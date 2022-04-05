package com.example.gestiontienda.Entidades
/*
        ENTRADAS: ID
                  FECHA
                  TIPO_ENTRADA (COMPRA, DEVOLUCIÓN, AJUSTE_STOCK)
                  PROVEEDOR
                  PRODUCTO
                  UNIDADES
                  PRECIO_ENTRADA
                  IMPORTE
                  CARGO (BANCO, CAJA, VALOR_STOCK)
                  IVA
 */
class Entrada (idEntrada : String, fechaEntrada : String, tipoEntrada : String, proveedorEntrada : String, productoEntrada : String, unidadesEntrada : Float, precioEntrada : Float, cargo : String){
    var idEntrada : String
    var fechaEntrada : String
    var tipoEntrada : String
    var proveedorEntrada : String
    var productoEntrada : String
    var unidadesEntrada : Float
    var precioEntrada : Float
    var cargo : String
    
    init {
        this.idEntrada = idEntrada
        this.fechaEntrada = fechaEntrada
        this.tipoEntrada = tipoEntrada
        this.proveedorEntrada = proveedorEntrada
        this.productoEntrada = productoEntrada
        this.unidadesEntrada = unidadesEntrada
        this.precioEntrada = precioEntrada
        this.cargo = cargo
    }
    
}