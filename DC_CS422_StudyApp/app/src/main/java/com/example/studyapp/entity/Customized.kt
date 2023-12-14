package com.example.studyapp.entity

import androidx.room.*

@Entity
data class Customized (
    var title: String,
    var optionA: String,
    var optionB: String,
    var optionC: String,
    var optionD: String,
    var answer: String,
    @PrimaryKey var id: Long? = null
)

@Dao
interface CustomizedDao {
    @Query("select * from customized")
    suspend fun getAll(): List<Customized>

    @Insert
    suspend fun insert(customized: Customized): Long

    @Update
    suspend fun update(customized: Customized)

    @Delete
    suspend fun delete(customized: Customized)
}