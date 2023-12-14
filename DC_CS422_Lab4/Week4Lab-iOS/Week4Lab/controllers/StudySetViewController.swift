//
//  StudySetViewController.swift
//  Week4Lab
//
//  Created by Andrew Taylor on 1/29/23.
//

import UIKit

class StudySetViewController: UIViewController  {
    
    
    var flashcards = getFlashcards()
    var position = 0
    var flag = 0
    var total_cards = 0
    var missed = [Int]()
    var correct = 0
    var completed = 0
    
    @IBOutlet weak var text_button: UIButton!
    @IBOutlet weak var completedCount: UILabel!
    
    @IBOutlet weak var missedCount: UILabel!
    @IBOutlet weak var correctCount: UILabel!
    
    // I know it's not the original label, but this is a little easier
    // Than tying a gesture recognizer to the label.
    @IBAction func on_study_click(_ sender: Any) {
        if(flashcards.count > 0){
            if(flag == 0){
                text_button.setTitle(flashcards[0].definition, for: .normal)
                flag = 1
            }
            else if(flag == 1){
                text_button.setTitle(flashcards[0].term, for: .normal)
                flag = 0
            }
        }
        else{
            text_button.setTitle("Set Complete!", for: .normal)
        }
        
    }
    @IBAction func on_click_skipped(_ sender: Any) {
        if(flashcards.count > 0){
            position += 1
            if(position >= flashcards.count){
                position = 0
            }
            flag = 0
            text_button.setTitle(flashcards[position].term, for: .normal)
        }
        else{
            text_button.setTitle("Set Complete!", for: .normal)
        }
    }
    
    @IBAction func on_click_missed(_ sender: Any) {
        if(flashcards.count > 0){
            completed += 1
            completedCount.text = String(completed)+"/"+String(total_cards)
            if(missed.count > 0){
                missed[position] = 1
            }

            position += 1
            if(position >= flashcards.count){
                position = 0
            }
            flag = 0
            missedCount.text = "Missed: "+String(missed.filter{$0 == 1}.count)
            text_button.setTitle(flashcards[position].term, for: .normal)
        }
        else{
            text_button.setTitle("Set Complete!", for: .normal)
        }
    }
    
    @IBAction func on_click_correct(_ sender: Any) {
        if(flashcards.count > 0){
            completed += 1
            completedCount.text = String(completed)+"/"+String(total_cards)
            if(missed[position] != 1){
                correct += 1
            }
            flashcards.remove(at: position)
            missed.remove(at: position)
            if((position+1) > flashcards.count){
                position = 0
            }
            flag = 0
            correctCount.text = "Correct: "+String(correct)
            if(flashcards.count > 0){
                text_button.setTitle(flashcards[position].term, for: .normal)
            }
            else{
                text_button.setTitle("Set Complete!", for: .normal)
            }
        }
        else{
            text_button.setTitle("Set Complete!", for: .normal)
        }
    }
    
    
    // TODO: In both android and iOS, make sure that an empty study set
    //   Does not cause the app to crash, diplay's set complete instead.
    
    override func viewDidLoad() {
        super.viewDidLoad()
        text_button.setTitle(flashcards[0].term, for: .normal)
        missed = Array<Int>(repeating: 0, count: flashcards.count)
        total_cards = flashcards.count
    }
    
    func quitStudying() {
        self.dismiss(animated: true)
    }
}
