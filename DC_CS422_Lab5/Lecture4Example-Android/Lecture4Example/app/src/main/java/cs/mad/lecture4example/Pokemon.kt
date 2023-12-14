package cs.mad.lecture4example

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import com.google.gson.annotations.SerializedName

data class PokemonContainer(
    @SerializedName("results") val result: List<Pokemon>
)

@Entity
data class Pokemon(
    val name: String,
    val url: String,
    @PrimaryKey val id: Long? = null
)

@Dao
interface PokemonDao {
    @Query("select * from pokemon")
    suspend fun getAll(): List<Pokemon>

    @Insert
    suspend fun insertAll(pokemon: List<Pokemon>)

    @Update
    suspend fun update(pokemon: Pokemon)

    @Delete
    suspend fun delete(pokemon: Pokemon)

    @Query("delete from pokemon")
    suspend fun deleteAll()
}