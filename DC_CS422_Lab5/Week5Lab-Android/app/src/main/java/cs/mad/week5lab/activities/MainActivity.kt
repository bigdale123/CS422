package cs.mad.week5lab.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import cs.mad.week5lab.adapters.FlashcardSetAdapter
import cs.mad.week5lab.R
import cs.mad.week5lab.databinding.ActivityMainBinding
import cs.mad.week5lab.entities.AppDatabase
import cs.mad.week5lab.entities.FlashcardSet
import cs.mad.week5lab.entities.FlashcardSetDao
import cs.mad.week5lab.runOnIO


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var flashcardSetDao: FlashcardSetDao
    private var flashcard_sets: List<FlashcardSet> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "AppDatabase").build()
        flashcardSetDao = db.flashcardSetDao

        runOnIO { flashcard_sets = flashcardSetDao.getAll() }
        binding.flashcardSetRecycler.adapter = FlashcardSetAdapter(flashcard_sets)
    }

    fun addSet(view: View) {
        val adapter = binding.flashcardSetRecycler.adapter as FlashcardSetAdapter
        runOnIO { flashcardSetDao.insertAll(listOf(FlashcardSet("New Set",null))) }
        adapter.add(FlashcardSet("New Set"))
        binding.flashcardSetRecycler.smoothScrollToPosition(adapter.itemCount)
    }
}