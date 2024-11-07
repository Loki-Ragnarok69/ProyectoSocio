package com.example.proyectosocio

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

class AdminSQLiteOpenHelper(context: Context, name: String, factory: CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table departamentos(nombre text primary key, planta text, direccion text)")
        db.execSQL("create table socios(nombre text primary key, apellidos text, direccion text, telefono text, nombre_d text, FOREIGN KEY(nombre_d) REFERENCES departamentos(nombre))")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    fun loadDepartamentosToSpinner(
        context: Context,
        spinner: Spinner,
    ){
        try {
            val db = this.readableDatabase
            val cursor = db.rawQuery("SELECT nombre FROM departamentos", null)
            val departamentos = ArrayList<String>()

            departamentos.add("Seleccione un departamento")

            while(cursor.moveToNext()){
                departamentos.add(cursor.getString(0))
            }

            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, departamentos)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter


            cursor.close()
            db.close()
        }catch (e: Exception){
            Toast.makeText(context, "Error al cargar departamentos: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun loadSociosToSpinner(
        context: Context,
        spinner: Spinner,
    ){
        try {
            val db = this.readableDatabase
            val cursor = db.rawQuery("SELECT rowid FROM socios", null)
            val socios = ArrayList<String>()

            // Agrega un elemento inicial como "Seleccione un socio"
            socios.add("Seleccione un socio")

            while(cursor.moveToNext()){
                socios.add(cursor.getString(0))
            }

            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, socios)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            //Toast.makeText(context, spinner.count, Toast.LENGTH_LONG).show()

            cursor.close()
            db.close()
        }catch (e: Exception){
            Toast.makeText(context, "Error al cargar socios: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun loadSociosToSpinnerNombre(
        context: Context,
        spinner: Spinner,
    ){
        try {
            val db = this.readableDatabase
            val cursor = db.rawQuery("SELECT nombre || ' ' || apellidos AS nombreCompleto FROM socios", null)

            val sociosList = ArrayList<String>()
            sociosList.add("Seleccione un socio") // Primer elemento para el mensaje de selección

            while (cursor.moveToNext()) {
                val nombreCompleto = cursor.getString(cursor.getColumnIndexOrThrow("nombreCompleto"))
                sociosList.add(nombreCompleto)
            }

            cursor.close()
            db.close()

            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, sociosList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }catch (e: Exception){
            Toast.makeText(context, "Error al cargar socios: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}