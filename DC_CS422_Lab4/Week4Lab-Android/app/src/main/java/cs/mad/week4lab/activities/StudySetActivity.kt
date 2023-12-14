package cs.mad.week4lab.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cs.mad.week4lab.R
import cs.mad.week4lab.databinding.ActivityStudySetBinding
import cs.mad.week4lab.entities.Flashcard
import cs.mad.week4lab.entities.getFlashcards
import java.lang.reflect.TypeVariable

class StudySetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudySetBinding
    private val flashcards = getFlashcards().toMutableList()
    // Various vars for holding data
    private var position = 0
    private var flag = 0
    private var missed = MutableList(flashcards.size) {0}
    private var correct = 0
    private var completed = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudySetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setting text to first card term
        binding.currentCard.text = flashcards[position].term



        ///////////////////////////
        //   Flashcard Text Button
        ///////////////////////////
        binding.currentCard.setOnClickListener {
            // Added functionality, allows you to switch between term and definition
            if(flashcards.size > 0){
                if(flag == 0){
                    binding.currentCard.text = flashcards[position].definition
                    flag = 1
                }
                else if(flag == 1){
                    binding.currentCard.text = flashcards[position].term
                    flag = 0
                }
            }
            else{
                binding.currentCard.text = "Set Complete!"
            }

        }

        /////////////////////////
        //  Skip Button
        /////////////////////////
        binding.skip.setOnClickListener {
            if(flashcards.size > 0){
                position++
                if(position >= flashcards.size){
                    position = 0
                }
                flag = 0
                binding.currentCard.text = flashcards[position].term
            }
            else{
                binding.currentCard.text = "Set Complete!"
            }

        }

        /////////////////////////
        //  Missed Button
        /////////////////////////
        binding.missed.setOnClickListener {
            // The $ notation was recommended by Android Studio, because of a string concatenation warning
            // I guess it's like bash?
            if(flashcards.size > 0){
                completed++
                binding.completedCount.text = "$completed"
                missed[position] = 1
                position++
                if(position >= flashcards.size){
                    position = 0
                }
                flag = 0
                binding.missedCount.text = "Missed: ${missed.count{it == 1}}"
                binding.currentCard.text = flashcards[position].term
            }
            else{
                binding.currentCard.text = "Set Complete!"
            }

        }

        /////////////////////////
        // Correct Button
        /////////////////////////
        binding.correct.setOnClickListener {
            if(flashcards.size > 0){
                completed++
                binding.completedCount.text = "$completed"
                if(missed[position] != 1){
                    correct++
                }
                flashcards.removeAt(position)
                missed.removeAt(position)
                if((position+1) > flashcards.size){
                    // Fun Fact: If you set the position to 0 and the flashcard array is empty,
                    // The activity crashes and puts you back on the set page (Which is expected behavior)
                    // It's not a bug, it's a feature.
                    position = 0
                }
                flag = 0

                binding.correctCount.text = "Correct: $correct"
                if(flashcards.size > 0){
                    binding.currentCard.text = flashcards[position].term
                }
                else{
                    binding.currentCard.text = "Set Complete!"
                }
            }
            else{
                binding.currentCard.text = "Set Complete!"
            }

        }
    }

    fun quitStudying(view: View) {
        finish()
    }
}