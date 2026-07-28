package com.example.data.repository

import com.example.data.dao.ProjectDao
import com.example.data.entity.FileEntity
import com.example.data.entity.ProjectEntity
import com.example.model.DiagnosticItem
import com.example.model.DiagnosticSeverity
import com.example.model.FileNode
import com.example.model.SearchResult
import com.example.util.ProjectTemplates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

class ProjectRepository(private val projectDao: ProjectDao) {

    val allProjects: Flow<List<ProjectEntity>> = projectDao.getAllProjects()

    fun getProjectFiles(projectId: String): Flow<List<FileEntity>> =
        projectDao.getFilesForProject(projectId)

    suspend fun createProject(
        name: String,
        packageName: String,
        templateType: String = "Compose Activity",
        language: String = "Kotlin",
        minSdk: Int = 29
    ): ProjectEntity = withContext(Dispatchers.IO) {
        val projectId = UUID.randomUUID().toString()
        val project = ProjectEntity(
            id = projectId,
            name = name,
            packageName = packageName,
            templateType = templateType,
            rootPath = "Project/",
            language = language,
            minSdk = minSdk
        )
        projectDao.insertProject(project)

        val files = ProjectTemplates.createEmptyComposeProjectFiles(projectId, name, packageName)
        projectDao.insertFiles(files)

        project
    }

    suspend fun createAiGeneratedProject(prompt: String): ProjectEntity = withContext(Dispatchers.IO) {
        val projectId = UUID.randomUUID().toString()
        val (info, files) = ProjectTemplates.createAiGeneratedProjectFiles(projectId, prompt)

        val project = ProjectEntity(
            id = projectId,
            name = info.name,
            packageName = info.packageName,
            templateType = "AI Generated App",
            rootPath = "Project/",
            language = "Kotlin",
            minSdk = 29
        )
        projectDao.insertProject(project)
        projectDao.insertFiles(files)

        project
    }

    suspend fun saveFileContent(projectId: String, path: String, content: String) = withContext(Dispatchers.IO) {
        val existing = projectDao.getFileByPath(projectId, path)
        if (existing != null) {
            projectDao.updateFile(
                existing.copy(
                    content = content,
                    isModified = false,
                    lastModifiedAt = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun createNewFile(
        projectId: String,
        parentPath: String,
        fileName: String,
        isDirectory: Boolean,
        content: String = ""
    ) = withContext(Dispatchers.IO) {
        val path = if (parentPath.endsWith("/")) "$parentPath$fileName" else "$parentPath/$fileName"
        val ext = if (!isDirectory && fileName.contains(".")) fileName.substringAfterLast(".") else ""
        val fileId = UUID.randomUUID().toString()

        val newFile = FileEntity(
            id = fileId,
            projectId = projectId,
            path = path,
            name = fileName,
            isDirectory = isDirectory,
            content = content,
            extension = ext,
            parentPath = parentPath,
            lastModifiedAt = System.currentTimeMillis()
        )
        projectDao.insertFile(newFile)
    }

    suspend fun deleteFile(projectId: String, path: String) = withContext(Dispatchers.IO) {
        projectDao.deleteFile(projectId, path)
    }

    fun buildFileTree(files: List<FileEntity>): FileNode {
        val root = files.find { it.path == "Project/" } ?: files.firstOrNull() ?: return FileNode("root", "Project", "Project/", true)
        
        fun buildNode(file: FileEntity): FileNode {
            val children = files
                .filter { it.parentPath == file.path || (it.parentPath.removeSuffix("/") == file.path.removeSuffix("/")) }
                .map { buildNode(it) }
                .sortedWith(compareByDescending<FileNode> { it.isDirectory }.thenBy { it.name })

            return FileNode(
                id = file.id,
                name = file.name,
                path = file.path,
                isDirectory = file.isDirectory,
                children = children,
                extension = file.extension,
                isGitModified = file.isModified
            )
        }

        return buildNode(root)
    }

    suspend fun searchInFiles(projectId: String, query: String, isCaseSensitive: Boolean = false): List<SearchResult> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()

        val results = mutableListOf<SearchResult>()
        // In a real app we query Room files flow
        // Simplified file search implementation:
        return@withContext results
    }

    fun analyzeDiagnostics(fileName: String, content: String): List<DiagnosticItem> {
        val diagnostics = mutableListOf<DiagnosticItem>()
        val lines = content.lines()

        lines.forEachIndexed { index, line ->
            val lineNum = index + 1
            // Check for unclosed brackets or common syntax mistakes
            if (line.count { it == '{' } != line.count { it == '}' }) {
                diagnostics.add(
                    DiagnosticItem(
                        id = UUID.randomUUID().toString(),
                        filePath = fileName,
                        line = lineNum,
                        column = line.length,
                        message = "Unbalanced curly brackets '{ }' in line $lineNum",
                        severity = DiagnosticSeverity.WARNING,
                        codeSnippet = line.trim()
                    )
                )
            }
            if (fileName.endsWith(".kt") && line.contains("val ") && line.contains("=") && line.endsWith(";")) {
                diagnostics.add(
                    DiagnosticItem(
                        id = UUID.randomUUID().toString(),
                        filePath = fileName,
                        line = lineNum,
                        column = line.indexOf(";"),
                        message = "Redundant semicolon in Kotlin code",
                        severity = DiagnosticSeverity.INFO,
                        codeSnippet = line.trim()
                    )
                )
            }
            if (fileName.endsWith(".xml") && line.contains("<") && !line.contains(">") && !line.endsWith("/>")) {
                diagnostics.add(
                    DiagnosticItem(
                        id = UUID.randomUUID().toString(),
                        filePath = fileName,
                        line = lineNum,
                        column = line.length,
                        message = "Unclosed XML tag",
                        severity = DiagnosticSeverity.ERROR,
                        codeSnippet = line.trim()
                    )
                )
            }
        }
        return diagnostics
    }
}
