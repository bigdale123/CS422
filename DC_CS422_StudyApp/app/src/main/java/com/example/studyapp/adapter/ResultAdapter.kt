package com.example.studyapp.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.studyapp.R
import com.example.studyapp.activity.runOnIO
import com.example.studyapp.entity.QuestionDao
import com.example.studyapp.entity.Result
import com.example.studyapp.entity.Title

class ResultAdapter(dataSet: ArrayList<Result>, questionData: List<Title>, questionDao: QuestionDao): RecyclerView.Adapter<ResultAdapter.ViewHolder>() {
    private val data = dataSet
    private val questionData = questionData.toMutableList()
    private var questionDao = questionDao

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val question: TextView = view.findViewById<TextView>(R.id.result_question)
        val title: TextView = view.findViewById<TextView>(R.id.result_title)
        val star: ImageView = view.findViewById<ImageView>(R.id.result_star)
        val optionA: TextView = view.findViewById<TextView>(R.id.result_option_a)
        val optionB: TextView = view.findViewById<TextView>(R.id.result_option_b)
        val optionC: TextView = view.findViewById<TextView>(R.id.result_option_c)
        val optionD: TextView = view.findViewById<TextView>(R.id.result_option_d)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.grade_result, parent, false)
    )

    override fun getItemCount() = data.size

    /**
     * Holds the logic for displaying results
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.question.text = "Q${position+1}"
        holder.title.text = "${data[position].title}"
        holder.optionA.text = data[position].options[0]
        holder.optionB.text = data[position].options[1]
        holder.optionC.text = data[position].options[2]
        holder.optionD.text = data[position].options[3]
        var count = 0
        for(i in questionData.indices){
            if(holder.title.text == questionData[i].title){
                holder.star.setImageResource(R.drawable.star_fill)
                holder.star.setColorFilter(Color.parseColor("#FFC107"), PorterDuff.Mode.SRC_IN)
                count++
            }
        }
        if(count == 0){
            holder.star.setImageResource(R.drawable.star)
            holder.star.setColorFilter(Color.parseColor("#FFC107"), PorterDuff.Mode.SRC_IN)
        }

        // Initialize the color for all options
        holder.optionA.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.button_colors))
        holder.optionA.setTextColor(Color.BLACK)
        holder.optionB.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.button_colors))
        holder.optionB.setTextColor(Color.BLACK)
        holder.optionC.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.button_colors))
        holder.optionC.setTextColor(Color.BLACK)
        holder.optionD.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.button_colors))
        holder.optionD.setTextColor(Color.BLACK)

        for(i in 0 until data[position].options.size){
            if(data[position].select == data[position].options[i]){
                when (i) {
                    0 -> {
                        holder.optionA.setBackgroundColor(Color.RED)
                        holder.optionA.setTextColor(Color.WHITE)
                    }
                    1 -> {
                        holder.optionB.setBackgroundColor(Color.RED)
                        holder.optionB.setTextColor(Color.WHITE)
                    }
                    2 -> {
                        holder.optionC.setBackgroundColor(Color.RED)
                        holder.optionC.setTextColor(Color.WHITE)
                    }
                    3 -> {
                        holder.optionD.setBackgroundColor(Color.RED)
                        holder.optionD.setTextColor(Color.WHITE)
                    }
                }
            }
            if(data[position].answer == data[position].options[i]){
                when (i) {
                    0 -> {
                        holder.optionA.setBackgroundColor(Color.GREEN)
                        holder.optionA.setTextColor(Color.WHITE)
                    }
                    1 -> {
                        holder.optionB.setBackgroundColor(Color.GREEN)
                        holder.optionB.setTextColor(Color.WHITE)
                    }
                    2 -> {
                        holder.optionC.setBackgroundColor(Color.GREEN)
                        holder.optionC.setTextColor(Color.WHITE)
                    }
                    3 -> {
                        holder.optionD.setBackgroundColor(Color.GREEN)
                        holder.optionD.setTextColor(Color.WHITE)
                    }
                }
            }
        }

        holder.star.setOnClickListener(){
            var count = 0
            for(i in questionData.indices){
                if(holder.title.text == questionData[i].title){
                    runOnIO { questionDao.delete(questionData[i]) }
                    questionData[i].title = ""
                    holder.star.setImageResource(R.drawable.star)
                    holder.star.setColorFilter(Color.parseColor("#FFC107"), PorterDuff.Mode.SRC_IN)
                    count++
                }
            }
            if(count == 0){
                var question = Title(data[position].title.toString())
                runOnIO { question.id = questionDao.insert(question) }
                questionData.add(question)
                holder.star.setImageResource(R.drawable.star_fill)
                holder.star.setColorFilter(Color.parseColor("#FFC107"), PorterDuff.Mode.SRC_IN)
            }
        }
    }
}