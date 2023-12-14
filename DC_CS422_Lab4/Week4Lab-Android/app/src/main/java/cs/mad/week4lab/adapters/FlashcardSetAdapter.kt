package cs.mad.week4lab.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cs.mad.week4lab.R
import cs.mad.week4lab.activities.FlashcardSetDetailActivity
import cs.mad.week4lab.entities.FlashcardSet

class FlashcardSetAdapter(dataSet: List<FlashcardSet>): RecyclerView.Adapter<FlashcardSetAdapter.ViewHolder>() {
    private val data = dataSet.toMutableList()

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.title_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.flashcard_set_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = data[position].title
        holder.itemView.setOnClickListener {
            it.context.startActivity(Intent(it.context, FlashcardSetDetailActivity::class.java))
        }
    }

    override fun getItemCount() = data.size

    fun add(set: FlashcardSet) {
        data.add(set)
        notifyItemInserted(data.size - 1)
    }
}