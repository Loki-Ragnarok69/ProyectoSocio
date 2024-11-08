package com.example.proyectosocio

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SocioAdapter(context: Context, socios: List<Socio>) : ArrayAdapter<Socio>(context, 0, socios) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_socio, parent, false)

        val socio = getItem(position)
        val textNombreApellido: TextView = view.findViewById(R.id.textNombreApellido)

        textNombreApellido.text = "${socio?.nombre} ${socio?.apellidos}"

        return view
    }
}
