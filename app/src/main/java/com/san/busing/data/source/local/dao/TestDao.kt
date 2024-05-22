package com.san.busing.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.san.busing.data.entity.Test

@Dao
interface TestDao {
    @Query("SELECT * FROM test")
    fun getAll(): List<Test>

    @Insert
    fun insert(test: Test)
}