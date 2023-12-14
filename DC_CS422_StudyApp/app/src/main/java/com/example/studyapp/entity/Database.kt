package com.example.studyapp.entity

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Title::class, Customized::class], version = 1)
abstract class Database: RoomDatabase() {
    abstract val questionDao: QuestionDao
    abstract val customizedDao: CustomizedDao
}