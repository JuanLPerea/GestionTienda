package com.example.gestiontienda.Entidades
/*
        PROVEEDORES:
                  ID
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
 */
//"CREATE TABLE PROVEEDORES (IDPROVEEDOR ID, NOMBREPROVEEDOR TEXT, APELLIDOSPROVEEDOR TEXT, DIRECCIONPROVEEDOR TEXT, CIUDADPROVEEDOR TEXT, PROVINCIAPROVEEDOR TEXT, CODIGOPOSTALPROVEEDOR TEXT, CIFPROVEEDOR TEXT, TELEFONOPROVEDOR TEXT, EMAILPROVEEDOR TEXT, REFERENCIAPROVEEDOR TEXT)"

class Proveedor (idProveedor : String, nombreProveedor : String, nombre2Proveedor : String, direccionProveedor : String, ciudadProveedor : String, provinciaProveedor : String, cpProveedor : Int, telefonoProveedor : String, emailProveedor : String, referenciaProveedor : String){

    var idProveedor : String
    var nombreProveedor : String
    var nombre2Proveedor : String
    var direccionProveedor : String
    var ciudadProveedor : String
    var provinciaProveedor : String
    var cpProveedor : Int
    var telefonoProveedor : String
    var emailProveedor : String
    var referenciaProveedor : String
    
    init {
        this.idProveedor = idProveedor
        this.nombreProveedor = nombreProveedor
        this.nombre2Proveedor = nombre2Proveedor
        this.direccionProveedor = direccionProveedor
        this.ciudadProveedor = ciudadProveedor
        this.provinciaProveedor = provinciaProveedor
        this.cpProveedor = cpProveedor
        this.telefonoProveedor = telefonoProveedor
        this.emailProveedor = emailProveedor
        this.referenciaProveedor = referenciaProveedor
    }
    
    
    
}