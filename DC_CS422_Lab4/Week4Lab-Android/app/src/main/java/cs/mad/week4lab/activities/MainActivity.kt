package cs.mad.week4lab.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cs.mad.week4lab.adapters.FlashcardSetAdapter
import cs.mad.week4lab.R
import cs.mad.week4lab.entities.FlashcardSet
import cs.mad.week4lab.entities.getFlashcardSets

class MainActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler = findViewById(R.id.flashcard_set_recycler)
        recycler.adapter = FlashcardSetAdapter(getFlashcardSets())
    }

    fun addSet(view: View) {
        val adapter = recycler.adapter as FlashcardSetAdapter
        adapter.add(FlashcardSet("New Set"))
        recycler.smoothScrollToPosition(adapter.itemCount)
    }
}