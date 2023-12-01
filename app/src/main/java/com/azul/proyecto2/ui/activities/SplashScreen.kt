package com.azul.proyecto2.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.window.SplashScreenView
import androidx.core.content.ContextCompat
import com.azul.proyecto2.R
import com.azul.proyecto2.databinding.ActivityMainBinding
import com.azul.proyecto2.databinding.ActivitySplashScreenBinding
import kotlin.concurrent.thread

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //intentar utilizar varios threads para crear una aplicacion si es posible


        thread{
            Thread.sleep(6275)
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }


    }
}