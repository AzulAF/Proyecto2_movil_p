package com.azul.proyecto2.model

import com.google.gson.annotations.SerializedName

data class Character(
    @SerializedName("id")
    var id: String?,
    @SerializedName("firstName")
    var firstName: String?,
    @SerializedName("lastName")
    var lastName: String?,
    @SerializedName("fullName")
    var fullName: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("family")
    var family: String?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("imageUrl")
    var imageUrl: String?
)

