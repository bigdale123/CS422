package com.example.studyapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.studyapp.R
import com.example.studyapp.activity.runOnIO
import com.example.studyapp.entity.Customized
import com.example.studyapp.entity.CustomizedDao
import com.example.studyapp.entity.QuestionDao
import com.example.studyapp.entity.Title

class CustomizedQuestionAdapter(customizedData: List<Customized>, customizedDao: CustomizedDao, questionData: List<Title>, questionDao: QuestionDao): RecyclerView.Adapter<CustomizedQuestionAdapter.ViewHolder>() {
    private val customizedData = customizedData.toMutableList()
    private val customizedDao = customizedDao
    private val questionData = questionData.toMutableList()
    private var questionDao = questionDao

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById<TextView>(R.id.customized_question_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.customized_question, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // spawns a AlertDialog for entering custom questions
        val customized = customizedData[position]
        holder.title.text = customized.title
        holder.itemView.setOnClickListener {
            val alert = AlertDialog.Builder(it.context)
                .setTitle("Customized Question")
                .setMessage(customized.title)
                .setPositiveButton("Edit") { dialog,_ ->
                    showEditDialog(it.context, position)
                    dialog.dismiss()
                }
                .setNeutralButton("Delete") {dialog, _ ->
                    for(i in questionData.indices){
                        if(holder.title.text == questionData[i].title){
                            runOnIO { questionDao.delete(questionData[i]) }
                        }
                    }
                    removeAt(position)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            alert.show()
        }
    }
    override fun getItemCount() = customizedData.size

    /**
     * update the customizedData with the given list
     *
     * @param all: List<Customized>
     */
    fun update(all: List<Customized>) {
        customizedData.clear()
        customizedData.addAll(all)
        notifyDataSetChanged()
    }

    /**
     * add new question to the database and to customizedData
     *
     * @param set: Customized
     */
    fun add(set: Customized) {
        runOnIO { customizedDao.insert(set) }
        customizedData.add(set)
        notifyItemInserted(customizedData.size - 1)
    }

    /**
     * Remove a question from the database and customizedData
     *
     * @param index
     */
    private fun removeAt(index: Int) {
        runOnIO { customizedDao.delete(customizedData[index]) }
        customizedData.removeAt(index)
        notifyDataSetChanged()
    }

    /**
     * Shows the edit dialog for editing questions
     *
     * @param context
     * @param position
     */
    private fun showEditDialog(context: Context, position: Int) {
        val customized = customizedData[position]
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Enter Your Question Information")

        // Dialog Layout
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        // RadioGroup
        val radioGroup = RadioGroup(context)
        radioGroup.orientation = RadioGroup.HORIZONTAL
        radioGroup.gravity = Gravity.CENTER

        // Title
        val editTitle = EditText(context)
        editTitle.hint = "Title"
        editTitle.setText(customized.title)
        layout.addView(editTitle)

        // TextView for spacing
        val text = TextView(context)
        text.textSize = 10.0f
        layout.addView(text)

        // Option A
        val editOptionA = EditText(context)
        editOptionA.hint = "option A"
        editOptionA.setText(customized.optionA)
        val radioButtonA = RadioButton(context)
        radioButtonA.id = View.generateViewId()
        radioButtonA.text = "option A"
        layout.addView(editOptionA)

        // Option B
        val editOptionB = EditText(context)
        editOptionB.hint = "option B"
        editOptionB.setText(customized.optionB)
        val radioButtonB = RadioButton(context)
        radioButtonB.id = View.generateViewId()
        radioButtonB.text = "option B"
        layout.addView(editOptionB)

        // Option C
        val editOptionC = EditText(context)
        editOptionC.hint = "option C"
        editOptionC.setText(customized.optionC)
        val radioButtonC = RadioButton(context)
        radioButtonC.id = View.generateViewId()
        radioButtonC.text = "option C"
        layout.addView(editOptionC)

        // Option D
        val editOptionD = EditText(context)
        editOptionD.hint = "option D"
        editOptionD.setText(customized.optionD)
        val radioButtonD = RadioButton(context)
        radioButtonD.id = View.generateViewId()
        radioButtonD.text = "option D"
        layout.addView(editOptionD)

        radioGroup.addView(radioButtonA)
        radioGroup.addView(radioButtonB)
        radioGroup.addView(radioButtonC)
        radioGroup.addView(radioButtonD)
        when (customized.answer) {
            customized.optionA -> radioGroup.check(radioButtonA.id)
            customized.optionB -> radioGroup.check(radioButtonB.id)
            customized.optionC -> radioGroup.check(radioButtonC.id)
            customized.optionD -> radioGroup.check(radioButtonD.id)
        }

        layout.addView(radioGroup)
        builder.setView(layout)

        builder.setPositiveButton("OK") { dialog, _ ->
            // Check if the question has already exist
            var check = 0
            for(i in 0 until customizedData.size){
                if(editTitle.text.toString() == customizedData[i].title && i != position){
                    showEditDialog(context, position)
                    Toast.makeText(context, "The question has already exist", Toast.LENGTH_SHORT).show()
                    break
                }
                check++
            }
            // Check if the any of filed is blank
            if(check == customizedData.size && (editTitle.text.toString() == "" || editOptionA.text.toString() == "" || editOptionB.text.toString() == "" || editOptionC.text.toString() == "" || editOptionD.text.toString() == "")){
                showEditDialog(context, position)
                Toast.makeText(context, "Fill out all text boxes", Toast.LENGTH_SHORT).show()
                check = -1
            }
            // Pass checking and change the info of the question
            if(check == customizedData.size){
                customized.title = editTitle.text.toString()
                customized.optionA = editOptionA.text.toString()
                customized.optionB = editOptionB.text.toString()
                customized.optionC = editOptionC.text.toString()
                customized.optionD = editOptionD.text.toString()
                when (layout.findViewById<RadioButton>(radioGroup.checkedRadioButtonId).text.toString()) {
                    "option A" -> customized.answer = editOptionA.text.toString()
                    "option B" -> customized.answer = editOptionB.text.toString()
                    "option C" -> customized.answer = editOptionC.text.toString()
                    "option D" -> customized.answer = editOptionD.text.toString()
                }
                runOnIO { customizedDao.update(customizedData[position]) }
                notifyItemChanged(position)
                dialog.dismiss()
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}