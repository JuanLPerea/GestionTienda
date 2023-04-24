package com.example.gestiontienda.Utilidades

import android.content.Context
import android.content.ContextWrapper
import android.os.Environment
import android.util.Log
import com.example.gestiontienda.Utilidades.Utilidades.Companion.zipAll
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.*
import java.time.LocalDateTime
import java.util.jar.Manifest

class ExcelHelper {

     fun createWorkbook(databaseHelper : DatabaseHelper): Workbook {

         // Instanciar Base de Datos SQLite
         val db = databaseHelper.writableDatabase

        // Creating a workbook object from the XSSFWorkbook() class
        val ourWorkbook = XSSFWorkbook()

        //Creating a sheet called "statSheet" inside the workbook and then add data to it
        val tiendaSheet: Sheet = ourWorkbook.createSheet("Datos Tienda")
        val productosSheet: Sheet = ourWorkbook.createSheet("Productos")
        val clientesSheet : Sheet = ourWorkbook.createSheet("Clientes")
        val proveedoresSheet : Sheet = ourWorkbook.createSheet("Proveedores")
        val entradasSheet: Sheet = ourWorkbook.createSheet("Entradas")
        val salidasSheet: Sheet = ourWorkbook.createSheet("Salidas")


         // Añadir los datos de la tienda en su tabla
         val tienda = databaseHelper.obtenerTienda(db)
         //Creating rows at passed in indices
         val row1 = tiendaSheet.createRow(0)
         val row2 = tiendaSheet.createRow(1)
         val row3 = tiendaSheet.createRow(2)
         val row4 = tiendaSheet.createRow(3)
         val row5 = tiendaSheet.createRow(4)
         val row6 = tiendaSheet.createRow(5)
         createCell(row1, 0, "Nombre de la Tienda")
         createCell(row1, 1, tienda.nombreTienda)
         createCell(row2, 0, "Dirección")
         createCell(row2, 1, tienda.direccionTienda)
         createCell(row3, 0, "CIF")
         createCell(row3, 1, tienda.cifTienda)
         createCell(row4, 0, "e-mail")
         createCell(row4, 1, tienda.emailTienda)
         createCell(row5, 0, "Teléfono")
         createCell(row5, 1, tienda.telefonoTienda)
         createCell(row6, 0, "Otros datos")
         createCell(row6, 1, tienda.otrosDatosTienda)

         // Añadir la lista de los clientes
         // Encabezado
         val encabezado = clientesSheet.createRow(0)
         createCell(encabezado, 0, "ID")
         createCell(encabezado, 1, "NOMBRE")
         createCell(encabezado, 2, "APELLIDOS")
         createCell(encabezado, 3, "DIRECCIÓN")
         createCell(encabezado, 4, "CIUDAD")
         createCell(encabezado, 5, "PROVINCIA")
         createCell(encabezado, 6, "CÓDIGO POSTAL")
         createCell(encabezado, 7, "CIF")
         createCell(encabezado, 8, "TELÉFONO")
         createCell(encabezado, 9, "E-MAIL")
         createCell(encabezado, 10, "REFERENCIA")
         
         val listaClientes = databaseHelper.obtenerClientes(db,"","")
         listaClientes.forEachIndexed() { index , cliente ->
             val rowCliente = clientesSheet.createRow(index + 1)
             createCell(rowCliente, 0, cliente.idCliente)
             createCell(rowCliente, 1, cliente.nombreCliente)
             createCell(rowCliente, 2, cliente.nombre2Cliente)
             createCell(rowCliente, 3, cliente.direccionCliente)
             createCell(rowCliente, 4, cliente.ciudadCliente)
             createCell(rowCliente, 5, cliente.provinciaCliente)
             createCell(rowCliente, 6, cliente.cpCliente.toString())
             createCell(rowCliente, 7, cliente.cifCliente)
             createCell(rowCliente, 8, cliente.telefonoCliente)
             createCell(rowCliente, 9, cliente.emailCliente)
             createCell(rowCliente, 10, cliente.referenciaCliente)
         }


         // Añadir la lista de los productos
         // Encabezado
         //     val CREATE_TABLE_PRODUCTOS = "CREATE TABLE PRODUCTOS (NOMBREPRODUCTO TEXT, CODIGOPRODUCTO TEXT, RUTAFOTOPRODUCTO TEXT, STOCK TEXT, PRECIOCOMPRAPRODUCTO TEXT, PRECIOVENTAPRODUCTO TEXT, IVAPRODUCTO INTEGER, MARGENPRODUCTO TEXT)"
         //
         val encabezadoProductos = productosSheet.createRow(0)
         createCell(encabezadoProductos, 0, "NOMBREPRODUCTO")
         createCell(encabezadoProductos, 1, "CODIGOPRODUCTO")
         createCell(encabezadoProductos, 2, "RUTAFOTOPRODUCTO")
         createCell(encabezadoProductos, 3, "PRECIOCOMPRAPRODUCTO")
         createCell(encabezadoProductos, 4, "PRECIOVENTAPRODUCTO")
         createCell(encabezadoProductos, 5, "IVAPRODUCTO")
         createCell(encabezadoProductos, 6, "MARGENPRODUCTO")

         val listaProductos = databaseHelper.obtenerProductos(db,"","")
         listaProductos.forEachIndexed() { index , producto ->
             val rowProducto = productosSheet.createRow(index + 1)
             createCell(rowProducto, 0, producto.nombreProducto)
             createCell(rowProducto, 1, producto.codigoProducto)
             createCell(rowProducto, 2, producto.rutafotoProducto)
             createCell(rowProducto, 3, producto.precioCompraProducto.toString())
             createCell(rowProducto, 4, producto.precioVentaProducto.toString())
             createCell(rowProducto, 5, producto.ivaProducto.toString())
             createCell(rowProducto, 6, producto.margenProducto.toString())

         }

         // Añadir la lista de los proveedores
         // Encabezado
         val encabezado_proveedor = proveedoresSheet.createRow(0)
         createCell(encabezado_proveedor, 0, "ID")
         createCell(encabezado_proveedor, 1, "NOMBRE")
         createCell(encabezado_proveedor, 2, "APELLIDOS")
         createCell(encabezado_proveedor, 3, "DIRECCIÓN")
         createCell(encabezado_proveedor, 4, "CIUDAD")
         createCell(encabezado_proveedor, 5, "PROVINCIA")
         createCell(encabezado_proveedor, 6, "CÓDIGO POSTAL")
         createCell(encabezado_proveedor, 7, "CIF")
         createCell(encabezado_proveedor, 8, "TELÉFONO")
         createCell(encabezado_proveedor, 9, "E-MAIL")
         createCell(encabezado_proveedor, 10, "REFERENCIA")

         val listaProveedores = databaseHelper.obtenerProveedores(db,"","")
         listaProveedores.forEachIndexed() { index , proveedor ->
             val rowProveedor = proveedoresSheet.createRow(index + 1)
             createCell(rowProveedor, 0, proveedor.idProveedor)
             createCell(rowProveedor, 1, proveedor.nombreProveedor)
             createCell(rowProveedor, 2, proveedor.nombre2Proveedor)
             createCell(rowProveedor, 3, proveedor.direccionProveedor)
             createCell(rowProveedor, 4, proveedor.ciudadProveedor)
             createCell(rowProveedor, 5, proveedor.provinciaProveedor)
             createCell(rowProveedor, 6, proveedor.cpProveedor.toString())
             createCell(rowProveedor, 7, proveedor.cifProveedor)
             createCell(rowProveedor, 8, proveedor.telefonoProveedor)
             createCell(rowProveedor, 9, proveedor.emailProveedor)
             createCell(rowProveedor, 10, proveedor.referenciaProveedor)
         }
         
         // Añadir lista de entradas en su tabla correspondiente
         // Encabezado
         val encabezado_entradas = entradasSheet.createRow(0)
         createCell(encabezado_entradas, 0, "ID")
         createCell(encabezado_entradas, 1, "FECHA")
         createCell(encabezado_entradas, 2, "TIPO ENTRADA")
         createCell(encabezado_entradas, 3, "PROVEEDOR ID")
         createCell(encabezado_entradas, 4, "PRODUCTO ID")
         createCell(encabezado_entradas, 5, "UNIDADES")
         createCell(encabezado_entradas, 6, "PRECIO COMPRA")
         createCell(encabezado_entradas, 7, "CARGADO EN")
         createCell(encabezado_entradas, 8, "IVA")

        // Datos
         val listaEntradas = databaseHelper.obtenerEntradas(db)
         listaEntradas.forEachIndexed() { index , entrada ->
             val rowEntrada = entradasSheet.createRow(index + 1)
             createCell(rowEntrada, 0, entrada.idEntrada)
             createCell(rowEntrada, 1, entrada.fechaEntrada)
             createCell(rowEntrada, 2, entrada.tipoEntrada)
             createCell(rowEntrada, 3, entrada.proveedorEntrada)
             createCell(rowEntrada, 4, entrada.productoEntrada)
             createCell(rowEntrada, 5, entrada.unidadesEntrada.toString())
             createCell(rowEntrada, 6, entrada.precioEntrada.toString())
             createCell(rowEntrada, 7, entrada.cargo)
             createCell(encabezado_entradas, 8, entrada.iva.toString())
         }

         // Añadir lista de salidas en su tabla correspondiente
         // Encabezado
         val encabezado_salidas = salidasSheet.createRow(0)
         createCell(encabezado_salidas, 0, "ID")
         createCell(encabezado_salidas, 1, "FECHA")
         createCell(encabezado_salidas, 2, "TIPO ENTRADA")
         createCell(encabezado_salidas, 3, "PROVEEDOR ID")
         createCell(encabezado_salidas, 4, "PRODUCTO ID")
         createCell(encabezado_salidas, 5, "UNIDADES")
         createCell(encabezado_salidas, 6, "PRECIO COMPRA")
         createCell(encabezado_salidas, 7, "CARGADO EN")
         createCell(encabezado_salidas, 8, "IVA")

         // Datos
         val listaSalidas = databaseHelper.obtenerSalidas(db)
         listaSalidas.forEachIndexed() { index , salida ->
             val rowSalida = salidasSheet.createRow(index + 1)
             createCell(rowSalida, 0, salida.idSalida)
             createCell(rowSalida, 1, salida.fechaSalida)
             createCell(rowSalida, 2, salida.tipoSalida)
             createCell(rowSalida, 3, salida.proveedorSalida)
             createCell(rowSalida, 4, salida.productoSalida)
             createCell(rowSalida, 5, salida.unidadesSalida.toString())
             createCell(rowSalida, 6, salida.precioSalida.toString())
             createCell(rowSalida, 7, salida.cargo)
             createCell(rowSalida, 8, salida.iva.toString())
         }

         return ourWorkbook
    }
    

    //function for creating a cell.
     fun createCell(sheetRow: Row, columnIndex: Int, cellValue: String?) {
        //create a cell at a passed in index
        val ourCell = sheetRow.createCell(columnIndex)
        //add the value to it
        ourCell?.setCellValue(cellValue)
    }

     fun createExcelFile(ourWorkbook: Workbook, context: Context) {
        // Get the context wrapper
        val wrapper = ContextWrapper(context)
        // Initialize a new file instance to save bitmap object
        var excelDir = wrapper.getDir("Datos", Context.MODE_PRIVATE)

        //get our app file directory
        val ourAppFileDirectory = excelDir
        //Check whether it exists or not, and create if does not exist.
        if (ourAppFileDirectory != null && !ourAppFileDirectory.exists()) {
            ourAppFileDirectory.mkdirs()
        }

        //Create an excel file called test.xlsx
        val excelFile = File(ourAppFileDirectory, "copia_tienda.xlsx")

        //Write a workbook to the file using a file outputstream
        try {
            val fileOut = FileOutputStream(excelFile)
            ourWorkbook.write(fileOut)
            fileOut.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }


         // TODO Guardar fotos en una carpeta o un fichero .zip para poder recuperarlas luego
         var copiaDir = wrapper.getDir("Copia", Context.MODE_PRIVATE)
         val fecha = LocalDateTime.now().toString().replace(':', '_').replace('.', '_')
         val descargas_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
         // Crea 2 archivos de copia de seguridad:
         // 1 en la memoria interna
         zipAll("/data/user/0/com.example.gestiontienda/app_Datos", "/data/user/0/com.example.gestiontienda/app_Copia/copia" + fecha +".zip")
         // y 2 en la carpeta de descargas
         Log.d("Miapp" , descargas_path.toString())
         zipAll( "/data/user/0/com.example.gestiontienda/app_Datos" , descargas_path.toString() + "/copia_tienda.zip")
     }

     fun getExcelFile(context: Context): File? {
        // Get the context wrapper
        val wrapper = ContextWrapper(context)
        // Initialize a new file instance to save bitmap object
        var filesDir = wrapper.getDir("Datos", Context.MODE_PRIVATE)

        val ourAppFileDirectory = filesDir
        ourAppFileDirectory?.let {

            //Check if file exists or not
            if (it.exists()) {
                //check the file in the directory called "test.xlsx"
                val retrievedExcel = File(ourAppFileDirectory, "test.xlsx")
                //return the file
                return retrievedExcel
            }
        }
        return null
    }

    //function for reading the workbook from the loaded spreadsheet file
     fun retrieveWorkbook(context: Context): Workbook? {

        //Reading the workbook from the loaded spreadsheet file
        getExcelFile(context)?.let {
            try {

                //Reading it as stream
                val workbookStream = FileInputStream(it)

                //Return the loaded workbook
                return WorkbookFactory.create(workbookStream)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return null
    }

    //function for selecting the sheet
     fun selectSheet(context: Context): Sheet? {

        //choosing the workbook
        retrieveWorkbook(context)?.let { workbook ->

            //Checking the existence of a sheet
            if (workbook.numberOfSheets > 0) {

                //Return the first sheet
                return workbook.getSheetAt(0)
            }
        }

        return null
    }

    //function to compute the statistical functions
     fun compute(context: Context) {
        //get sheet
        selectSheet(context)?.let { sheet ->
            //finding the total number of rows
            val totalRows = sheet.physicalNumberOfRows
            val scoreArray = Array<Int>(totalRows) { 0 }
            for (i in 0 until totalRows) {
                scoreArray[i] = (sheet.getRow(i).getCell(1)).toString().toInt()
            }

            var mean = findMean(scoreArray)
            var variance = findVariance(scoreArray, mean)
            var stdDeviation: Double = Math.sqrt(variance)

            //formatting to 2 decimal places
            var meanTo2dp: String = String.format("%.2f", mean)
            var stdDeviationTo2dp = String.format("%.2f", stdDeviation)
            var varianceTo2dp: String = String.format("%.2f", variance)

            //displaying the text to the textview
         //   textView.setText("From the Spreadsheet, we get these:\n\n MEAN: " + meanTo2dp + "\n VARIANCE: " + varianceTo2dp + "\n STD DEVIATION: " + stdDeviationTo2dp)
        }

    }

     fun findMean(arrayArg: Array<Int>): Double {
        var total = 0.0
        var i = 0
        for (a in arrayArg) {
            total += arrayArg[i]
            i++
        }
        var avg = total / arrayArg.size
        return avg
    }

     fun findVariance(arrayArg: Array<Int>, mean: Double): Double {
        var indexVariance = 0.0
        var i = 0
        for (a in arrayArg) {
            indexVariance += Math.pow(((arrayArg[i].toDouble()) - mean), 2.0)
            i++
        }
        var avgVariance = indexVariance / arrayArg.size
        return avgVariance
    }

     fun updateCell(context: Context) {
        getExcelFile(context)?.let {
            try {

                //Reading it as stream
                val workbookStream = FileInputStream(it)

                //Return the loaded workbook
                val workbook = WorkbookFactory.create(workbookStream)
                if (workbook.numberOfSheets > 0) {

                    //Return the first sheet
                    val sheet = workbook.getSheetAt(0)
                    //choosing the first row as the headers
                    var nameHeaderCell = sheet.getRow(0).getCell(0)
                    var scoreHeaderCell = sheet.getRow(0).getCell(1)

                    //selecting cells to be editted and formatted
                    var targetCellLabel = sheet.getRow(1).getCell(0)
                    var targetCellValue = sheet.getRow(1).getCell(1)

                    val font: Font = workbook.createFont()
                    val headerCellStyle = workbook.createCellStyle()
                    val targetCellDataStyle = workbook.createCellStyle()

                    //choosing white color and a bold formatting
                    font.color = IndexedColors.WHITE.index
                    font.bold = true

                    //applying formatting styles to the cells
                    headerCellStyle.setAlignment(HorizontalAlignment.LEFT)
                    headerCellStyle.fillForegroundColor = IndexedColors.RED.index
                    headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)
                    headerCellStyle.setFont(font);
                    nameHeaderCell.cellStyle = headerCellStyle
                    scoreHeaderCell.cellStyle = headerCellStyle

                    targetCellDataStyle.setAlignment(HorizontalAlignment.LEFT)
                    targetCellValue.cellStyle = targetCellDataStyle
                    targetCellLabel.cellStyle = targetCellDataStyle

                    //feeding in new values to the selected cells
                    targetCellLabel.setCellValue("Mitchelle")
                    targetCellValue.setCellValue(140.0)

                    workbookStream.close()

                    //saving the changes
                    try {
                        val fileOut = FileOutputStream(it)
                        workbook.write(fileOut)
                        fileOut.close()
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

     fun deleteSheet(context: Context) {
        getExcelFile(context)?.let {
            try {

                //Reading it as stream
                val workbookStream = FileInputStream(it)

                //Return the loaded workbook
                val workbook = WorkbookFactory.create(workbookStream)
                if (workbook.numberOfSheets > 0) {
                    //removing the second sheet
                    workbook.removeSheetAt(1)
                    workbookStream.close()

                    try {
                        val fileOut = FileOutputStream(it)
                        workbook.write(fileOut)
                        fileOut.close()
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

     fun deleteRow(context: Context) {
        getExcelFile(context)?.let {
            try {
                val rowNo = 1
                //Reading it as stream
                val workbookStream = FileInputStream(it)

                //Return the loaded workbook
                val workbook = WorkbookFactory.create(workbookStream)
                if (workbook.numberOfSheets > 0) {

                    //Return the first sheet
                    val sheet = workbook.getSheetAt(0)

                    //getting the total number of rows available
                    val totalNoOfRows = sheet.lastRowNum

                    val targetRow = sheet.getRow(rowNo)
                    if (targetRow != null) {
                        sheet.removeRow(targetRow)
                    }

                    /*excluding the last row, move the cells that come
                    after the deleted row one step behind*/
                    if (rowNo >= 0 && rowNo < totalNoOfRows) {
                        sheet.shiftRows(rowNo + 1, totalNoOfRows, -1)
                    }
                    workbookStream.close()

                    try {
                        val fileOut = FileOutputStream(it)
                        workbook.write(fileOut)
                        fileOut.close()
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

     fun deleteColumn(context: Context) {
        getExcelFile(context)?.let {
            try {
                val colNo = 0
                //Reading it as stream
                val workbookStream = FileInputStream(it)

                //Return the loaded workbook
                val workbook = WorkbookFactory.create(workbookStream)
                if (workbook.numberOfSheets > 0) {

                    //Return the first sheet
                    val sheet = workbook.getSheetAt(0)
                    val totalRows = sheet.lastRowNum
                    val row = sheet.getRow(colNo)
                    val maxCell = row.lastCellNum.toInt()
                    if (colNo >= 0 && colNo <= maxCell) {
                        for (rowNo in 0..totalRows) {
                            val targetCol: Cell = sheet.getRow(rowNo).getCell(colNo)
                            if (targetCol != null) {
                                sheet.getRow(rowNo).removeCell(targetCol);
                            }
                        }
                    }
                    workbookStream.close()

                    try {
                        val fileOut = FileOutputStream(it)
                        workbook.write(fileOut)
                        fileOut.close()
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}