package com.example.proyectosocio

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

// Clase personalizada Adapter para mostrar una lista de objetos 'Socio' en una vista tipo ListView o Spinner.
class SocioAdapter(context: Context, socios: List<Socio>) : ArrayAdapter<Socio>(context, 0, socios) {

    // Método para obtener una vista que representará a un socio en la lista.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        // Si convertView es nulo, inflamos un nuevo layout para el item de la lista
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_socio, parent, false)

        // Obtenemos el objeto 'Socio' en la posición actual de la lista
        val socio = getItem(position)

        // Referenciamos el TextView donde se mostrará el nombre y apellido del socio
        val textNombreApellido: TextView = view.findViewById(R.id.textNombreApellido)

        // Establecemos el texto del TextView con el nombre y apellido del socio
        textNombreApellido.text = "${socio?.nombre} ${socio?.apellidos}"

        // Devolvemos la vista configurada para este ítem
        return view
    }
}
