//
//  StudySetViewController.swift
//
//  Created by Andrew Taylor on 1/29/23.
//

import UIKit
import CoreData

class StudySetViewController: UIViewController  {
    
    @IBOutlet weak var correctCountLabel: UILabel!
    @IBOutlet weak var missedCountLabel: UILabel!
    @IBOutlet weak var completedCountLabel: UILabel!
    @IBOutlet weak var currentLabel: UILabel!
    
    var flashcards: [Flashcard] = []
    var isShowingDefinition = false
    var totalAmount = 0
    var position = 0
    var missed: [Flashcard] = []
    var correct = 0
    var completed = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        loadData()
        
        totalAmount = flashcards.count
        currentLabel.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(currentLabelClicked)))
        updateCurrentCard()
        completedCountLabel.text = "0 / \(totalAmount)"
    }

    @objc func currentLabelClicked() {
        isShowingDefinition = !isShowingDefinition
        updateCurrentCard()
    }
    
    func loadData() {
        let managedContext = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
        
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "Flashcard")
        do {
            let dbItems = try managedContext.fetch(fetchRequest) as! [Flashcard]
            flashcards = dbItems
        } catch {}
    }

    func updateCurrentCard() {
        if (isComplete()) { currentLabel.text = "Set Complete!!!" }
        else if (isShowingDefinition) { currentLabel.text = flashcards[position].definition }
        else { currentLabel.text = flashcards[position].term }
    }
    
    func quitStudying() {
        self.dismiss(animated: true)
    }
    
    func isComplete() -> Bool {
        flashcards.count == 0
    }
    
    @IBAction func skipCurrent(_ sender: Any) {
        if (isComplete()) { return }

        if (position == flashcards.endIndex - 1) { position = 0 }
        else { position += 1 }

        isShowingDefinition = false
        updateCurrentCard()
    }
    
    @IBAction func missCurrent(_ sender: Any) {
        if (isComplete()) { return }

        if (!missed.contains(where: { $0 == flashcards[position] })) {
            missed.append(flashcards[position])
        }
        missedCountLabel.text = "Missed: \(missed.count)"
        skipCurrent(sender)
    }
    
    @IBAction func correctCurrent(_ sender: Any) {
        if (isComplete()) { return }

        completed += 1
        if (!missed.contains(flashcards[position])) { correct += 1 }
        completedCountLabel.text = "\(completed) / \(totalAmount)"
        correctCountLabel.text = "Correct: \(correct)"
        flashcards.remove(at: position)
        if (position == flashcards.endIndex - 1) { position = 0 }

        isShowingDefinition = false
        updateCurrentCard()
    }
}
