package cs.mad.lecture4example

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import cs.mad.lecture4example.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), Callback<PokemonContainer> {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var pokemonDao: PokemonDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Room.databaseBuilder(
            applicationContext,
            PokemonDatabase::class.java, DATABASE_NAME
        ).build()
        pokemonDao = db.pokemonDao

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        var pokemon: List<Pokemon> = listOf()
        runOnIO { pokemon = pokemonDao.getAll() }
        binding.pokemonRecycler.adapter = PokemonAdapter(pokemon)

        //clearStorage()
        loadData()
    }

    private fun loadData() {
        if (prefs.getBoolean(IS_DOWNLOADED_KEY, false)) {
            runOnIO { (binding.pokemonRecycler.adapter as PokemonAdapter).update(pokemonDao.getAll()) }
        } else {
            PokemonWebservice().pokemonService.getPokemon().enqueue(this)
        }
    }

    private fun clearStorage() {
        runOnIO {
            prefs.edit().putBoolean(IS_DOWNLOADED_KEY, false).apply()
            pokemonDao.deleteAll()
        }
    }

    override fun onResponse(
        call: Call<PokemonContainer>,
        response: Response<PokemonContainer>
    ) {
        if (response.isSuccessful) {
            Log.d("onResponse", "download success!")
            val pokemon = response.body()?.result
            pokemon?.let {
                (binding.pokemonRecycler.adapter as PokemonAdapter).update(pokemon)
                runOnIO { pokemonDao.insertAll(pokemon) }
            }
            prefs.edit().putBoolean(IS_DOWNLOADED_KEY, true).apply()
        }
    }

    override fun onFailure(call: Call<PokemonContainer>, t: Throwable) {
        Log.e("onFailure", t.message!!)
    }

    companion object {
        const val DATABASE_NAME = "app_database"
        const val PREFS_NAME = "app_prefs"
        const val IS_DOWNLOADED_KEY = "isDownloaded"
    }
}

fun runOnIO(lambda: suspend () -> Unit) {
    runBlocking {
        launch(Dispatchers.IO) { lambda() }
    }
}