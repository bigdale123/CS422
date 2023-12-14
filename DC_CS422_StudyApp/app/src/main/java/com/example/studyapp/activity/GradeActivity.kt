package com.example.studyapp.activity

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.room.Room
import com.example.studyapp.adapter.ResultAdapter
import com.example.studyapp.databinding.ActivityGradeBinding
import com.example.studyapp.entity.*

class GradeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGradeBinding
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
        super.onCreate(savedInstanceState)
        binding = ActivityGradeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // connecting to the audio service
        bindService(
            Intent(this, AudioService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )
        mediaStatus = intent.getBooleanExtra("media", false)
        binding.prohibit.alpha = if(mediaStatus) 0.0f else 1.0f

        // setting resultIntent as the result of the activity (allows for sending data back after activity finishes)
        resultIntent.putExtra("resultMedia", mediaStatus)
        setResult(Activity.RESULT_OK, resultIntent)

        // Populating the activity elements
        binding.gradeTitle.text = intent.getStringExtra("book")
        val results = intent.getParcelableArrayListExtra<Result>("results")
        var score = intent.getIntExtra("correct", 0) * 100 / results?.size!!
        binding.gradeNumber.text = "$score%"
        if(score > 80){
            binding.gradeNumber.setTextColor(Color.GREEN)
        }else if(score in 51..80){
            binding.gradeNumber.setTextColor(Color.parseColor("#FFC107"))
        }else{
            binding.gradeNumber.setTextColor(Color.RED)
        }

        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, MainActivity.DATABASE_NAME
        ).build()
        questionDao = db.questionDao
        runOnIO { this.questionsData = questionDao.getAll() }
        binding.gradeRecycler.adapter = results?.let { ResultAdapter(it, this.questionsData, questionDao) }
    }

    /**
     * backHome is a "nicer" finish() for the activity, that passes back the media data and select the
     * correct activity to switch to
     *
     * @param view
     */
    fun backHome(view: View){
        val intent = Intent(this, BooksActivity::class.java)
        intent.putExtra("media", mediaStatus)
        this.startActivityForResult(intent, REQUEST_CODE)
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