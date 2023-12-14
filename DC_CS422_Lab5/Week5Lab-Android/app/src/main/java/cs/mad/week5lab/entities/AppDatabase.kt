package cs.mad.week5lab.entities
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Flashcard::class, FlashcardSet::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract val flashcardDao: FlashcardDao
    abstract val flashcardSetDao: FlashcardSetDao
}