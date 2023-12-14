package com.example.studyapp.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.room.Room
import com.example.studyapp.adapter.CustomizedQuestionAdapter
import com.example.studyapp.databinding.ActivityCustomizedQuestionBinding
import com.example.studyapp.entity.*

class CustomizedQuestionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomizedQuestionBinding
    private lateinit var customizedData: List<Customized>
    private lateinit var customizedDao: CustomizedDao
    private lateinit var questionsData: List<Title>
    private lateinit var questionDao: QuestionDao
    var service: AudioService? = null
    private var mediaStatus = false

    private val resultIntent = Intent()
    private val REQUEST_CODE = 100

    // creates connection to the audio service so music can play
    private val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            service = (p1 as AudioService.AudioBinder).getService()
        }
        override fun onServiceDisconnected(p0: ComponentName?) {
            service = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Ran on CustomizedQuestionActivity being created
        super.onCreate(savedInstanceState)
        binding = ActivityCustomizedQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // connecting to the audio service
        bindService(
            Intent(this, AudioService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )
        mediaStatus = intent.getBooleanExtra("media", false)
        binding.prohibit.alpha = if(mediaStatus) 0.0f else 1.0f

        // prepare the resultIntent for when the CustomizedQuestionActivity is done
        resultIntent.putExtra("resultMedia", mediaStatus)
        setResult(Activity.RESULT_OK, resultIntent)

        // Create database
        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, MainActivity.DATABASE_NAME
        ).build()
        customizedDao = db.customizedDao
        questionDao = db.questionDao

        // Gather the custom questions from the DB
        runOnIO { this.customizedData = customizedDao.getAll() }
        runOnIO { this.questionsData = questionDao.getAll() }
        binding.customizedQuestionRecycler.adapter = CustomizedQuestionAdapter(customizedData, customizedDao, questionsData, questionDao)
        runOnIO { (binding.customizedQuestionRecycler.adapter as CustomizedQuestionAdapter).update(customizedDao.getAll()) }
    }

    /**
     * Logic for the "Add Question" button
     *
     * @param view
     */
    fun addQuestion(view: View){
        val adapter = binding.customizedQuestionRecycler.adapter as CustomizedQuestionAdapter

        // ~~~

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Your Question Information")

        // Dialog Layout
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        // RadioGroup
        val radioGroup = RadioGroup(this)
        radioGroup.orientation = RadioGroup.HORIZONTAL
        radioGroup.gravity = Gravity.CENTER

        // Title
        val editTitle = EditText(this)
        editTitle.hint = "Title"
        layout.addView(editTitle)

        // TextView for spacing
        val text = TextView(this)
        text.textSize = 10.0f
        layout.addView(text)

        // Option A
        val editOptionA = EditText(this)
        editOptionA.hint = "option A"
        val radioButtonA = RadioButton(this)
        radioButtonA.id = View.generateViewId()
        radioButtonA.text = "option A"
        layout.addView(editOptionA)

        // Option B
        val editOptionB = EditText(this)
        editOptionB.hint = "option B"
        val radioButtonB = RadioButton(this)
        radioButtonB.id = View.generateViewId()
        radioButtonB.text = "option B"
        layout.addView(editOptionB)

        // Option C
        val editOptionC = EditText(this)
        editOptionC.hint = "option C"
        val radioButtonC = RadioButton(this)
        radioButtonC.id = View.generateViewId()
        radioButtonC.text = "option C"
        layout.addView(editOptionC)

        // Option D
        val editOptionD = EditText(this)
        editOptionD.hint = "option D"
        val radioButtonD = RadioButton(this)
        radioButtonD.id = View.generateViewId()
        radioButtonD.text = "option D"
        layout.addView(editOptionD)

        radioGroup.addView(radioButtonA)
        radioGroup.addView(radioButtonB)
        radioGroup.addView(radioButtonC)
        radioGroup.addView(radioButtonD)

        // Set option A as the default answer
        radioGroup.check(radioButtonA.id)

        layout.addView(radioGroup)
        builder.setView(layout)

        builder.setPositiveButton("OK") { _, _ ->
            // Check if the question has already exist
            var check = 0
            for(element in customizedData){
                if(editTitle.text.toString() == element.title){
                    Toast.makeText(this, "The question already exists", Toast.LENGTH_SHORT).show()
                    break
                }
                check++
            }
            // Check if the any of filed is blank
            if(check == customizedData.size && (editTitle.text.toString() == "" || editOptionA.text.toString() == "" || editOptionB.text.toString() == "" || editOptionC.text.toString() == "" || editOptionD.text.toString() == "")){
                Toast.makeText(this, "Incomplete text entry", Toast.LENGTH_SHORT).show()
                check = -1
            }
            // Pass checking and change the info of the question
            if(check == customizedData.size) {
                var answer = ""
                when (layout.findViewById<RadioButton>(radioGroup.checkedRadioButtonId).text.toString()) {
                    "option A" -> answer = editOptionA.text.toString()
                    "option B" -> answer = editOptionB.text.toString()
                    "option C" -> answer = editOptionC.text.toString()
                    "option D" -> answer = editOptionD.text.toString()
                }

                var customizedQuestionItem = Customized(
                    editTitle.text.toString(),
                    editOptionA.text.toString(),
                    editOptionB.text.toString(),
                    editOptionC.text.toString(),
                    editOptionD.text.toString(),
                    answer
                )
                adapter.add(customizedQuestionItem)
                binding.customizedQuestionRecycler.smoothScrollToPosition(adapter.itemCount)

                runOnIO {
                    (binding.customizedQuestionRecycler.adapter as CustomizedQuestionAdapter).update(
                        customizedDao.getAll()
                    )
                }
                runOnIO { this.customizedData = customizedDao.getAll() }
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()

        // ~~~

    }

    /**
     * Logic for quitStudying button
     *
     * @param view
     */
    fun quitStudying(view: View){
        finish()
    }

    /**
     * Gets the result data from the intent upon finishing
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Get the result data from the intent
            mediaStatus = data?.getBooleanExtra("resultMedia", false) == true
            if(mediaStatus){
                binding.prohibit.alpha = 0.0f
            }else{
                binding.prohibit.alpha = 1.0f
            }
        }
    }

    /**
     * Responsible for toggling music play based on mediaStatus
     *
     * @param view
     */
    fun mediaPlay(view: View){
        if(!mediaStatus){
            mediaStatus = true
            binding.prohibit.alpha = 0.0f
            service?.start()
        }else{
            mediaStatus = false
            binding.prohibit.alpha = 1.0f
            service?.pause()
        }
        resultIntent.putExtra("resultMedia", mediaStatus)
        setResult(Activity.RESULT_OK, resultIntent)
    }
}