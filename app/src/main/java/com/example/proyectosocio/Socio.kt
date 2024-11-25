package com.example.proyectosocio

import java.io.Serializable

// Definición de la clase de datos 'Socio'
data class Socio(
    val nombre: String,      // Atributo para almacenar el nombre del socio
    val apellidos: String,   // Atributo para almacenar los apellidos del socio
    val direccion: String,   // Atributo para almacenar la dirección del socio
    val telefono: String,    // Atributo para almacenar el teléfono del socio
    val nombre_d: String     // Atributo para almacenar el nombre del departamento al que pertenece el socio
) : Serializable  // Implementación de la interfaz Serializable para permitir la conversión a un formato serializado
