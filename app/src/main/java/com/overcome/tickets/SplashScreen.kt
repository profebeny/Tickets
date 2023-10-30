package com.overcome.tickets

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        val preferences = getSharedPreferences("datos", MODE_PRIVATE)
        val Usuario = preferences.getString("Usuario", "")



        Handler(Looper.getMainLooper()).postDelayed({
            if(Usuario.equals(""))
            {
                val intento1 = Intent(this, Login::class.java)
                startActivity(intento1)
                finish()
            }else{
                val intento2 = Intent(this, MainActivity::class.java)
                startActivity(intento2)
                finish()
            }
        }, 2500)


    }
}