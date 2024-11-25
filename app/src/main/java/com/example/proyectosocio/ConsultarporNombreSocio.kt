package com.example.proyectosocio

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

// Definimos la clase ConsultarporNombreSocio que extiende de ComponentActivity
class ConsultarporNombreSocio : ComponentActivity() {

    // Método onCreate, se ejecuta cuando la actividad es creada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establecemos el layout para esta actividad (es el archivo XML donde se definen los elementos visuales)
        setContentView(R.layout.consulta_nombre_activity)

        // Inicializamos el botón de menú con el id menuButton que debe estar en el layout XML
        val menuButton: ImageButton = findViewById(R.id.menuButton)

        // Configuramos un listener para mostrar el PopupMenu cuando se haga click en el botón de menú
        menuButton.setOnClickListener { view ->
            showPopupMenu(view) // Método que se encarga de mostrar el menú
        }

        // Inicializamos el Spinner (menú desplegable) que permite seleccionar un socio
        val spinnerSocios: Spinner = findViewById(R.id.spinnerSocios)

        // Inicializamos el objeto AdminSQLiteOpenHelper, que es el encargado de manejar la base de datos
        val admin = AdminSQLiteOpenHelper(this, "administracion.bd", null, 1)

        // Llamamos al método loadSociosToSpinnerNombre para cargar los nombres de los socios en el spinner
        admin.loadSociosToSpinnerNombre(this, spinnerSocios)

        // Inicializamos el TextView donde se mostrarán los datos del socio seleccionado
        val datos: TextView = findViewById(R.id.datos)

        // Establecemos un listener para detectar cuando el usuario selecciona un elemento en el spinner
        spinnerSocios.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            // Método que se ejecuta cuando el usuario selecciona un elemento del Spinner
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                // Obtenemos el nombre completo del socio seleccionado en el Spinner
                val selectedNombreCompleto = spinnerSocios.selectedItem.toString()

                // Verificamos si el socio seleccionado es el mensaje por defecto "Seleccione un socio"
                if (selectedNombreCompleto == "Seleccione un socio") {
                    // Si se selecciona el mensaje por defecto, ocultamos el TextView de datos
                    datos.visibility = TextView.GONE
                    return // Salimos del método sin realizar ninguna acción
                }

                // Realizamos la consulta a la base de datos usando el nombre completo (nombre + apellidos)
                val bd = admin.readableDatabase
                val query = """ 
                    SELECT nombre, apellidos, direccion, telefono, nombre_d 
                    FROM socios 
                    WHERE (nombre || ' ' || apellidos) = ? 
                """.trimIndent()  // Aquí unimos el nombre y apellidos para la búsqueda

                // Ejecutamos la consulta con el nombre completo seleccionado
                val fila = bd.rawQuery(query, arrayOf(selectedNombreCompleto))

                // Procesamos los resultados de la consulta
                if (fila.moveToFirst()) {
                    // Si encontramos un socio con el nombre completo, extraemos los datos
                    val nombreSocio = fila.getString(0)
                    val apellidos = fila.getString(1)
                    val direccion = fila.getString(2)
                    val telefono = fila.getString(3)
                    val departamento = fila.getString(4)

                    // Mostramos los datos del socio en el TextView
                    datos.text = "Nombre: $nombreSocio\nApellidos: $apellidos\nDirección: $direccion\nTeléfono: $telefono\nDepartamento: $departamento"
                    datos.visibility = TextView.VISIBLE // Hacemos visible el TextView con la información
                } else {
                    // Si no encontramos un socio con el nombre completo, mostramos un mensaje de error
                    Toast.makeText(this@ConsultarporNombreSocio, "No existe un socio con ese nombre completo", Toast.LENGTH_LONG).show()
                    // Limpiamos el TextView y lo ocultamos
                    datos.text = ""
                    datos.visibility = TextView.GONE
                }

                // Cerramos el cursor y la base de datos después de realizar la consulta
                fila.close()
                bd.close()
            }

            // Este método se ejecuta cuando no se selecciona ningún elemento del Spinner
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Si no se selecciona ningún socio, ocultamos el TextView de datos
                datos.visibility = TextView.GONE
            }
        }
    }

    // Método para mostrar el PopupMenu cuando se hace clic en el botón de menú
    private fun showPopupMenu(view: View) {
        // Creamos el objeto PopupMenu y le asignamos el layout para el menú
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)

        // Configuramos el listener para manejar las opciones seleccionadas del menú
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            // Según el ítem del menú que se seleccione, lanzamos la actividad correspondiente
            when (item.itemId) {
                R.id.menu_alta_socio -> startActivity(Intent(this, AltaSocio::class.java))
                R.id.menu_alta_departamento -> startActivity(Intent(this, AltaDepartamento::class.java))
                R.id.menu_baja_socio -> startActivity(Intent(this, BajaSocio::class.java))
                R.id.menu_consultar_codigo -> startActivity(Intent(this, ConsultarporCodigoSocio::class.java))
                R.id.menu_consultar_nombre -> startActivity(Intent(this, ConsultarporNombreSocio::class.java))
                R.id.menu_consultar_departamento -> startActivity(Intent(this, ConsultaDepartamento::class.java))
                R.id.menu_modificar_socio -> startActivity(Intent(this, ModificacionSocio::class.java))
                R.id.menu_detalle_socio -> startActivity(Intent(this, DetalleSocios::class.java))
            }
            true // Retornamos true indicando que el ítem del menú ha sido manejado
        }
        popupMenu.show() // Mostramos el menú emergente
    }

    // Método que se ejecuta cuando se presiona el botón "Atrás"
    override fun onBackPressed() {
        super.onBackPressed()
        // Creamos un Intent para redirigir al usuario al menú principal
        val intent = Intent(this, Menu::class.java)
        // Establecemos las banderas para limpiar la pila de actividades y evitar que se regrese a esta actividad
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent) // Iniciamos la actividad del menú principal
        finish() // Finalizamos la actividad actual
    }
}
