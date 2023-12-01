package com.azul.proyecto2.ui.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.azul.proyecto2.databinding.ActivityDetailsBinding
import com.azul.proyecto2.model.Character
import com.azul.proyecto2.network.CharactersApi
import com.azul.proyecto2.network.RetrofitService
import com.azul.proyecto2.utils.Constants
import com.bumptech.glide.Glide
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
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(call: Call<Character>, response: Response<Character>) {
                with(binding){
                    //Carga con flecha
                pbConexion.visibility = View.INVISIBLE
                    //Titulo del personaje
                tvTitle.text = response.body()?.title
                    //Datos del personaje
                tvFamily.text = response.body()?.family
                tvfirstName.text = response.body()?.firstName
                tvlastName.text = response.body()?.lastName
                    //Imagen del personaje
                Glide.with(this@DetailsActivity).load(response.body()?.imageUrl).into(binding.tvImageUrl)
                    //Casa del personaje
                val determinar = response.body()?.family.toString()
                var url_final = Constants.OTHERS_URL

                    //El texto dentro del WHEN no puede ser intercambiado por los valores dentro del archivo
                    //Strings ya que no son correctamente identificados por el programa
                    when(determinar){
                        "House Targaryen", "Targaryan" -> url_final = Constants.STARGARYEN_URL
                        "House Tarly"->url_final = Constants.TARLY_URL
                        "House Stark", "Stark"->url_final = Constants.STARK_URL
                        "House Baratheon","Baratheon"->url_final = Constants.BARATHEON_URL
                        "House Lannister", "House Lanister","Lannister"->url_final = Constants.LANNISTER_URL
                        "House Greyjoy","Greyjoy"->url_final = Constants.GREYJOY_URL
                        "House Clegane"->url_final = Constants.CLEGANE_URL
                        "House Baelish"->url_final = Constants.BAELISH_URL
                        "House Seaworth"->url_final = Constants.SEAWORTH_URL
                        "House Tyrell","Tyrell"->url_final = Constants.TYRELL_URL
                        "Tarth"->url_final = Constants.TARTH_URL
                        "Bolton"->url_final = Constants.BOLTON_URL
                        "Naharis"->url_final = Constants.NAHARIS_URL
                        "Mormont"->url_final = Constants.MORMONT_URL
                        "Bronn"->url_final = Constants.BRONN_URL
                    }
                    Glide.with(this@DetailsActivity).load(url_final).into(binding.tvImage)

                    
                }



            }

            override fun onFailure(call: Call<Character>, t: Throwable) {
                binding.pbConexion.visibility = View.INVISIBLE
                Toast.makeText(this@DetailsActivity, "@string/aviso", Toast.LENGTH_SHORT).show()
            }

        })
    }
}