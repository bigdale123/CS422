//
//  Flashcard.swift
//  CS422L
//
//  Created by Jonathan Sligh on 2/3/21.
//

import Foundation

class Movie {
    var title: String = ""
    var desc: String = ""
    
    init(movie: String, desc: String) {
        self.title = movie
        self.desc = desc
    }
    
    static func getHardCodedCollection() -> [Movie]
    {
        var movies = [Movie]()
        movies.append(Movie(movie: "Mortal", desc: "Nat Wolff is basically a superhero"))
        movies.append(Movie(movie: "The Clovehitch Killer", desc: "Dylan Mcdermott is a serial killer."))
        movies.append(Movie(movie: "Antebellum", desc: "Movie of the year.  Just watch it."))
        movies.append(Movie(movie: "Possessor", desc: "Taking over other peoples' brains."))
        movies.append(Movie(movie: "Cadaver", desc: "If you combined The Green Inferno and Snowpiercer."))
        movies.append(Movie(movie: "Upgrade", desc: "Sleeper movie from 2018."))
        movies.append(Movie(movie: "Soul", desc: "The FEELS"))
        movies.append(Movie(movie: "Unhinged", desc: "Roadrage from Christian Bale"))
        movies.append(Movie(movie: "Promising Young Woman", desc: "Ridiculously Good"))
        movies.append(Movie(movie: "Hamilton", desc: "My name is Alexander Hamilton."))
        return movies
    }
}
