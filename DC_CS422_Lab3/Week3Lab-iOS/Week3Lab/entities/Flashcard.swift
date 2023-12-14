//
//  Flashcard.swift
//  Week3Lab
//
//  Created by Andrew Taylor on 1/22/23.
//

import Foundation

class Flashcard {
    var term: String
    var definition: String
    
    init(term: String, definition: String) {
        self.term = term
        self.definition = definition
    }
}

func getFlashcards() -> [Flashcard]
{
    var flashcards = [Flashcard]()
    for i in 1...10
    {
        flashcards.append(Flashcard(term: "Term \(i)", definition: "Definition \(i)"))
    }
    return flashcards
}
