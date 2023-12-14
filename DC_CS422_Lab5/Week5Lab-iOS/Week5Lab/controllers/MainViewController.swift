//
//  ViewController.swift
//
//  Created by Andrew Taylor on 1/22/23.
//

import UIKit
import CoreData

class MainViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource  {

    @IBOutlet weak var collectionView: UICollectionView!
    var sets: [FlashcardSet] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        collectionView.delegate = self
        collectionView.dataSource = self
        
        loadData()
    }
    

    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        //return number of items
        return sets.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "FlashcardSetCell", for: indexPath) as! FlashcardSetCollectionCell
        //setup cell display here
        cell.myLabel.text = sets[indexPath.row].title
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        //go to new view
        performSegue(withIdentifier: "GoToDetail", sender: self)
    }
    
    func loadData() {
        let managedContext = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
        
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "FlashcardSet")
        do {
            let dbItems = try managedContext.fetch(fetchRequest) as! [FlashcardSet]
            sets = dbItems
        } catch {}
        collectionView.reloadData()
    }
    
    @IBAction func addFlashcardSet(_ sender: Any) {
        let managedContext = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
        let myObject = NSEntityDescription.insertNewObject(forEntityName: "FlashcardSet", into: managedContext) as! FlashcardSet
        myObject.title = "New Set"
        do {
            try managedContext.save()
        } catch {}
        loadData()
        collectionView.reloadData()
        collectionView.scrollToItem(at: IndexPath(item:sets.count-1, section: 0), at: .bottom, animated: true)
    }
}
