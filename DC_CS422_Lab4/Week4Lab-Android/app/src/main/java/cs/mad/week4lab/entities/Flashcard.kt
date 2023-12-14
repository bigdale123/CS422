package cs.mad.week4lab.entities

data class Flashcard(var term: String, var definition: String)

fun getFlashcards(): List<Flashcard> {
    val flashcards = mutableListOf<Flashcard>()
    for (i in 0..9) {
        flashcards.add(Flashcard("Term $i", "Definition $i"))
    }
    return flashcards
}