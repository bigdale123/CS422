//
//  FlashcardSetDetailViewController.swift
//
//  Created by Andrew Taylor on 1/22/23.
//

import UIKit
import Alamofire

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
        cell.flashcardLabel.text = flashcards[indexPath.row].term
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let flashcard = flashcards[indexPath.row]
        let alert = UIAlertController(title: flashcard.term, message: flashcard.definition, preferredStyle: .alert)
        
        alert.addAction(UIAlertAction(title: "Edit", style: .default, handler: { _ in
            self.showEditAlert(indexPath: indexPath)
        }))
        alert.addAction(UIAlertAction(title: "Done", style: .cancel))
        
        present(alert, animated: true)
    }
    
    // swipe to delete
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            flashcards.remove(at: indexPath.row)
            tableView.deleteRows(at: [indexPath], with: .fade)
        }
    }
    
    @IBAction func addFlashcard(_ sender: Any) {
        flashcards.append(Flashcard(term: "New Term", definition: "New Definition"))
        tableView.reloadData()
        tableView.scrollToRow(at: IndexPath(item: flashcards.count-1, section: 0), at: .bottom, animated: true)
    }
    
    func showEditAlert(indexPath: IndexPath) {
        let flashcard = flashcards[indexPath.row]
        let editAlert = UIAlertController(title: nil, message: nil, preferredStyle: .alert)
        
        editAlert.addTextField { textField in
            textField.text = flashcard.term
        }
        editAlert.addTextField { textField in
            textField.text = flashcard.definition
        }
        editAlert.addAction(UIAlertAction(title: "Cancel", style: .cancel))
        editAlert.addAction(UIAlertAction(title: "Delete", style: .destructive, handler: { _ in
            self.flashcards.remove(at: indexPath.row)
            self.tableView.reloadData()
        }))
        editAlert.addAction(UIAlertAction(title: "Done", style: .default, handler: { _ in
            flashcard.term = editAlert.textFields?[0].text ?? ""
            flashcard.definition = editAlert.textFields?[1].text ?? ""
            self.tableView.reloadRows(at: [indexPath], with: .automatic)
        }))
        
        self.present(editAlert, animated: true)
    }
}
