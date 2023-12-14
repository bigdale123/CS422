package cs.mad.lecture4example

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Pokemon::class], version = 1)
abstract class PokemonDatabase: RoomDatabase() {
    abstract val pokemonDao: PokemonDao
}