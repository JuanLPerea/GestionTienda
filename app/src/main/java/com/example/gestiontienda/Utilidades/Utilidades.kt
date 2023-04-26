package com.example.gestiontienda.Utilidades

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class Utilidades {

    companion object {
        private const val BUFFER_SIZE = 4096

        fun Fragment.hideKeyboard() {
            view?.let { activity?.hideKeyboard(it) }
        }

        fun Activity.hideKeyboard() {
            hideKeyboard(currentFocus ?: View(this))
        }

        fun Context.hideKeyboard(view: View) {
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun zipAll(directory: String, zipFile: String) {
            val sourceFile = File(directory)

            val inputDirectory = sourceFile
            val outputZipFile = File(zipFile)

            if (outputZipFile.exists()) {
                outputZipFile.delete()
            }

            val existeCarpeta = File("/data/user/0/com.example.gestiontienda/app_Copia")
            if (!existeCarpeta.exists()) {
                Files.createDirectory(Paths.get("/data/user/0/com.example.gestiontienda/app_Copia"))
            }

            ZipOutputStream(BufferedOutputStream(FileOutputStream(outputZipFile))).use { zos ->
                inputDirectory.walkTopDown().forEach { file ->
                    val zipFileName = file.absolutePath.removePrefix(inputDirectory.absolutePath).removePrefix("/")
                    val entry = ZipEntry( "$zipFileName${(if (file.isDirectory) "/" else "" )}")
                    zos.putNextEntry(entry)
                    if (file.isFile) {
                        file.inputStream().copyTo(zos)
                    }
                }
            }
        }

        fun listaArchivosCopia() : MutableList<String> {
            var listaArchivos : MutableList<String> = mutableListOf()

            val directorio = File("/data/user/0/com.example.gestiontienda/app_Copia")

            directorio.walkTopDown().forEach { file ->
                if (file.isFile) listaArchivos.add(file.name)
            }
            return listaArchivos
        }


        fun unzipAll(archivoCopia : File){
            // TODO descomprimir el archivo de copia de seguridad y restaurar los datos
            val directory = File("/data/user/0/com.example.gestiontienda/app_Datos")
            if (!directory.exists()) {
                Files.createDirectory(Paths.get("/data/user/0/com.example.gestiontienda/app_Datos"))
            }

            ZipFile(archivoCopia).use { zip ->
                zip.entries().asSequence().forEach { entry ->
                    if (entry.size >0) {
                        zip.getInputStream(entry).use { input ->
                            val filePath =  "/data/user/0/com.example.gestiontienda/app_Datos" + "/" + entry.name
                            extractFile(input, filePath)
                        }
                    }
                }
            }


        }
        /**
         * Extracts a zip entry (file entry)
         * @param inputStream
         * @param destFilePath
         * @throws IOException
         */
        @Throws(IOException::class)
        private fun extractFile(inputStream: InputStream, destFilePath: String) {
            val bos = BufferedOutputStream(FileOutputStream(destFilePath))
            val bytesIn = ByteArray(BUFFER_SIZE)
            var read: Int
            while (inputStream.read(bytesIn).also { read = it } != -1) {
                bos.write(bytesIn, 0, read)
            }
            bos.close()
        }
    }

}