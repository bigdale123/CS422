//
//  ViewController.swift
//  Lecture 4 Example
//
//  Created by Andrew Taylor on 2/3/23.
//

import UIKit
import CoreData
import Alamofire
import AlamofireImage

class ViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var tableView: UITableView!
    var pokemon: [Pokemon] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.dataSource = self
        tableView.delegate = self
        
        //clearStorage()
        loadData()
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath) as! PokemonTableViewCell
        
        cell.nameLabel.text = pokemon[indexPath.row].name
        cell.linkLabel.text = pokemon[indexPath.row].url
        
        let spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" +
        "\(pokemon[indexPath.row].url?.dropLast(1).description.substringAfterLastOccurenceOf("/") ?? "").png"
        AF.request(spriteUrl).responseImage(completionHandler: { response in
                    if case .success(let image) = response.result {
                        cell.spriteImageView.image = image
                }
        })

        return cell
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        pokemon.count
    }
    
    func clearStorage() {
        let managedContext = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
        pokemon.forEach { item in
            managedContext.delete(item)
        }
        do { try managedContext.save() } catch {}
    }
    
    func loadData() {
        let defaults = UserDefaults.standard
        let managedContext = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
        
        if defaults.bool(forKey: "isDownloaded") {
            let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "Pokemon")
            do {
                let dbItems = try managedContext.fetch(fetchRequest) as! [Pokemon]
                pokemon.append(contentsOf: dbItems)
                tableView.reloadData()
            } catch {}
        } else {
            AF.request("https://pokeapi.co/api/v2/pokemon").responseDecodable(of: ApiPokemonContainer.self, completionHandler: { response in
                do {
                    try response.value?.results.forEach { item in
                        let myObject = NSEntityDescription.insertNewObject(forEntityName: "Pokemon", into: managedContext) as! Pokemon
                        myObject.name = item.name
                        myObject.url = item.url
                        try managedContext.save()
                        
                        self.pokemon.append(myObject)
                    }
                    self.tableView.reloadData()
                    defaults.set(true, forKey: "isDownloaded")
                } catch {}
            })
        }
    }
}

extension String {
    
    var nsRange: NSRange {
        return Foundation.NSRange(startIndex ..< endIndex, in: self)
    }
    
    subscript(nsRange: NSRange) -> Substring? {
        return Range(nsRange, in: self)
            .flatMap { self[$0] }
    }
    
    func substringAfterLastOccurenceOf(_ char: Character) -> String {
        
        let regex = try! NSRegularExpression(pattern: "\(char)\\s*(\\S[^\(char)]*)$")
        if let match = regex.firstMatch(in: self, range: self.nsRange), let result = self[match.range(at: 1)] {
            return String(result)
        }
        return ""
    }
}

