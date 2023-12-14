package cs.mad.week4lab.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import cs.mad.week4lab.adapters.FlashcardAdapter
import cs.mad.week4lab.databinding.ActivityFlashcardSetDetailBinding
import cs.mad.week4lab.entities.Flashcard
import cs.mad.week4lab.entities.getFlashcards

class FlashcardSetDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFlashcardSetDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlashcardSetDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = FlashcardAdapter(getFlashcards())
        binding.flashcardRecycler.adapter = adapter

        // swipe to delete
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(v: RecyclerView, h: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false
            override fun onSwiped(h: RecyclerView.ViewHolder, dir: Int) = adapter.removeAt(h.adapterPosition)
        }).attachToRecyclerView(binding.flashcardRecycler)
    }

    fun addFlashcard(view: View) {
        val adapter = binding.flashcardRecycler.adapter as FlashcardAdapter
        adapter.add(Flashcard("New Term", "New Definition"))
        binding.flashcardRecycler.smoothScrollToPosition(adapter.itemCount)
    }

    fun startStudying(view: View) {
        startActivity(Intent(this, StudySetActivity::class.java))
    }
}