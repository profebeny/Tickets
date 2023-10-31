package com.overcome.tickets.ui.Activitys

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.overcome.tickets.MainActivity
import com.overcome.tickets.R

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSesion)
        val txtUsuario = findViewById<EditText>(R.id.txtUsuario)


        //Limpia el Usuario Preference
        val preferences = getSharedPreferences("datos", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("Usuario", "")
        editor.commit()

        btnIniciarSesion.setOnClickListener{
            editor.putString("Usuario", ""+txtUsuario.text)
            editor.commit()
            val intento1 = Intent(this, MainActivity::class.java)
            startActivity(intento1)
            finish()
        }

    }
    override fun onBackPressed() {
        AlertDialog.Builder(this@Login)
            .setMessage("¿Salir de la aplicación?")
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, whichButton ->
                finishAffinity() //Sale de Activity.
            }
            .setNegativeButton("Cancelar") { dialog, whichButton ->

            }
            .show()
    }
}