package com.example.proyectosocio

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.view.GestureDetectorCompat

// La actividad que muestra la información detallada de un socio
class InformacionSocios : ComponentActivity() {

    // Declaramos una variable para detectar gestos de deslizamiento (swipe)
    private lateinit var gestureDetector: GestureDetectorCompat

    // Método que se llama cuando la actividad se crea
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.informacion_socios_activity)

        // Obtenemos el botón de menú (un ImageButton)
        val menuButton: ImageButton = findViewById(R.id.menuButton)

        // Configuramos un Listener para el botón de menú
        // Cuando el botón sea presionado, se mostrará un menú emergente
        menuButton.setOnClickListener { view ->
            showPopupMenu(view)
        }

        // Inicializamos el detector de gestos para la detección de swipe
        gestureDetector = GestureDetectorCompat(this, GestureListener())

        // Obtenemos el objeto 'socio' que fue pasado desde la actividad anterior
        // Se espera que el 'socio' sea un objeto serializado de la clase Socio
        val socio = intent.getSerializableExtra("socio") as? Socio

        // Referenciamos los TextViews donde se mostrará la información del socio
        val textNombre: TextView = findViewById(R.id.textNombre)
        val textApellido: TextView = findViewById(R.id.textApellido)
        val textDireccion: TextView = findViewById(R.id.textDireccion)
        val textTelefono: TextView = findViewById(R.id.textTelefono)
        val textDepartamento: TextView = findViewById(R.id.textDepartamento)

        // Si el socio no es null, actualizamos los TextViews con sus datos
        socio?.let {
            textNombre.text = "Nombre: ${it.nombre}" // Mostramos el nombre del socio
            textApellido.text = "Apellido: ${it.apellidos}" // Mostramos los apellidos del socio
            textDireccion.text = "Dirección: ${it.direccion}" // Mostramos la dirección del socio
            textTelefono.text = "Teléfono: ${it.telefono}" // Mostramos el teléfono del socio
            textDepartamento.text = "Departamento: ${it.nombre_d}" // Mostramos el nombre del departamento
        }
    }

    // Función que muestra el menú emergente (PopupMenu)
    private fun showPopupMenu(view: View) {
        // Creamos un objeto PopupMenu y lo vinculamos al botón presionado
        val popupMenu = PopupMenu(this, view)
        // Inflamos el menú desde el archivo XML de menú
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)

        // Configuramos el listener para cada opción del menú
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
            true // Indicamos que el ítem fue procesado
        }
        // Mostramos el menú emergente
        popupMenu.show()
    }

    // Sobrescribimos el método onTouchEvent para detectar los gestos de deslizamiento
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Pasamos el evento de toque al GestureDetector para que lo maneje
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    // Clase interna para manejar los gestos de deslizamiento
    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        // Definimos los umbrales para detectar el deslizamiento
        private val SWIPE_THRESHOLD = 100 // Distancia mínima para considerar que hubo un deslizamiento
        private val SWIPE_VELOCITY_THRESHOLD = 100 // Velocidad mínima para considerar que el gesto fue un deslizamiento rápido

        // Sobrescribimos el método onFling, que se llama cuando detectamos un deslizamiento
        override fun onFling(
            e1: MotionEvent?, // Primer evento de toque
            e2: MotionEvent, // Segundo evento de toque
            velocityX: Float, // Velocidad del deslizamiento en el eje X
            velocityY: Float // Velocidad del deslizamiento en el eje Y
        ): Boolean {
            // Calculamos la diferencia en la posición horizontal (eje X) entre los dos toques
            val diffX = e2.x - (e1?.x ?: 0f)

            // Comprobamos si el deslizamiento fue hacia la derecha
            // y si la velocidad del deslizamiento y la distancia superan los umbrales definidos
            return if (diffX > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                finish() // Si fue un deslizamiento hacia la derecha, cerramos la actividad
                true
            } else {
                false // Si no fue un deslizamiento válido, no hacemos nada
            }
        }
    }
}
