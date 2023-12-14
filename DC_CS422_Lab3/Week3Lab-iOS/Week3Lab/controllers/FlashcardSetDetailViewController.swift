//
//  FlashcardSetDetailViewController.swift
//  Week3Lab
//
//  Created by Andrew Taylor on 1/22/23.
//

import UIKit

class FlashcardSetDetailViewController: UIViewController, UITableViewDelegate, UITableViewDataSource  {

    var flashcards = getFlashcards()
    
    
    
    @IBOutlet weak var table_view: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // use the below to set the dataSource and delegate
        
        table_view.dataSource = self
        table_view.delegate = self
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        //return number of items
        return flashcards.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "FlashcardCell", for: indexPath) as! FlashcardTableCell
        
        //setup cell display here -- use indexPath.row to get position
        cell.card_cell_label.text = flashcards[indexPath.row].term
        return cell
    }
    
    @IBAction func card_add_button(_ sender: Any) {
        flashcards.append(Flashcard(term: "New Term", definition: "New Definition"))
        table_view.reloadData()
    }
    
    
}
