//
//  Week2LabTests.swift
//  Week2LabTests
//
//  Created by Andrew Taylor on 1/15/23.
//

import XCTest
@testable import Week2Lab

final class Week2LabTests: XCTestCase {
    
    func testFlashcard() throws {
        let flashcards = getFlashcards()
        assert(flashcards.count == 10)
        assert(flashcards is Array<Flashcard>)
        
        let mirror = Mirror(reflecting: flashcards[0])
        assert(mirror.children.count == 2)
        assert(
            mirror.children.contains { (label: String?, _: Any) in
                label == "term"
            }
        )
        assert(
            mirror.children.contains { (label: String?, _: Any) in
                label == "definition"
            }
        )
    }

    func testFlashcardSet() throws {
        let flashcardSets = getFlashcardSets()
        assert(flashcardSets.count == 10)
        assert(flashcardSets is Array<FlashcardSet>)
        
        let mirror = Mirror(reflecting: flashcardSets[0])
        assert(mirror.children.count == 1)
        assert(
            mirror.children.contains { (label: String?, _: Any) in
                label == "title"
            }
        )
    }
}
