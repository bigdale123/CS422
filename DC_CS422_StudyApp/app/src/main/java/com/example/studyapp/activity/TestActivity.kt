package com.example.studyapp.activity

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.studyapp.R
import com.example.studyapp.databinding.ActivityTestBinding
import com.example.studyapp.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class TestActivity : AppCompatActivity(), GestureDetector.OnGestureListener{
    private lateinit var binding: ActivityTestBinding
    private lateinit var questions: ArrayList<Question>
    private lateinit var questionDao: QuestionDao
    private lateinit var customizedData: List<Customized>
    private lateinit var questionTitle: List<Title>
    private var results = ArrayList<Result>()
    private var count = 0
    private val random = Random()
    private var index = 0
    private var correct = 0
    private var choose = ""
    private var lock = false
    private lateinit var gestureDetector: GestureDetector
    var service: AudioService? = null
    private var mediaStatus = false

    private val resultIntent = Intent()
    private val REQUEST_CODE = 100

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
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindService(
            Intent(this, AudioService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )
        mediaStatus = intent.getBooleanExtra("media", false)
        binding.prohibit.alpha = if(mediaStatus) 0.0f else 1.0f

        resultIntent.putExtra("resultMedia", mediaStatus)
        setResult(Activity.RESULT_OK, resultIntent)

        val book = intent.getStringExtra("book")

        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, MainActivity.DATABASE_NAME
        ).build()

        val customizedDao: CustomizedDao = db.customizedDao
        runOnIO { this.customizedData = customizedDao.getAll() }

        when (book) {
            "Video Game Trivia" -> {
                this.questions = getTrivia()
            }
            "CS422 and 522" -> {
                this.questions = getCS422522()
            }
            "Custom Quiz" -> {
                this.questions = getCustomized(customizedData)
            }
            "Bookmarks" -> {

                questionDao = db.questionDao
                runOnIO { questionTitle = questionDao.getAll() }
                val temp1 = getTrivia()
                val temp2 = getCS422522()
                val temp3 = getCustomized(customizedData)
                this.questions = ArrayList<Question>()
                for(i in 0 until temp1.size){
                    for(j in questionTitle.indices){
                        if(temp1[i].title == questionTitle[j].title){
                            this.questions.add(temp1[i])
                        }
                    }
                }
                for(i in 0 until temp2.size){
                    for(j in questionTitle.indices){
                        if(temp2[i].title == questionTitle[j].title){
                            this.questions.add(temp2[i])
                        }
                    }
                }
                for(i in 0 until temp3.size){
                    for(j in questionTitle.indices){
                        if(temp3[i].title == questionTitle[j].title){
                            this.questions.add(temp3[i])
                        }
                    }
                }
            }
        }
        count = questions.size
        binding.testTitle.text = book
        index = random.nextInt(questions.size)
        shuffle(questions[index].options)
        binding.testQuestion.text = questions[index].title
        binding.testOptionA.text = questions[index].options[0]
        binding.testOptionB.text = questions[index].options[1]
        binding.testOptionC.text = questions[index].options[2]
        binding.testOptionD.text = questions[index].options[3]
        binding.completedCount.text = "${count - questions.size} / $count"

        gestureDetector = GestureDetector(this, this)
        binding.root.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    fun quitStudying(view: View){
        finish()
    }

    fun aClick(view: View){
        if(!lock){
            val others = ContextCompat.getColor(this, R.color.button_colors)
            binding.testOptionA.setBackgroundColor(Color.parseColor("#505050"))
            binding.testOptionB.setBackgroundColor(others)
            binding.testOptionC.setBackgroundColor(others)
            binding.testOptionD.setBackgroundColor(others)
            choose = binding.testOptionA.text.toString()
        }
    }

    fun bClick(view: View){
        if(!lock){
            val others = ContextCompat.getColor(this, R.color.button_colors)
            binding.testOptionA.setBackgroundColor(others)
            binding.testOptionB.setBackgroundColor(Color.parseColor("#505050"))
            binding.testOptionC.setBackgroundColor(others)
            binding.testOptionD.setBackgroundColor(others)
            choose = binding.testOptionB.text.toString()
        }
    }

    fun cClick(view: View){
        if(!lock){
            val others = ContextCompat.getColor(this, R.color.button_colors)
            binding.testOptionA.setBackgroundColor(others)
            binding.testOptionB.setBackgroundColor(others)
            binding.testOptionC.setBackgroundColor(Color.parseColor("#505050"))
            binding.testOptionD.setBackgroundColor(others)
            choose = binding.testOptionC.text.toString()
        }
    }

    fun dClick(view: View){
        if(!lock){
            val others = ContextCompat.getColor(this, R.color.button_colors)
            binding.testOptionA.setBackgroundColor(others)
            binding.testOptionB.setBackgroundColor(others)
            binding.testOptionC.setBackgroundColor(others)
            binding.testOptionD.setBackgroundColor(Color.parseColor("#505050"))
            choose = binding.testOptionD.text.toString()
        }
    }

    fun submitClick(view: View){
        if(choose != ""){
            if(!lock){
                lock = true
                // User Answer
                when (choose) {
                    binding.testOptionA.text -> {
                        binding.testOptionA.setBackgroundColor(Color.RED)
                        binding.testOptionA.setTextColor(Color.WHITE)
                    }
                    binding.testOptionB.text -> {
                        binding.testOptionB.setBackgroundColor(Color.RED)
                        binding.testOptionB.setTextColor(Color.WHITE)
                    }
                    binding.testOptionC.text -> {
                        binding.testOptionC.setBackgroundColor(Color.RED)
                        binding.testOptionC.setTextColor(Color.WHITE)
                    }
                    else -> {
                        binding.testOptionD.setBackgroundColor(Color.RED)
                        binding.testOptionD.setTextColor(Color.WHITE)
                    }
                }
                // Correct Answer
                when (questions[index].answer) {
                    binding.testOptionA.text -> {
                        binding.testOptionA.setBackgroundColor(Color.GREEN)
                        binding.testOptionA.setTextColor(Color.WHITE)
                    }
                    binding.testOptionB.text -> {
                        binding.testOptionB.setBackgroundColor(Color.GREEN)
                        binding.testOptionB.setTextColor(Color.WHITE)
                    }
                    binding.testOptionC.text -> {
                        binding.testOptionC.setBackgroundColor(Color.GREEN)
                        binding.testOptionC.setTextColor(Color.WHITE)
                    }
                    else -> {
                        binding.testOptionD.setBackgroundColor(Color.GREEN)
                        binding.testOptionD.setTextColor(Color.WHITE)
                    }
                }
                if(choose == questions[index].answer){
                    correct++
                }
                results.add(Result(questions[index].title, questions[index].options, questions[index].answer, choose))
                questions.removeAt(index)
                binding.completedCount.text = "${count - questions.size} / $count"
                if(questions.size == 0){
                    binding.testSubmit.text = "Result"
                }else{
                    binding.testSubmit.text = "Next"
                }
            }else{
                if(questions.size != 0){
                    lock = false
                    choose = ""

                    index = random.nextInt(questions.size)
                    shuffle(questions[index].options)
                    binding.testQuestion.text = questions[index].title
                    binding.testOptionA.text = questions[index].options[0]
                    binding.testOptionB.text = questions[index].options[1]
                    binding.testOptionC.text = questions[index].options[2]
                    binding.testOptionD.text = questions[index].options[3]

                    val options = ContextCompat.getColor(this, R.color.button_colors)
                    binding.testOptionA.setBackgroundColor(options)
                    binding.testOptionB.setBackgroundColor(options)
                    binding.testOptionC.setBackgroundColor(options)
                    binding.testOptionD.setBackgroundColor(options)
                    binding.testOptionA.setTextColor(Color.BLACK)
                    binding.testOptionB.setTextColor(Color.BLACK)
                    binding.testOptionC.setTextColor(Color.BLACK)
                    binding.testOptionD.setTextColor(Color.BLACK)

                    binding.testSubmit.text = "Submit"
                }else{
                    val intents = Intent(this, GradeActivity::class.java)
                    intents.putExtra("results", results)
                    intents.putExtra("correct", correct)
                    intents.putExtra("book", intent.getStringExtra("book"))
                    intents.putExtra("media", mediaStatus)
                    this.startActivityForResult(intents, REQUEST_CODE)
                }
            }
        }
    }

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

    override fun onLongPress(p0: MotionEvent) {
        if(!lock){
            Toast.makeText(this, "The answer is ${questions[index].answer}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFling(
        p0: MotionEvent,    // Start point
        p1: MotionEvent,    // Finish point
        p2: Float,          // X velocity
        p3: Float           // Y velocity
    ): Boolean {
        if(!lock){
            if(questions.size == 1){
                Toast.makeText(this, "You cannot skip the last question", Toast.LENGTH_SHORT).show()
            }else{
                val swipeThreshold = 100
                if(abs(p1.x-p0.x) > swipeThreshold || abs(p1.y-p0.y) > swipeThreshold && abs(p2) > swipeThreshold || abs(p3) > swipeThreshold){
                    choose = ""

                    var num = 0
                    do{
                        num = random.nextInt(questions.size)
                    }while(index == num)
                    index = num
                    shuffle(questions[index].options)
                    binding.testQuestion.text = questions[index].title
                    binding.testOptionA.text = questions[index].options[0]
                    binding.testOptionB.text = questions[index].options[1]
                    binding.testOptionC.text = questions[index].options[2]
                    binding.testOptionD.text = questions[index].options[3]

                    val options = ContextCompat.getColor(this, R.color.button_colors)
                    binding.testOptionA.setBackgroundColor(options)
                    binding.testOptionB.setBackgroundColor(options)
                    binding.testOptionC.setBackgroundColor(options)
                    binding.testOptionD.setBackgroundColor(options)
                    binding.testOptionA.setTextColor(Color.BLACK)
                    binding.testOptionB.setTextColor(Color.BLACK)
                    binding.testOptionC.setTextColor(Color.BLACK)
                    binding.testOptionD.setTextColor(Color.BLACK)

                    Toast.makeText(this, "You skipped this question", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return true
    }

    // Override GestureDetector interface but do not use it.
    override fun onDown(p0: MotionEvent): Boolean {
        return true
    }
    override fun onShowPress(p0: MotionEvent) {
        return
    }
    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        return true
    }
    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return true
    }
}

fun runOnIO(lambda: suspend () -> Unit) {
    runBlocking {
        launch(Dispatchers.IO) { lambda() }
    }
}

fun shuffle(arr: Array<String>){
    for(i in 0..100){
        val random = Random()
        val a = random.nextInt(arr.size)
        val b = random.nextInt(arr.size)
        arr[a] = arr[b].also { arr[b] = arr[a] }
    }
}