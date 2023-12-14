//
//  Flashcard.swift
//
//  Created by Andrew Taylor on 1/22/23.
//

import Foundation

class Flashcard: Equatable {
    static func == (lhs: Flashcard, rhs: Flashcard) -> Bool {
        lhs.term == rhs.term && lhs.definition == rhs.definition
    }
    
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
