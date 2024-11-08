package com.example.proyectosocio

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity

class InformacionSocios : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.informacion_socios_activity)

        val socio = intent.getSerializableExtra("socio") as? Socio

        val textNombre: TextView = findViewById(R.id.textNombre)
        val textApellido: TextView = findViewById(R.id.textApellido)
        val textDireccion: TextView = findViewById(R.id.textDireccion)
        val textTelefono: TextView = findViewById(R.id.textTelefono)
        val textDepartamento: TextView = findViewById(R.id.textDepartamento)

        socio?.let {
            textNombre.text = "Nombre: ${it.nombre}"
            textApellido.text = "Apellido: ${it.apellidos}"
            textDireccion.text = "Dirección: ${it.direccion}"
            textTelefono.text = "Teléfono: ${it.telefono}"
            textDepartamento.text = "Departamento: ${it.nombre_d}"
        }
    }
}
