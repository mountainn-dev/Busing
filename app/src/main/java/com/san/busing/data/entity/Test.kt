package com.san.busing.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Test(
    @PrimaryKey val name: String
)
