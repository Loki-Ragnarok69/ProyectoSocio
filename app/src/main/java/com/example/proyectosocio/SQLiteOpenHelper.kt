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

    fun obtenerTodosLosSocios(): List<Socio> {
        val socios = mutableListOf<Socio>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT nombre, apellidos, direccion, telefono, nombre_d FROM socios", null)

        while (cursor.moveToNext()) {
            val socio = Socio(
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                apellidos = cursor.getString(cursor.getColumnIndexOrThrow("apellidos")),
                direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion")),
                telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                nombre_d = cursor.getString(cursor.getColumnIndexOrThrow("nombre_d"))
            )
            socios.add(socio)
        }
        cursor.close()
        db.close()
        return socios
    }


    // Obtener socios de un departamento específico
    fun getSociosPorDepartamento(departamento: String): List<Socio> {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT nombre, apellidos, direccion, telefono, nombre_d FROM socios WHERE nombre_d = ?",
            arrayOf(departamento)
        )

        val sociosList = ArrayList<Socio>()
        while (cursor.moveToNext()) {
            val socio = Socio(
                nombre = cursor.getString(0),
                apellidos = cursor.getString(1),
                direccion = cursor.getString(2),
                telefono = cursor.getString(3),
                nombre_d = cursor.getString(4)
            )
            sociosList.add(socio)
        }

        cursor.close()
        db.close()
        return sociosList
    }

    fun loadDetalleDepartamentosToSpinner(context: Context, spinner: Spinner) {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT DISTINCT nombre_d FROM socios", null)

        val departamentosList = ArrayList<String>()
        departamentosList.add("Seleccione un departamento") // Opción por defecto

        while (cursor.moveToNext()) {
            val departamento = cursor.getString(0)
            departamentosList.add(departamento)
        }

        cursor.close()
        db.close()

        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, departamentosList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}