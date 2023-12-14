package com.example.studyapp.activity

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.room.Room
import com.example.studyapp.databinding.ActivityBooksBinding
import com.example.studyapp.entity.AudioService
import com.example.studyapp.entity.Database
import com.example.studyapp.entity.QuestionDao
import com.example.studyapp.entity.Title

class BooksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBooksBinding
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
        // ran on BooksActivity being created
        super.onCreate(savedInstanceState)
        binding = ActivityBooksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // connecting to the audio service Conncetion
        bindService(
            Intent(this, AudioService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )
        // logic for whether music plays or not (based on previous value)
        mediaStatus = intent.getBooleanExtra("media", false)
        binding.prohibit.alpha = if(mediaStatus) 0.0f else 1.0f

        // store current mediaStatus to pass back to parent activity
        resultIntent.putExtra("resultMedia", mediaStatus)
        setResult(Activity.RESULT_OK, resultIntent)
    }

    /**
     * Button Logic for spawning the Video Game Trivia Quiz
     *
     * @param view
     */
    fun triviaButton(view: View){
        val intent = Intent(this, TestActivity::class.java)
        intent.putExtra("book", "Video Game Trivia")
        intent.putExtra("media", mediaStatus)
        this.startActivityForResult(intent, REQUEST_CODE)
    }

    /**
     * Button Logic for spawning the CS422 & CS522 Quiz
     *
     * @param view
     */
    fun cs422522Button(view: View){
        val intent = Intent(this, TestActivity::class.java)
        intent.putExtra("book", "CS422 and 522")
        intent.putExtra("media", mediaStatus)
        this.startActivityForResult(intent, REQUEST_CODE)
    }

    /**
     * Button Logic for spawning the Custom Quiz
     *
     * @param view
     */
    fun customizedButton(view: View){
        val intent = Intent(this, OptionActivity::class.java)
        intent.putExtra("media", mediaStatus)
        this.startActivityForResult(intent, REQUEST_CODE)
    }

    /**
     * Button Logic for spawning the Bookmarks Section
     *
     * @param view
     */
    fun bookmarksButton(view: View){
        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, MainActivity.DATABASE_NAME
        ).build()
        questionDao = db.questionDao

        runOnIO { this.questionsData = questionDao.getAll() }

        if(questionsData.isEmpty()){
            // Spawn "No Questions" activity if no bookmarks
            val intent = Intent(this, NoQuestionActivity::class.java)
            intent.putExtra("media", mediaStatus)
            this.startActivityForResult(intent, REQUEST_CODE)
        }else{
            val intent = Intent(this, TestActivity::class.java)
            intent.putExtra("book", "Bookmarks")
            intent.putExtra("media", mediaStatus)
            this.startActivityForResult(intent, REQUEST_CODE)
        }
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
            intent.putExtra("media", true)
            service?.start()
        }else{
            mediaStatus = false
            binding.prohibit.alpha = 1.0f
            intent.putExtra("media", false)
            service?.pause()
        }
        resultIntent.putExtra("resultMedia", mediaStatus)
        setResult(Activity.RESULT_OK, resultIntent)
    }
}
