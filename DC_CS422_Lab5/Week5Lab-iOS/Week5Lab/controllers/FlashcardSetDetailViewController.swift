//
//  FlashcardSetDetailViewController.swift
//
//  Created by Andrew Taylor on 1/22/23.
//

import UIKit
import CoreData

class FlashcardSetDetailViewController: UIViewController, UITableViewDelegate, UITableViewDataSource  {

    @IBOutlet weak var tableView: UITableView!
    var flashcards: [Flashcard] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.dataSource = self
        tableView.delegate = self
        
        loadData()
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
    
    @IBAction func onDeleteClick(_ sender: Any) {
        navigationController?.popViewController(animated: true)
    }
    // swipe to delete
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            flashcards.remove(at: indexPath.row)
            tableView.deleteRows(at: [indexPath], with: .fade)
        }
    }
    
    func loadData() {
        let managedContext = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
        
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "Flashcard")
        do {
            let dbItems = try managedContext.fetch(fetchRequest) as! [Flashcard]
            flashcards = dbItems
        } catch {}
        tableView.reloadData()
    }
    
    @IBAction func addFlashcard(_ sender: Any) {
        let managedContext = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
        let myObject = NSEntityDescription.insertNewObject(forEntityName: "Flashcard", into: managedContext) as! Flashcard
        myObject.term = "New Term"
        myObject.definition = "New Definition"
        do {
            try managedContext.save()
        } catch {}
        loadData()
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
            let managedContext = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
            managedContext.delete(self.flashcards[indexPath.row])
            do {
                try managedContext.save()
            } catch {}
            self.flashcards.remove(at: indexPath.row)
            self.tableView.reloadData()
        }))
        editAlert.addAction(UIAlertAction(title: "Done", style: .default, handler: { _ in
            flashcard.term = editAlert.textFields?[0].text ?? ""
            flashcard.definition = editAlert.textFields?[1].text ?? ""
            let managedContext = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
            do {
                try managedContext.save()
            } catch {}
            self.loadData()
            self.tableView.reloadRows(at: [indexPath], with: .automatic)
        }))
        
        self.present(editAlert, animated: true)
    }
}
