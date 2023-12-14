package cs.mad.week4lab.entities

data class FlashcardSet(val title: String)

fun getFlashcardSets(): List<FlashcardSet> {
    val sets = mutableListOf<FlashcardSet>()
    for (i in 0..9) {
        sets.add(FlashcardSet("Set $i"))
    }
    return sets
}