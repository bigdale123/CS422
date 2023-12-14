package com.example.studyapp.entity

import androidx.room.*

data class Question(
    var title: String,
    val options: Array<String> = Array(4){""},
    var answer: String,
)

@Entity
data class Title(
    var title: String,
    @PrimaryKey var id: Long? = null
)

@Dao
interface QuestionDao {
    @Query("select * from title")
    suspend fun getAll(): List<Title>

    @Insert
    suspend fun insert(title: Title): Long

    @Delete
    suspend fun delete(title: Title)
}

fun getTrivia(): ArrayList<Question>{
    var questions = ArrayList<Question>()
    questions.add(Question("What is the release date for The Legend of Zelda\nTears of the Kingdom?", arrayOf("June 15th, 2023", "September 29th, 2023", "May 12th, 2023", "August 8th, 2023"), "May 12th, 2023"))
    questions.add(Question("What is the name of the red ghost in Pac-Man?", arrayOf("Blinky", "Pinky", "Inky", "Clyde"), "Blinky"))
    questions.add(Question("Who was originally supposed to be the protagonist in Donkey Kong (1981)?", arrayOf("Mario", "Popeye", "Donkey Kong", "Princess Peach"), "Popeye"))
    questions.add(Question("In The Legend of Zelda: Ocarina of Time, the combat style was based on what style of sword fighting?", arrayOf("Fencing", "Chanbara", "Medieval", "Kendo"), "Chanbara"))
    questions.add(Question("Where did Tetsuya Nomura first pitch the idea of Kingdom Hearts to Disney?", arrayOf("Animal Kingdom", "An elevator", "Disney World", "McDonalds"), "An elevator"))
    questions.add(Question("Who is the primary composer for Kingdom Hearts?", arrayOf("Junichi Masuda", "John Williams", "Koji Kondo", "Yoko Shimomura"), "Yoko Shimomura"))
    questions.add(Question("What is the true name of the Tetris theme song?", arrayOf("Type A", "Korobeiniki", "Kalinka", "Katioucha"), "Korobeiniki"))
    questions.add(Question("The Pokemon franchise was initially released in Japan as", arrayOf("Digimon", "Bakugan", "Pocket Monsters", "Pokemon"), "Pocket Monsters"))
    questions.add(Question("In The Legend of Zelda, Zelda was named after", arrayOf("Zelda Fitzgerald", "Zelda Williams", "Zelda Nordlinger", "Zelda Zonk"), "Zelda Fitzgerald"))
    questions.add(Question("What was Nintendo’s first product?", arrayOf("Mario", "Playing Cards", "Pong", "Board Games"), "Playing Cards"))
    return questions
}

fun getCS422522(): ArrayList<Question>{
    var questions = ArrayList<Question>()
    questions.add(Question("In Swift, which keyword creates an immutable variable?", arrayOf("var", "val", "let", "const"), "let"))
    questions.add(Question("In Kotlin, which keyword creates an immutable variable?", arrayOf("var", "val", "let", "const"), "val"))
    questions.add(Question("Which languages can be used for Android development?", arrayOf("Swift", "Objective-C", "Kotlin", "Python"), "Kotlin"))
    questions.add(Question("Which languages can be used for iOS development?", arrayOf("Swift", "C / C++", "Kotlin", "Java"), "Swift"))
    questions.add(Question("You can create an emulator to simulate the configuration of a particular type of Android device using a tool like ___.", arrayOf("Theme Editor", "AVD Manager", "Android SDK Manager", "Virtual Editor"), "AVD Manager"))
    questions.add(Question("Which attribute increases the space between a View's content and its border?", arrayOf("Elevation", "Padding", "Visibility", "Margin"), "Padding"))
    questions.add(Question("Which of the following is a potential negative about mobile devices which must be considered?", arrayOf("Low storage", "Limited screen space", "All of the above", "Poor network connection"), "All of the above"))
    questions.add(Question("A database consists of which of the following?", arrayOf("Tables", "Rows", "Columns", "All of the above"), "All of the above"))
    questions.add(Question("In Android, what is used to launch a component?", arrayOf("Intent", "Bundle", "Window", "Backstack"), "Intent"))
    questions.add(Question("Which mobile design paradigm makes extensive use of animations and thus can be computationally expensive?", arrayOf("None of the above", "Skeuomorphic design", "Material design", "Flat design"), "Material design"))
    questions.add(Question("In the following endpoint, everything after the question mark is considered what?\nhttps://api.fake.com/foo?test=something&test2=else", arrayOf("Query parameters", "Path", "JSON", "Base url"), "Query parameters"))
    questions.add(Question("Which of the following can result from improper lifecycle handling?", arrayOf("All of the above", "Loss of user progress", "Crashing", "Memory leaks"), "All of the above"))
    questions.add(Question("Animation ___ is applying acceleration to an animation based on the animation's percent completion.", arrayOf("Scrub", "Delay", "Transition", "Easing"), "Easing"))
    return questions
}

fun getCustomized(customizedData: List<Customized>): ArrayList<Question>{
    var questions = ArrayList<Question>()
    for(i in customizedData.indices){
        questions.add(Question(customizedData[i].title,arrayOf(customizedData[i].optionA, customizedData[i].optionB, customizedData[i].optionC, customizedData[i].optionD),customizedData[i].answer))
    }
    return questions
}