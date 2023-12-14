//
//  ViewController.swift
//  CS422L
//
//  Created by Jonathan Sligh on 1/29/21.
//

import UIKit

class MainViewController: UIViewController, UITableViewDelegate, UITableViewDataSource  {
    
    @IBOutlet var textLabel: UILabel!
    @IBOutlet var movieTableView: UITableView!
    var movies = Movie.getHardCodedCollection()
    var selectedIndex: Int = 0
    var clickedCount = 1
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        movieTableView.delegate = self
        movieTableView.dataSource = self
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        movies.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = movieTableView.dequeueReusableCell(withIdentifier: "MovieCell") as! MovieTableViewCell
        cell.movieTitleLabel.text = movies[indexPath.row].title
        cell.selectionStyle = .none
        return cell
    }
    
    //create normal alert
    func createAlert(indexPath: IndexPath)
    {
        let alert = UIAlertController(title: "\(movies[indexPath.row].title)", message: "\(movies[indexPath.row].desc)", preferredStyle: .alert)
        selectedIndex = indexPath.row
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        alert.addAction(UIAlertAction(title: "Edit", style: .default, handler: {_ in
            alert.dismiss(animated: true, completion: {})
            self.createEditableAlert(indexPath: indexPath)
        }))
        self.present(alert, animated: true)
    }
    
    //create editable alert
    func createEditableAlert(indexPath: IndexPath)
    {
        let movie = movies[indexPath.row]
        let alert = UIAlertController(title: nil, message: nil, preferredStyle: .alert)
        alert.addTextField(configurationHandler: { textField in
            textField.text = movie.title
        })
        alert.addTextField(configurationHandler: { textField in
            textField.text = movie.desc
        })
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel))
        alert.addAction(UIAlertAction(title: "Save", style: .default, handler: {_ in
            guard let title = alert.textFields?[0].text else {
                alert.dismiss(animated: true)
                return
            }
            guard let description = alert.textFields?[1].text else {
                alert.dismiss(animated: true)
                return
            }
            movie.title = title
            movie.desc = description
            self.movieTableView.reloadData()
            alert.dismiss(animated: true)
        }))
        self.present(alert, animated: true, completion: nil)
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        createAlert(indexPath: indexPath)
    }
    
    @IBAction func updateText(_ sender: Any) {
        textLabel.text = "Text Updated \(clickedCount) times"
        clickedCount += 1
    }
}

