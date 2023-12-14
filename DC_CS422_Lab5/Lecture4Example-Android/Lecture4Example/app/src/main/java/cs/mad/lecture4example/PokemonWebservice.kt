package cs.mad.lecture4example

import android.graphics.Region
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class PokemonWebservice {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val pokemonService = retrofit.create(PokemonService::class.java)

    interface PokemonService {
        @GET("pokemon")
        fun getPokemon(): Call<PokemonContainer>
    }
}