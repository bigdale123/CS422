//
//  ViewController.swift
//  Week3Lab
//
//  Created by Andrew Taylor on 1/22/23.
//

import UIKit

class MainViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource  {

    var sets: [FlashcardSet] = getFlashcardSets()
    

    
    @IBOutlet weak var collection_view: UICollectionView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // use the below to set the dataSource and delegate
        
        collection_view.dataSource = self
        collection_view.delegate = self
    }

    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        //return number of items
        return sets.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "FlashcardSetCell", for: indexPath) as! FlashcardSetCollectionCell
        
        //setup cell display here -- use indexPath.row to get position
        cell.set_cell_label.text = sets[indexPath.row].title
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        //go to new view
        performSegue(withIdentifier: "GoToDetail", sender: self)
    }
    
    @IBAction func set_add_button(_ sender: Any) {
        sets.append(FlashcardSet(title: "New Set"))
        collection_view.reloadData()
    }
}
