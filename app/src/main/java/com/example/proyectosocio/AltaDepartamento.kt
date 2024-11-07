package com.example.proyectosocio

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.ComponentActivity

class AltaDepartamento : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alta_departamento_activity)

        val menuButton: ImageButton = findViewById(R.id.menuButton)

        // Configura el PopupMenu cuando el botón es presionado
        menuButton.setOnClickListener { view ->
            showPopupMenu(view)
        }

        val nombreDepartamento: EditText = findViewById(R.id.nombreDepartamento)
        val plantaDepartamento: EditText = findViewById(R.id.plantaDepartamento)
        val direccionDepartamento: EditText = findViewById(R.id.direccionDepartamento)

        val guardarDepartamento: Button = findViewById(R.id.guardar)

        guardarDepartamento.setOnClickListener {
            // Obtener los valores de los campos
            val nombre = nombreDepartamento.text.toString().trim()
            val planta = plantaDepartamento.text.toString().trim()
            val direccion = direccionDepartamento.text.toString().trim()

            // Validar los campos de manera individual
            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre del departamento no puede estar vacío", Toast.LENGTH_LONG).show()
                return@setOnClickListener // Detener el guardado si hay error
            }

            if (planta.isEmpty()) {
                Toast.makeText(this, "La planta del departamento no puede estar vacía", Toast.LENGTH_LONG).show()
                return@setOnClickListener // Detener el guardado si hay error
            }

            if (direccion.isEmpty()) {
                Toast.makeText(this, "La dirección del departamento no puede estar vacía", Toast.LENGTH_LONG).show()
                return@setOnClickListener // Detener el guardado si hay error
            }

            // Si todos los campos son válidos, continuar con el guardado
            try {
                val admin = AdminSQLiteOpenHelper(this, "administracion.bd", null, 1)
                val bd = admin.writableDatabase
                val registro = ContentValues()

                // Rellenar los valores del registro
                registro.put("nombre", nombre)
                registro.put("planta", planta)
                registro.put("direccion", direccion)

                // Insertar el registro en la base de datos
                bd.insert("departamentos", null, registro)
                bd.close()

                // Limpiar los campos
                nombreDepartamento.setText("")
                plantaDepartamento.setText("")
                direccionDepartamento.setText("")

                // Mostrar mensaje de éxito
                Toast.makeText(this, "Departamento guardado correctamente", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                // Mostrar mensaje de error si ocurre alguna excepción
                Toast.makeText(this, "Error al guardar el departamento: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)

        // Configura las acciones para cada elemento del menú
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_alta_socio -> startActivity(Intent(this, AltaSocio::class.java))
                R.id.menu_alta_departamento -> startActivity(Intent(this, AltaDepartamento::class.java))
                R.id.menu_baja_socio -> startActivity(Intent(this, BajaSocio::class.java))
                R.id.menu_consultar_codigo -> startActivity(Intent(this, ConsultarporCodigoSocio::class.java))
                R.id.menu_consultar_nombre -> startActivity(Intent(this, ConsultarporNombreSocio::class.java))
                R.id.menu_modificar_socio -> startActivity(Intent(this, ModificacionSocio::class.java))
            }
            true
        }
        popupMenu.show() // Muestra el menú
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Redirige al menú cuando se presiona el botón de atrás
        val intent = Intent(this, Menu::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Cierra esta actividad
    }
}