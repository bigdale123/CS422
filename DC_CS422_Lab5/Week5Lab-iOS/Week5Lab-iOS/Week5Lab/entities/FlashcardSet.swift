//
//  FlashcardSet.swift
//
//  Created by Andrew Taylor on 1/22/23.
//

import Foundation

class FlashcardSet {
    var title: String
    
    init(title: String) {
        self.title = title
    }
}

func getFlashcardSets() -> [FlashcardSet]
{
    var sets = [FlashcardSet]()
    for i in 1...10
    {
        sets.append(FlashcardSet(title: "Title \(i)"))
    }
    return sets
}
