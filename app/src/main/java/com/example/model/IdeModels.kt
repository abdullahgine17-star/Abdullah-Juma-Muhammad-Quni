package com.example.model

enum class LanguageType {
    KOTLIN, JAVA, XML, GRADLE, JSON, MARKDOWN, TEXT
}

data class EditorTab(
    val fileId: String,
    val filePath: String,
    val fileName: String,
    var content: String,
    val language: LanguageType,
    var isModified: Boolean = false,
    var cursorPosition: Int = 0,
    var lineCount: Int = 1,
    var selectionStart: Int = 0,
    var selectionEnd: Int = 0
)

data class FileNode(
    val id: String,
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val children: List<FileNode> = emptyList(),
    val isExpanded: Boolean = false,
    val extension: String = "",
    val isGitModified: Boolean = false
)

enum class DiagnosticSeverity {
    ERROR, WARNING, INFO
}

data class DiagnosticItem(
    val id: String,
    val filePath: String,
    val line: Int,
    val column: Int,
    val message: String,
    val severity: DiagnosticSeverity,
    val codeSnippet: String = ""
)

enum class LogLevel {
    VERBOSE, DEBUG, INFO, WARNING, ERROR
}

data class LogcatEntry(
    val id: Long = System.currentTimeMillis(),
    val timestamp: String,
    val pid: Int = 10452,
    val tag: String,
    val level: LogLevel,
    val message: String
)

enum class BuildStatus {
    IDLE, BUILDING, SUCCESS, FAILED
}

data class BuildStep(
    val name: String,
    val isCompleted: Boolean = false,
    val isCurrent: Boolean = false,
    val error: String? = null
)

data class ApkBuildResult(
    val apkName: String,
    val apkPath: String,
    val sizeMb: Double,
    val buildTimeSeconds: Double,
    val isSigned: Boolean
)

data class AdbDevice(
    val name: String,
    val ipAddress: String,
    val port: Int,
    val isConnected: Boolean,
    val model: String,
    val androidVersion: String,
    val batteryPercent: Int
)

data class SdkComponent(
    val name: String,
    val version: String,
    val sizeMb: Int,
    val isInstalled: Boolean,
    val isDownloading: Boolean = false,
    val downloadProgress: Float = 0f
)

data class SearchResult(
    val filePath: String,
    val fileName: String,
    val lineNumber: Int,
    val lineContent: String,
    val matchStart: Int,
    val matchEnd: Int
)

enum class SidePanelTab {
    PROJECT_EXPLORER, GIT, SEARCH, SDK_MANAGER, WIRELESS_ADB, AI_ASSISTANT, SETTINGS
}

enum class BottomPanelTab {
    BUILD_OUTPUT, TERMINAL, LOGCAT, PROBLEMS
}
