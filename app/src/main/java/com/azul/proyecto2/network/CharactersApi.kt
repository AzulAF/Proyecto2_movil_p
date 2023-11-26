package com.azul.proyecto2.network

import com.azul.proyecto2.model.Character
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url


interface CharactersApi {
    @GET
    fun getCharacterList(
        //url para que retrofit sepa como armar
        @Url url:  String?
    ): Call<ArrayList<Character>>

    //Aqui colocar el endpoint correcto
    @GET("api/v2/Characters/{id}/")
    fun getCharacterDetail(
        @Path("id") id: String?
    ): Call<Character>

}