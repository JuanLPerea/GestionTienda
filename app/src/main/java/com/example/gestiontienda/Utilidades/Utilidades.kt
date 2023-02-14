package com.example.gestiontienda.Utilidades

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class Utilidades {

    companion object {
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

            println("directory: $directory")
            println("zipFile: $zipFile")

            val inputDirectory = sourceFile
            val outputZipFile = File(zipFile)

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
    }

}