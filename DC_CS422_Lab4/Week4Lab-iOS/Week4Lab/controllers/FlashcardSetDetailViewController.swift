//
//  FlashcardSetDetailViewController.swift
//
//  Created by Andrew Taylor on 1/22/23.
//

import UIKit

class FlashcardSetDetailViewController: UIViewController, UITableViewDelegate, UITableViewDataSource  {

    @IBOutlet weak var tableView: UITableView!
    var flashcards = getFlashcards()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.dataSource = self
        tableView.delegate = self
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        //return number of items
        return flashcards.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "FlashcardCell", for: indexPath) as! FlashcardTableCell
        //setup cell display here
        cell.flashcardLabel.text = flashcards[indexPath.row].term
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        // flashcard was clicked
        createAlert(indexPath: indexPath)
    }
    
    // swipe to delete
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            flashcards.remove(at: indexPath.row)
            tableView.deleteRows(at: [indexPath], with: .fade)
        }
    }
    
    func createAlert(indexPath: IndexPath){
        let alert = UIAlertController(title: flashcards[indexPath.row].term, message: flashcards[indexPath.row].definition, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Cancel", style: .default))
        alert.addAction(UIAlertAction(title: "Edit", style: .default, handler: {_ in alert.dismiss(animated: true, completion: {})
            self.createEditableAlert(indexPath: indexPath)
        }))
        self.present(alert, animated: true)
    }
    
    func createEditableAlert(indexPath: IndexPath){
        let flashcard = flashcards[indexPath.row]
        let alert = UIAlertController(title: nil, message: nil, preferredStyle: .alert)
        
        // Using Hints just like in the android version,
        // https://developer.apple.com/documentation/swiftui/textfield
        
        alert.addTextField(configurationHandler: { textField in
            textField.placeholder = flashcard.term
        })
        alert.addTextField(configurationHandler: { textField in
            textField.placeholder = flashcard.definition
        })
        alert.addAction(UIAlertAction(title: "Delete", style: .cancel, handler: {_ in
            self.flashcards.remove(at: indexPath.row)
            self.tableView.deleteRows(at: [indexPath], with: .top)
        }))
        alert.addAction(UIAlertAction(title: "Done", style: .default, handler: {_ in
            guard let term = alert.textFields?[0].text else {
                alert.dismiss(animated: true)
                return
            }
            guard let definition = alert.textFields?[1].text else {
                alert.dismiss(animated: true)
                return
            }
            if(term != ""){
                flashcard.term = term
            }
            if(term != ""){
                flashcard.definition = definition
            }
            self.tableView.reloadData()
            alert.dismiss(animated: true)
        }))
        self.present(alert, animated: true, completion: nil)
    }
    
    @IBAction func addFlashcard(_ sender: Any) {
        flashcards.append(Flashcard(term: "New Term", definition: "New Definition"))
        tableView.reloadData()
        tableView.scrollToRow(at: IndexPath(item:flashcards.count-1, section: 0), at: .bottom, animated: true)
    }
}
