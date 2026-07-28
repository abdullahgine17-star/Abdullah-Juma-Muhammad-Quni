package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.FileEntity
import com.example.data.entity.ProjectEntity
import com.example.data.entity.SettingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY lastOpenedAt DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: String): ProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProject(id: String)

    @Query("SELECT * FROM project_files WHERE projectId = :projectId ORDER BY isDirectory DESC, name ASC")
    fun getFilesForProject(projectId: String): Flow<List<FileEntity>>

    @Query("SELECT * FROM project_files WHERE projectId = :projectId AND path = :path LIMIT 1")
    suspend fun getFileByPath(projectId: String, path: String): FileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(file: FileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFiles(files: List<FileEntity>)

    @Update
    suspend fun updateFile(file: FileEntity)

    @Query("DELETE FROM project_files WHERE projectId = :projectId AND path = :path")
    suspend fun deleteFile(projectId: String, path: String)

    @Query("DELETE FROM project_files WHERE projectId = :projectId")
    suspend fun deleteAllProjectFiles(projectId: String)

    @Query("SELECT * FROM ide_settings WHERE key = :key LIMIT 1")
    suspend fun getSetting(key: String): SettingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSetting(setting: SettingEntity)
}
