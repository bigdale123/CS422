package cs.mad.week5lab.entities

import androidx.room.*

@Entity(tableName = "cards")
data class Flashcard(
    var term: String,
    var definition: String,
    @PrimaryKey val id: Long? = null
)

@Dao
interface FlashcardDao {
    @Query("select * from cards")
    suspend fun getAll(): List<Flashcard>

    @Insert
    suspend fun insertAll(cards: List<Flashcard>)

    @Update
    suspend fun update(cards: Flashcard)

    @Delete
    suspend fun delete(cards: Flashcard)

    @Query("delete from cards")
    suspend fun deleteAll()

}