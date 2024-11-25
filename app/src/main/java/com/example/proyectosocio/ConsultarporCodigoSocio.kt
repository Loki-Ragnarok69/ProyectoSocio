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

class ConsultarporCodigoSocio : ComponentActivity() {
    // Método onCreate() que se ejecuta cuando la actividad se crea
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.consulta_codigo_activity)  // Establece el layout de la actividad

        // Referencia al botón de menú (ImageButton)
        val menuButton: ImageButton = findViewById(R.id.menuButton)

        // Configura el PopupMenu cuando el botón es presionado
        menuButton.setOnClickListener { view ->
            showPopupMenu(view)  // Muestra el menú emergente (PopupMenu)
        }

        // Referencia al Spinner donde se seleccionan los socios
        val spinnerSocios: Spinner = findViewById(R.id.spinnerSocios)

        // Instancia de AdminSQLiteOpenHelper para interactuar con la base de datos
        val admin = AdminSQLiteOpenHelper(this, "administracion.bd", null, 1)

        // Cargar los socios en el Spinner desde la base de datos
        admin.loadSociosToSpinner(this, spinnerSocios)

        // Referencia al TextView donde se mostrarán los detalles del socio seleccionado
        val datos: TextView = findViewById(R.id.datos)

        // Establecer el listener del Spinner para manejar la selección de un socio
        spinnerSocios.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                datos.setBackgroundResource(R.drawable.textview_borde_redondeado)  // Agregar borde redondeado al TextView

                // Obtener el código del socio seleccionado
                val selectedCodigo = spinnerSocios.selectedItem.toString()

                // Verificar si el usuario ha seleccionado un socio válido
                if (selectedCodigo == "Seleccione un socio") {
                    datos.visibility = TextView.GONE  // Hacer invisible el TextView si no se ha seleccionado un socio válido
                    return  // Salir si no hay selección válida
                }

                // Consulta a la base de datos para obtener información del socio
                val bd = admin.readableDatabase
                val fila = bd.rawQuery(
                    "SELECT nombre, apellidos, direccion, telefono, nombre_d FROM socios WHERE rowid = ?",
                    arrayOf(selectedCodigo)  // Usar el código del socio como parámetro en la consulta
                )

                // Procesar los resultados de la consulta
                if (fila.moveToFirst()) {
                    // Si se encuentra el socio, obtener sus datos
                    val nombre = fila.getString(0)
                    val apellidos = fila.getString(1)
                    val direccion = fila.getString(2)
                    val telefono = fila.getString(3)
                    val departamento = fila.getString(4)

                    // Mostrar los datos del socio en el TextView
                    datos.text = "Nombre: $nombre\nApellidos: $apellidos\nDirección: $direccion\nTeléfono: $telefono\nDepartamento: $departamento"
                    datos.visibility = TextView.VISIBLE  // Hacer visible el TextView con los datos
                } else {
                    // Si no se encuentra el socio, mostrar un mensaje de error
                    Toast.makeText(this@ConsultarporCodigoSocio, "No existe un socio con el código seleccionado", Toast.LENGTH_LONG).show()
                    datos.text = ""  // Limpiar el TextView
                    datos.visibility = TextView.GONE  // Hacer invisible el TextView
                }

                // Cerrar el cursor y la base de datos
                fila.close()
                bd.close()
            }

            // Método cuando no se ha seleccionado un socio
            override fun onNothingSelected(parent: AdapterView<*>) {
                datos.visibility = TextView.GONE  // Hacer invisible el TextView si no hay selección
            }
        }
    }

    // Función que muestra el PopupMenu cuando se presiona el botón de menú
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)  // Inflar el menú desde el XML

        // Configura las acciones para cada opción del menú
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
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
            true
        }
        popupMenu.show()  // Muestra el menú
    }

    // Método que se ejecuta al presionar el botón "Atrás"
    override fun onBackPressed() {
        super.onBackPressed()

        // Redirige al menú principal cuando se presiona "Atrás"
        val intent = Intent(this, Menu::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()  // Cierra la actividad actual
    }
}
