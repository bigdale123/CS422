package cs.mad.week2lab.entities

fun getFlashcards(): MutableList<Flashcard>{
    return mutableListOf<Flashcard>(
        Flashcard("Term 1", "Definition 1"),
        Flashcard("Term 2", "Definition 2"),
        Flashcard("Term 3", "Definition 3"),
        Flashcard("Term 4", "Definition 4"),
        Flashcard("Term 5", "Definition 5"),
        Flashcard("Term 6", "Definition 6"),
        Flashcard("Term 7", "Definition 7"),
        Flashcard("Term 8", "Definition 8"),
        Flashcard("Term 9", "Definition 9"),
        Flashcard("Term 10", "Definition 10")
    )
}