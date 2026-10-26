package com.example.adotapet.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path

data class DogImagesResponse(
    @SerializedName("message") val message: List<String>,
    @SerializedName("status") val status: String
)

interface DogApi {

    @GET("breeds/image/random/{quantidade}")
    suspend fun imagensAleatorias(@Path("quantidade") quantidade: Int): DogImagesResponse
}
