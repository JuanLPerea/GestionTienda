package com.example.gestiontienda.Entidades
/*
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

 */
class Cliente (idCliente : String, nombreCliente : String, nombre2Cliente : String, direccionCliente : String, ciudadCliente : String, provinciaCliente : String, cpCliente : Int, telefonoCliente : String, cifCliente : String, emailCliente : String, referenciaCliente : String){

    var idCliente : String
    var nombreCliente : String
    var nombre2Cliente : String
    var direccionCliente : String
    var ciudadCliente : String
    var provinciaCliente : String
    var cpCliente : Int
    var telefonoCliente : String
    var cifCliente : String
    var emailCliente : String
    var referenciaCliente : String

    init {
        this.idCliente = idCliente
        this.nombreCliente = nombreCliente
        this.nombre2Cliente = nombre2Cliente
        this.direccionCliente = direccionCliente
        this.ciudadCliente = ciudadCliente
        this.provinciaCliente = provinciaCliente
        this.cpCliente = cpCliente
        this.telefonoCliente = telefonoCliente
        this.cifCliente = cifCliente
        this.emailCliente = emailCliente
        this.referenciaCliente = referenciaCliente
    }



}