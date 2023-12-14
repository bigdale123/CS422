package cs.mad.week2lab.entities


fun getFlashcardSets(): MutableList<FlashcardSet>{
    return mutableListOf<FlashcardSet>(
        FlashcardSet("Set 1"),
        FlashcardSet("Set 2"),
        FlashcardSet("Set 3"),
        FlashcardSet("Set 4"),
        FlashcardSet("Set 5"),
        FlashcardSet("Set 6"),
        FlashcardSet("Set 7"),
        FlashcardSet("Set 8"),
        FlashcardSet("Set 9"),
        FlashcardSet("Set 10")
    )
}