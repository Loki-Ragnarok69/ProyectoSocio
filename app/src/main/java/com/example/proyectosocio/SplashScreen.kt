package com.example.proyectosocio

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity

class SplashScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el layout para esta actividad (pantalla de inicio)
        setContentView(R.layout.splash_screen_activity)

        // Configura la ventana para que la actividad se muestre en pantalla completa
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Cargamos las animaciones desde los recursos (archivos XML en res/anim)
        // 'scale' es una animación para el logo (probablemente hace que el logo crezca)
        val animacion1 = loadAnimation(this, R.anim.scale)
        // 'despl_abajo' es una animación para el texto (probablemente hace que el texto se desplace hacia abajo)
        val animacion2 = loadAnimation(this, R.anim.despl_abajo)

        // Buscamos los elementos en el layout donde se aplicarán las animaciones
        val desc_splash = findViewById<TextView>(R.id.descrip_splash)  // Texto de la pantalla de inicio
        val imgLogo = findViewById<ImageView>(R.id.logoImageView)  // Imagen del logo

        // Asignamos las animaciones a los elementos visuales
        desc_splash.setAnimation(animacion2)  // Aplica la animación al texto
        imgLogo.setAnimation(animacion1)  // Aplica la animación al logo

        // Usamos un Handler para ejecutar una acción después de un retraso (2 segundos en este caso)
        Handler(Looper.getMainLooper()).postDelayed({
            // Después de 2 segundos, lanzamos la actividad principal (Menu)
            val mainIntent = Intent(this, Menu::class.java)
            startActivity(mainIntent)  // Inicia la actividad de menú
            finish()  // Finaliza la actividad SplashScreen para que no pueda volver atrás
        }, 2000)  // 2000 milisegundos = 2 segundos
    }
}
