package com.example.proyectosocio

// Importación de las clases necesarias para manejar la interfaz de usuario y otras funcionalidades
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity

// Definición de la clase ConsultaDepartamento que extiende de ComponentActivity
class ConsultaDepartamento : ComponentActivity() {
    // Método onCreate() que se ejecuta cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el layout de la actividad
        setContentView(R.layout.consulta_departamento_activity)

        // Referencias a los elementos de la interfaz de usuario
        val menuButton: ImageButton = findViewById(R.id.menuButton)  // Botón de menú para abrir el PopupMenu
        val spinnerDepartamentos: Spinner = findViewById(R.id.spinnerDepartamentos)  // Spinner para seleccionar el departamento
        val datos: TextView = findViewById(R.id.datos)  // TextView donde se mostrarán los datos de los socios
        datos.movementMethod = ScrollingMovementMethod()  // Habilitar el desplazamiento en el TextView para mostrar más datos

        // Instancia de AdminSQLiteOpenHelper para interactuar con la base de datos
        val admin = AdminSQLiteOpenHelper(this, "administracion.bd", null, 1)

        // Cargar los nombres de los departamentos en el spinner
        admin.loadDetalleDepartamentosToSpinner(this, spinnerDepartamentos)

        // Configuración del listener para manejar la selección de un departamento en el spinner
        spinnerDepartamentos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                // Obtener el nombre del departamento seleccionado
                val selectedDepartamento = spinnerDepartamentos.selectedItem.toString()

                // Verificar que no se haya seleccionado la opción por defecto
                if (selectedDepartamento == "Seleccione un departamento") {
                    datos.visibility = TextView.GONE  // Si se selecciona la opción por defecto, ocultar el TextView
                    return
                }

                // Obtener los socios que pertenecen al departamento seleccionado
                val sociosList = admin.getSociosPorDepartamento(selectedDepartamento)

                // Mostrar los datos de los socios en el TextView
                if (sociosList.isNotEmpty()) {
                    // Si hay socios, unir los datos de cada socio en una cadena y mostrarla en el TextView
                    datos.text = sociosList.joinToString("\n\n") { socio ->
                        "Nombre: ${socio.nombre}\nApellidos: ${socio.apellidos}\nDirección: ${socio.direccion}\nTeléfono: ${socio.telefono}\n" +
                                "Departamento: ${socio.nombre_d}"
                    }
                    datos.setBackgroundResource(R.drawable.textview_borde_redondeado)  // Establecer un borde redondeado al TextView
                    datos.visibility = TextView.VISIBLE  // Hacer visible el TextView
                } else {
                    // Si no hay socios en ese departamento, mostrar un mensaje en el TextView
                    datos.text = "No hay socios en este departamento"
                    datos.visibility = TextView.VISIBLE  // Hacer visible el TextView
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                datos.visibility = TextView.GONE  // Si no se selecciona nada, ocultar el TextView
            }
        }

        // Configuración del botón de menú para abrir el PopupMenu
        menuButton.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }

    // Función que muestra el menú emergente (PopupMenu) cuando se presiona el botón de menú
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)  // Crear el PopupMenu
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)  // Infla el menú desde el archivo XML

        // Configura las acciones para cada elemento del menú
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
        popupMenu.show()  // Muestra el menú emergente
    }

    // Función que se ejecuta cuando el botón "Atrás" es presionado
    override fun onBackPressed() {
        super.onBackPressed()

        // Redirige al menú principal cuando se presiona el botón de atrás
        val intent = Intent(this, Menu::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()  // Cierra la actividad actual
    }
}
