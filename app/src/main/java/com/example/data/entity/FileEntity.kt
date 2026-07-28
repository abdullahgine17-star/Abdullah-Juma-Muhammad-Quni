package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "project_files")
data class FileEntity(
    @PrimaryKey
    val id: String,
    val projectId: String,
    val path: String,
    val name: String,
    val isDirectory: Boolean,
    val content: String = "",
    val extension: String = "",
    val parentPath: String = "",
    val isModified: Boolean = false,
    val lastModifiedAt: Long = System.currentTimeMillis()
)
