package com.example.proyectosocio

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.ComponentActivity

// Definimos la clase DetalleSocios que extiende de ComponentActivity
class DetalleSocios : ComponentActivity() {

    // El método onCreate se ejecuta cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establecemos el layout XML que corresponde a esta actividad
        setContentView(R.layout.detalle_socios_activity)

        // Inicializamos el botón de menú usando el id "menuButton" que está en el layout XML
        val menuButton: ImageButton = findViewById(R.id.menuButton)

        // Configuramos el listener del botón para mostrar un menú emergente (PopupMenu) cuando se haga clic
        menuButton.setOnClickListener { view ->
            showPopupMenu(view) // Método para mostrar el PopupMenu
        }

        // Inicializamos el ListView que mostrará los socios registrados
        val listView: ListView = findViewById(R.id.listViewSocios)

        // Creamos una instancia de la clase AdminSQLiteOpenHelper para acceder a la base de datos
        val admin = AdminSQLiteOpenHelper(this, "administracion.bd", null, 1)

        // Obtenemos todos los socios desde la base de datos usando el método "obtenerTodosLosSocios"
        val socios = admin.obtenerTodosLosSocios()

        // Verificamos si la lista de socios está vacía
        if (socios.isEmpty()) {
            // Si no hay socios registrados, mostramos un mensaje Toast
            Toast.makeText(this, "No hay socios registrados", Toast.LENGTH_LONG).show()
        } else {
            // Si hay socios, creamos un adaptador para el ListView
            val adapter = SocioAdapter(this, socios)
            // Establecemos el adaptador al ListView para mostrar los socios
            listView.adapter = adapter
        }

        // Configuramos el listener para detectar el clic en cualquier elemento de la lista (socios)
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // Obtenemos el socio seleccionado de la lista
            val socioSeleccionado = socios[position]

            // Creamos un Intent para abrir la actividad InformacionSocios
            val intent = Intent(this, InformacionSocios::class.java)

            // Enviamos el objeto socio seleccionado como extra en el Intent
            intent.putExtra("socio", socioSeleccionado)

            // Iniciamos la actividad InformacionSocios
            startActivity(intent)
        }
    }

    // Método que configura y muestra el PopupMenu cuando el usuario hace clic en el botón del menú
    private fun showPopupMenu(view: View) {
        // Creamos el PopupMenu y le asignamos el layout de menú
        val popupMenu = PopupMenu(this, view)
        // Inflamos el menú con las opciones definidas en el archivo XML (menu.xml)
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)

        // Configuramos el listener para manejar las acciones cuando el usuario selecciona un ítem del menú
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            // Según el ítem seleccionado, se abre la actividad correspondiente
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
            // Retornamos true para indicar que hemos manejado la selección del menú
            true
        }
        // Mostramos el menú emergente
        popupMenu.show()
    }

    // Método que se ejecuta cuando el usuario presiona el botón de "Atrás"
    override fun onBackPressed() {
        super.onBackPressed()

        // Creamos un Intent para redirigir al usuario al menú principal
        val intent = Intent(this, Menu::class.java)

        // Establecemos banderas en el Intent para limpiar la pila de actividades y evitar que el usuario regrese a esta actividad
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        // Iniciamos la actividad del menú principal
        startActivity(intent)

        // Finalizamos la actividad actual
        finish()
    }
}
