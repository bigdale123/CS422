package cs.mad.week4lab.adapters

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cs.mad.week4lab.R
import cs.mad.week4lab.entities.Flashcard

class FlashcardAdapter(dataSet: List<Flashcard>): RecyclerView.Adapter<FlashcardAdapter.ViewHolder>() {
    private val data = dataSet.toMutableList()

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.term_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.flashcard_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flashcard = data[position]
        holder.textView.text = flashcard.term

        holder.itemView.setOnClickListener {
            showDefaultDialog(holder.itemView.context, position);

        }
    }

    override fun getItemCount() = data.size

    fun add(flashcard: Flashcard) {
        data.add(flashcard)
        notifyItemInserted(data.size - 1)
    }

    fun showDefaultDialog(context: Context, position: Int){
        AlertDialog.Builder(context)
            .setTitle(data[position].term)
            .setMessage(data[position].definition)
            .setPositiveButton("Edit") {dialogInterface: DialogInterface, i: Int ->
                showEditableDialog(context, position)
            }
            .create()
            .show()
    }

    fun showEditableDialog(context: Context, position: Int){
        val editableTerm = EditText(context)
        val editableDefinition = EditText(context)

        // Assignment Instruction says to change text, but I'm using the hint instead.
        // Makes testing/general use a lot easier.
        // https://www.geeksforgeeks.org/android-edittext-in-kotlin/
        editableTerm.hint = "Enter New Term"
        editableDefinition.hint = "Enter New Defintion"

        AlertDialog.Builder(context)
            .setCustomTitle(editableTerm)
            .setView(editableDefinition)
            .setPositiveButton("Save Changes") {dialogInterface: DialogInterface, i: Int ->
                // Adding guards to make sure card isn't left blank on accident
                if(editableTerm.text.toString() != ""){
                    data[position].term = editableTerm.text.toString()
                }
                if(editableDefinition.text.toString() != ""){
                    data[position].definition = editableDefinition.text.toString()
                }
                notifyItemChanged(position)
            }
            .setNegativeButton("Delete Card") {dialogInterface: DialogInterface, i: Int ->
                data.removeAt(position)
                notifyItemRemoved(position)
            }
            .create()
            .show()
    }

    fun removeAt(index: Int) {
        data.removeAt(index)
        notifyDataSetChanged()
    }
}