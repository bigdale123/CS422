package cs.mad.week3lab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.recycler_view);
        val adapter = FlashcardSetAdapter();
        recycler.adapter = adapter;

        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener{
            adapter.add()
        }
    }
}