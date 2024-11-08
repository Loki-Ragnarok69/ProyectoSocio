package com.example.proyectosocio

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.ComponentActivity

class DetalleSocios : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalle_socios_activity)

        val listView: ListView = findViewById(R.id.listViewSocios)

        // Instancia de AdminSQLiteOpenHelper y recuperación de datos
        val admin = AdminSQLiteOpenHelper(this, "administracion.bd", null, 1)
        val socios = admin.obtenerTodosLosSocios()

        // Verificar si la lista de socios no está vacía
        if (socios.isEmpty()) {
            Toast.makeText(this, "No hay socios registrados", Toast.LENGTH_LONG).show()
        } else {
            val adapter = SocioAdapter(this, socios)
            listView.adapter = adapter
        }

        // Configurar el evento de clic en el ListView
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val socioSeleccionado = socios[position]
            val intent = Intent(this, InformacionSocios::class.java)
            intent.putExtra("socio", socioSeleccionado)
            startActivity(intent)
        }
    }
}