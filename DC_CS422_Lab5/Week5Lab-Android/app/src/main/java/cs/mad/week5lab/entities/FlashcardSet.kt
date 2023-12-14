package cs.mad.week5lab.entities

import androidx.room.*

@Entity(tableName = "sets")
data class FlashcardSet(
    val title: String,
    @PrimaryKey val id: Long? = null
)

@Dao
interface FlashcardSetDao {
    @Query("select * from sets")
    suspend fun getAll(): List<FlashcardSet>

    @Insert
    suspend fun insertAll(sets: List<FlashcardSet>)

    @Update
    suspend fun update(set: FlashcardSet)

    @Delete
    suspend fun delete(set: FlashcardSet)

    @Query("delete from sets")
    suspend fun deleteAll()

}