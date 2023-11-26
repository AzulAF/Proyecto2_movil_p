package com.azul.proyecto2.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.azul.proyecto2.databinding.ActivityDetailsBinding
import com.azul.proyecto2.model.Character
import com.azul.proyecto2.network.CharactersApi
import com.azul.proyecto2.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val id = bundle?.getString("id", "0")
        val call = RetrofitService.getRetrofit().create(CharactersApi::class.java).getCharacterDetail(id)

        call.enqueue(object: Callback<Character>{
            override fun onResponse(call: Call<Character>, response: Response<Character>) {
                with(binding){
                    //Carga con flecha
                pbConexion.visibility = View.INVISIBLE
                    //Titulo del personaje
                tvTitle.text = response.body()?.title
                    //Imagen de la casa del personaje
                //Glide.with(this@DetailsActivity).load(response.body()?.image).into(ivImage)
                tvFamily.text = response.body()?.family
                tvfirstName.text = response.body()?.firstName
                tvlastName.text = response.body()?.lastName
                }

            }

            override fun onFailure(call: Call<Character>, t: Throwable) {
                binding.pbConexion.visibility = View.INVISIBLE
                Toast.makeText(this@DetailsActivity, "No hay conexion disponible, revise su internet.", Toast.LENGTH_SHORT).show()
            }

        })
    }
}