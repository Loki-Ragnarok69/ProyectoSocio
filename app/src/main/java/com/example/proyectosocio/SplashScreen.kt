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
        setContentView(R.layout.splash_screen_activity)

        //Este linea es para que se vea en pantalla completa
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)


        //Animaciones
        val animacion1 = loadAnimation(this, R.anim.scale)
        val animacion2 = loadAnimation(this, R.anim.despl_abajo)

        //Elementos
        val desc_splash = findViewById<TextView>(R.id.descrip_splash)
        val imgLogo = findViewById<ImageView>(R.id.logoImageView)

        //Enlazar animaciones
        desc_splash.setAnimation(animacion2)
        imgLogo.setAnimation(animacion1)

        Handler(Looper.getMainLooper()).postDelayed({
            val mainIntent = Intent(this, Menu::class.java)
            startActivity(mainIntent)
            finish()
        }, 4000)
    }
}