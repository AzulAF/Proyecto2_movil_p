package com.azul.proyecto2.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azul.proyecto2.databinding.ActivityMainBinding
import com.azul.proyecto2.model.Character
import com.azul.proyecto2.network.CharactersApi
import com.azul.proyecto2.network.RetrofitService
import com.azul.proyecto2.ui.adapters.CharactersAdapter
import com.azul.proyecto2.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val call = RetrofitService.getRetrofit().create(CharactersApi::class.java).getCharacterList("api/v2/Characters")

        call.enqueue(object: Callback<ArrayList<Character>>{
            override fun onResponse(
                call: Call<ArrayList<Character>>,
                response: Response<ArrayList<Character>>
            ) {
                binding.pbConexion.visibility = View.INVISIBLE

                //Log.d(Constants.LOGTAG, "Respuesta del servidor: ${response.toString()}")
                //Log.d(Constants.LOGTAG, "Datos: ${response.body().toString()}")

                val charAdapter = CharactersAdapter(response.body()!!){
                    character ->
                    val bundle = bundleOf("id" to character.id)
                    val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
                //Toast.makeText(this@MainActivity, "Entro al personaje ${response.body().toString()}", Toast.LENGTH_SHORT).show()

                binding.rvMenu.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
                binding.rvMenu.adapter = charAdapter

            }

            override fun onFailure(call: Call<ArrayList<Character>>, t: Throwable) {
                binding.pbConexion.visibility = View.INVISIBLE
                Toast.makeText(this@MainActivity, "@strings/aviso", Toast.LENGTH_SHORT).show()
            }

        })




    }
}