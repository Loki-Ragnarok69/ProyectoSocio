package com.example.proyectosocio

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

// La clase AdminSQLiteOpenHelper extiende de SQLiteOpenHelper y maneja la creación, actualización y acceso a la base de datos.
class AdminSQLiteOpenHelper(context: Context, name: String, factory: CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    // Este método se llama cuando la base de datos es creada por primera vez
    override fun onCreate(db: SQLiteDatabase) {
        // Crea las tablas 'departamentos' y 'socios' en la base de datos
        db.execSQL("create table departamentos(nombre text primary key, planta text, direccion text)")
        db.execSQL("create table socios(nombre text primary key, apellidos text, direccion text, telefono text, nombre_d text, FOREIGN KEY(nombre_d) REFERENCES departamentos(nombre))")
    }

    // Este método es llamado si la versión de la base de datos se actualiza
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Aquí puedes manejar la actualización de la base de datos (no implementado en este caso)
    }

    // Método para cargar los departamentos en un Spinner (una lista desplegable)
    fun loadDepartamentosToSpinner(
        context: Context,
        spinner: Spinner,
    ) {
        try {
            // Abre la base de datos en modo lectura
            val db = this.readableDatabase
            // Ejecuta una consulta SQL para obtener los nombres de los departamentos
            val cursor = db.rawQuery("SELECT nombre FROM departamentos", null)
            val departamentos = ArrayList<String>()

            // Agrega un mensaje por defecto
            departamentos.add("Seleccione un departamento")

            // Recorre los resultados de la consulta y agrega los nombres de los departamentos al ArrayList
            while(cursor.moveToNext()){
                departamentos.add(cursor.getString(0))
            }

            // Crea un adaptador de ArrayAdapter para llenar el Spinner con los departamentos
            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, departamentos)
            // Configura cómo se mostrarán los elementos en el Spinner
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Asigna el adaptador al Spinner
            spinner.adapter = adapter

            // Cierra el cursor y la base de datos
            cursor.close()
            db.close()
        } catch (e: Exception) {
            // Muestra un mensaje de error si ocurre algún problema al cargar los departamentos
            Toast.makeText(context, "Error al cargar departamentos: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // Método para cargar los socios en un Spinner basado en el 'rowid' (ID único) de cada socio
    fun loadSociosToSpinner(
        context: Context,
        spinner: Spinner,
    ) {
        try {
            // Abre la base de datos en modo lectura
            val db = this.readableDatabase
            // Ejecuta una consulta SQL para obtener los IDs de los socios
            val cursor = db.rawQuery("SELECT rowid FROM socios ORDER BY rowid ASC", null)
            val socios = ArrayList<String>()

            // Agrega un mensaje por defecto
            socios.add("Seleccione un socio")

            // Recorre los resultados y agrega los IDs de los socios al ArrayList
            while(cursor.moveToNext()){
                socios.add(cursor.getString(0))
            }

            // Crea un adaptador para llenar el Spinner con los socios
            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, socios)
            // Configura cómo se mostrarán los elementos en el Spinner
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Asigna el adaptador al Spinner
            spinner.adapter = adapter

            // Cierra el cursor y la base de datos
            cursor.close()
            db.close()
        } catch (e: Exception) {
            // Muestra un mensaje de error si ocurre algún problema al cargar los socios
            Toast.makeText(context, "Error al cargar socios: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // Método para cargar los socios en un Spinner pero usando su nombre completo (nombre y apellidos)
    fun loadSociosToSpinnerNombre(
        context: Context,
        spinner: Spinner,
    ) {
        try {
            // Abre la base de datos en modo lectura
            val db = this.readableDatabase
            // Ejecuta una consulta SQL para obtener los nombres y apellidos de los socios
            val cursor = db.rawQuery("SELECT nombre || ' ' || apellidos AS nombreCompleto FROM socios", null)

            val sociosList = ArrayList<String>()
            // Agrega un mensaje por defecto
            sociosList.add("Seleccione un socio")

            // Recorre los resultados y agrega los nombres completos al ArrayList
            while (cursor.moveToNext()) {
                val nombreCompleto = cursor.getString(cursor.getColumnIndexOrThrow("nombreCompleto"))
                sociosList.add(nombreCompleto)
            }

            // Cierra el cursor y la base de datos
            cursor.close()
            db.close()

            // Crea un adaptador para llenar el Spinner con los nombres completos de los socios
            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, sociosList)
            // Configura cómo se mostrarán los elementos en el Spinner
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Asigna el adaptador al Spinner
            spinner.adapter = adapter
        } catch (e: Exception) {
            // Muestra un mensaje de error si ocurre algún problema al cargar los socios
            Toast.makeText(context, "Error al cargar socios: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // Método para obtener todos los socios de la base de datos y devolverlos como una lista de objetos Socio
    fun obtenerTodosLosSocios(): List<Socio> {
        val socios = mutableListOf<Socio>()
        val db = this.readableDatabase
        // Ejecuta una consulta SQL para obtener los datos de todos los socios
        val cursor = db.rawQuery("SELECT nombre, apellidos, direccion, telefono, nombre_d FROM socios", null)

        // Recorre los resultados de la consulta y crea objetos Socio a partir de ellos
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

        // Cierra el cursor y la base de datos
        cursor.close()
        db.close()
        return socios
    }

    // Método para obtener los socios de un departamento específico
    fun getSociosPorDepartamento(departamento: String): List<Socio> {
        val db = this.readableDatabase
        // Ejecuta una consulta SQL para obtener los socios de un departamento específico
        val cursor = db.rawQuery(
            "SELECT nombre, apellidos, direccion, telefono, nombre_d FROM socios WHERE nombre_d = ?",
            arrayOf(departamento)
        )

        val sociosList = ArrayList<Socio>()
        // Recorre los resultados de la consulta y agrega los socios al ArrayList
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

        // Cierra el cursor y la base de datos
        cursor.close()
        db.close()
        return sociosList
    }

    // Método para cargar los departamentos únicos (sin repeticiones) en un Spinner
    fun loadDetalleDepartamentosToSpinner(context: Context, spinner: Spinner) {
        val db = this.readableDatabase
        // Ejecuta una consulta SQL para obtener los departamentos únicos
        val cursor = db.rawQuery("SELECT DISTINCT nombre_d FROM socios", null)

        val departamentosList = ArrayList<String>()
        departamentosList.add("Seleccione un departamento") // Opción por defecto

        // Recorre los resultados y agrega los departamentos al ArrayList
        while (cursor.moveToNext()) {
            val departamento = cursor.getString(0)
            departamentosList.add(departamento)
        }

        // Cierra el cursor y la base de datos
        cursor.close()
        db.close()

        // Crea un adaptador para llenar el Spinner con los departamentos
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, departamentosList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}
