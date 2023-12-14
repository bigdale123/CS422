package com.example.studyapp.activity

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Toast
import androidx.room.Room
import com.example.studyapp.databinding.ActivityOptionBinding
import com.example.studyapp.entity.AudioService
import com.example.studyapp.entity.Customized
import com.example.studyapp.entity.CustomizedDao
import com.example.studyapp.entity.Database

class OptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOptionBinding
    private lateinit var customizedData: List<Customized>
    private lateinit var customizedDao: CustomizedDao
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
        binding = ActivityOptionBinding.inflate(layoutInflater)
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
    }

    /**
     * logic for the questionButton button.
     *
     * @param view
     */
    fun questionButton(view: View){
        val intent = Intent(this, CustomizedQuestionActivity::class.java)
        intent.putExtra("media", mediaStatus)
        this.startActivityForResult(intent, REQUEST_CODE)
    }

    /**
     * logic for the examButton button.
     *
     * @param view
     */
    fun examButton(view: View){
        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, MainActivity.DATABASE_NAME
        ).build()
        customizedDao = db.customizedDao
        runOnIO { this.customizedData = customizedDao.getAll() }
        if(customizedData.isEmpty()){
            Toast.makeText(this, "No question has been made yet!!", Toast.LENGTH_SHORT).show()
        }else{
            val intent = Intent(this, TestActivity::class.java)
            intent.putExtra("book", "Custom Quiz")
            intent.putExtra("media", mediaStatus)
            this.startActivityForResult(intent, REQUEST_CODE)
        }
    }

    /**
     * logic for the quitStudying button.
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