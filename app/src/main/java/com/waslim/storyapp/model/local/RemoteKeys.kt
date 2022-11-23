package com.waslim.storyapp.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_key")
data class RemoteKeys(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "previous_key") val previous: Int?,
    @ColumnInfo(name = "next_key") val next: Int?
)