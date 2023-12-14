package cs.mad.week5lab.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.room.Room
import cs.mad.week5lab.databinding.ActivityStudySetBinding
import cs.mad.week5lab.entities.AppDatabase
import cs.mad.week5lab.entities.Flashcard
import cs.mad.week5lab.runOnIO

class StudySetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudySetBinding
    private var flashcards = listOf<Flashcard>().toMutableList()
    private val totalAmount = flashcards.size
    private var isShowingDefinition = false
    private var position = 0
        set(value) = if (value == flashcards.size) field = 0 else field = value
    private var missed = mutableListOf<Flashcard>()
    private var correct = 0
    private var completed = 0
    private val isComplete get() = flashcards.size == 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudySetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "AppDatabase").build()
        runOnIO { flashcards = db.flashcardDao.getAll().toMutableList() }

        binding.currentCard.setOnClickListener {
            isShowingDefinition = !isShowingDefinition
            updateCurrentCard()
        }
        updateCurrentCard()
        binding.completedCount.text = "0/$totalAmount"
    }

    private fun updateCurrentCard() {
        binding.currentCard.text = if (isComplete) "Set Complete!!!"
        else if (isShowingDefinition) flashcards[position].definition else flashcards[position].term
    }

    fun missCurrent(view: View) {
        if (isComplete) return

        if (!missed.contains(flashcards[position])) missed.add(flashcards[position])
        binding.missedCount.text = "Missed: ${missed.size}"
        skipCurrent(view)
    }

    fun skipCurrent(view: View) {
        if (isComplete) return

        position++

        isShowingDefinition = false
        updateCurrentCard()
    }

    fun correctCurrent(view: View) {
        if (isComplete) return

        completed++
        if (!missed.contains(flashcards[position])) correct++
        binding.completedCount.text = "$completed/$totalAmount"
        binding.correctCount.text = "Correct: $correct"
        flashcards.removeAt(position)
        if (position == flashcards.size) position = 0

        isShowingDefinition = false
        updateCurrentCard()
    }

    fun quitStudying(view: View) {
        finish()
    }
}