package com.example.a3apis.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritos")
data class FavoritoEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val imageUrl: String?,
    val categoryName: String,
    val tags: String,
    val extraInfo: String?,
    val addedAt: Long = System.currentTimeMillis()
)
