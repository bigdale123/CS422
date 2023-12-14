package cs.mad.week3lab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class FlashcardSetDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard_set_detail)

        // No clue how hacky this is, but figured out how to dynamically change Set Title
        // By passing values along with the intent when creating the activity.
        //
        // I also know it wasn't required, but it made the most sense.
        // https://www.geeksforgeeks.org/how-to-use-putextra-and-getextra-for-string-data-in-android/
        findViewById<TextView>(R.id.set_title).text = intent.getStringExtra("title")

        val recycler = findViewById<RecyclerView>(R.id.recycler_view_activity);
        val adapter = FlashcardAdapter();
        recycler.adapter = adapter;

        findViewById<Button>(R.id.add_button).setOnClickListener{
            adapter.add("New Item");
        }
    }
}