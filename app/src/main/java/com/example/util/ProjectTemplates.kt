package com.example.util

import com.example.data.entity.FileEntity

object ProjectTemplates {

    fun createEmptyComposeProjectFiles(projectId: String, projectName: String, packageName: String): List<FileEntity> {
        val root = "Project/"
        val now = System.currentTimeMillis()

        return listOf(
            FileEntity(
                id = "${projectId}_root",
                projectId = projectId,
                path = root,
                name = projectName,
                isDirectory = true,
                lastModifiedAt = now
            ),
            FileEntity(
                id = "${projectId}_app",
                projectId = projectId,
                path = "${root}app",
                name = "app",
                isDirectory = true,
                parentPath = root,
                lastModifiedAt = now
            ),
            FileEntity(
                id = "${projectId}_build_gradle",
                projectId = projectId,
                path = "${root}app/build.gradle.kts",
                name = "build.gradle.kts",
                isDirectory = false,
                extension = "kts",
                parentPath = "${root}app",
                content = """
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "$packageName"
    compileSdk = 36

    defaultConfig {
        applicationId = "$packageName"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.core.ktx)
}
                """.trimIndent(),
                lastModifiedAt = now
            ),
            FileEntity(
                id = "${projectId}_src",
                projectId = projectId,
                path = "${root}app/src",
                name = "src",
                isDirectory = true,
                parentPath = "${root}app",
                lastModifiedAt = now
            ),
            FileEntity(
                id = "${projectId}_main",
                projectId = projectId,
                path = "${root}app/src/main",
                name = "main",
                isDirectory = true,
                parentPath = "${root}app/src",
                lastModifiedAt = now
            ),
            FileEntity(
                id = "${projectId}_java",
                projectId = projectId,
                path = "${root}app/src/main/java",
                name = "java",
                isDirectory = true,
                parentPath = "${root}app/src/main",
                lastModifiedAt = now
            ),
            FileEntity(
                id = "${projectId}_main_activity",
                projectId = projectId,
                path = "${root}app/src/main/java/MainActivity.kt",
                name = "MainActivity.kt",
                isDirectory = false,
                extension = "kt",
                parentPath = "${root}app/src/main/java",
                content = """
package $packageName

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppScreen(title = "$projectName")
                }
            }
        }
    }
}

@Composable
fun AppScreen(title: String) {
    var count by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to $projectName!",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { count++ }) {
            Text("Counter: ${'$'}count")
        }
    }
}
                """.trimIndent(),
                lastModifiedAt = now
            ),
            FileEntity(
                id = "${projectId}_manifest",
                projectId = projectId,
                path = "${root}app/src/main/AndroidManifest.xml",
                name = "AndroidManifest.xml",
                isDirectory = false,
                extension = "xml",
                parentPath = "${root}app/src/main",
                content = """
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="$projectName"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@android:style/Theme.Material.NoActionBar">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
                """.trimIndent(),
                lastModifiedAt = now
            ),
            FileEntity(
                id = "${projectId}_res",
                projectId = projectId,
                path = "${root}app/src/main/res",
                name = "res",
                isDirectory = true,
                parentPath = "${root}app/src/main",
                lastModifiedAt = now
            ),
            FileEntity(
                id = "${projectId}_values",
                projectId = projectId,
                path = "${root}app/src/main/res/values",
                name = "values",
                isDirectory = true,
                parentPath = "${root}app/src/main/res",
                lastModifiedAt = now
            ),
            FileEntity(
                id = "${projectId}_strings_xml",
                projectId = projectId,
                path = "${root}app/src/main/res/values/strings.xml",
                name = "strings.xml",
                isDirectory = false,
                extension = "xml",
                parentPath = "${root}app/src/main/res/values",
                content = """
<resources>
    <string name="app_name">$projectName</string>
    <string name="welcome_message">Welcome to $projectName</string>
</resources>
                """.trimIndent(),
                lastModifiedAt = now
            ),
            FileEntity(
                id = "${projectId}_settings_gradle",
                projectId = projectId,
                path = "${root}settings.gradle.kts",
                name = "settings.gradle.kts",
                isDirectory = false,
                extension = "kts",
                parentPath = root,
                content = """
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "$projectName"
include(":app")
                """.trimIndent(),
                lastModifiedAt = now
            ),
            FileEntity(
                id = "${projectId}_readme",
                projectId = projectId,
                path = "${root}README.md",
                name = "README.md",
                isDirectory = false,
                extension = "md",
                parentPath = root,
                content = """
# $projectName

An Android application built with Kotlin, Jetpack Compose, and Material Design 3.

## Features
- Modern Jetpack Compose UI
- Material 3 Design System
- Built with CodeCraft Studio mobile IDE
                """.trimIndent(),
                lastModifiedAt = now
            )
        )
    }

    fun createAiGeneratedProjectFiles(projectId: String, prompt: String): Pair<ProjectEntityInfo, List<FileEntity>> {
        val lowerPrompt = prompt.lowercase()
        val now = System.currentTimeMillis()
        val root = "Project/"

        val (appName, pkgName, mainActivityContent, extraFiles) = when {
            lowerPrompt.contains("todo") || lowerPrompt.contains("task") || lowerPrompt.contains("مهام") || lowerPrompt.contains("قائمة") -> {
                val appName = "Smart Tasks"
                val pkgName = "com.aistudio.smarttasks"
                val extra = listOf(
                    FileEntity(
                        id = "${projectId}_task_model",
                        projectId = projectId,
                        path = "${root}app/src/main/java/TaskModel.kt",
                        name = "TaskModel.kt",
                        isDirectory = false,
                        extension = "kt",
                        parentPath = "${root}app/src/main/java",
                        content = """
package $pkgName

data class TaskItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val isCompleted: Boolean = false,
    val category: String = "General",
    val createdAt: Long = System.currentTimeMillis()
)
                        """.trimIndent(),
                        lastModifiedAt = now
                    )
                )

                val mainCode = """
package $pkgName

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TodoAppScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoAppScreen() {
    var tasks by remember { mutableStateOf(listOf(
        TaskItem(title = "Design Jetpack Compose UI", isCompleted = true, category = "Android"),
        TaskItem(title = "Implement Room Database", category = "Data"),
        TaskItem(title = "Deploy APK with CodeCraft Studio", category = "Release")
    )) }
    var newTaskTitle by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("✨ AI Smart Tasks", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search tasks...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newTaskTitle,
                    onValueChange = { newTaskTitle = it },
                    placeholder = { Text("Add new task...") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (newTaskTitle.isNotBlank()) {
                            tasks = tasks + TaskItem(title = newTaskTitle)
                            newTaskTitle = ""
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Your Tasks (${'$'}{tasks.count { !it.isCompleted }} pending)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

            val filteredTasks = tasks.filter { it.title.contains(searchQuery, ignoreCase = true) }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filteredTasks, key = { it.id }) { task ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (task.isCompleted) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Checkbox(
                                    checked = task.isCompleted,
                                    onCheckedChange = { checked ->
                                        tasks = tasks.map { if (it.id == task.id) it.copy(isCompleted = checked) else it }
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = task.title,
                                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                                    fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.Bold
                                )
                            }
                            IconButton(onClick = { tasks = tasks.filter { it.id != task.id } }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}
                """.trimIndent()

                Quadruple(appName, pkgName, mainCode, extra)
            }
            lowerPrompt.contains("calc") || lowerPrompt.contains("حاسبة") -> {
                val appName = "AI Calculator"
                val pkgName = "com.aistudio.aicalculator"
                val extra = emptyList<FileEntity>()
                val mainCode = """
package $pkgName

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF1E1E2E)) {
                    CalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var display by remember { mutableStateOf("0") }
    var expression by remember { mutableStateOf("") }

    val buttons = listOf(
        listOf("C", "±", "%", "÷"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "=")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = expression,
            fontSize = 20.sp,
            color = Color.Gray,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth().padding(end = 12.dp)
        )
        Text(
            text = display,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth().padding(end = 12.dp, bottom = 24.dp)
        )

        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { label ->
                    val isOp = label in listOf("÷", "×", "-", "+", "=")
                    val isFn = label in listOf("C", "±", "%")
                    val weight = if (label == "0") 2f else 1f

                    Box(
                        modifier = Modifier
                            .weight(weight)
                            .aspectRatio(if (label == "0") 2.1f else 1f)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isOp -> Color(0xFFFF9500)
                                    isFn -> Color(0xFFA5A5A5)
                                    else -> Color(0xFF333333)
                                }
                            )
                            .clickable {
                                when (label) {
                                    "C" -> { display = "0"; expression = "" }
                                    "=" -> {
                                        display = try {
                                            "42" // Simulated engine result
                                        } catch (e: Exception) { "Error" }
                                    }
                                    else -> {
                                        if (display == "0") display = label else display += label
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isFn) Color.Black else Color.White
                        )
                    }
                }
            }
        }
    }
}
                """.trimIndent()
                Quadruple(appName, pkgName, mainCode, extra)
            }
            lowerPrompt.contains("weather") || lowerPrompt.contains("طقس") -> {
                val appName = "SkyPulse Weather"
                val pkgName = "com.aistudio.skypulse"
                val extra = emptyList<FileEntity>()
                val mainCode = """
package $pkgName

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WeatherScreen()
                }
            }
        }
    }
}

@Composable
fun WeatherScreen() {
    var city by remember { mutableStateOf("Riyadh") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Search City") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.WbSunny, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color(0xFFFFB300))
                Spacer(modifier = Modifier.height(12.dp))
                Text(city, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text("32°C | Sunny", fontSize = 22.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text("Humidity: 24% | Wind: 14 km/h", fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Weekly Forecast", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(7) { day ->
                Card(modifier = Modifier.width(90.dp).height(110.dp)) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text("Day ${'$'}{day + 1}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.WbCloudy, contentDescription = null, modifier = Modifier.size(24.dp))
                        Text("${'$'}{30 + day}°C", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
                """.trimIndent()
                Quadruple(appName, pkgName, mainCode, extra)
            }
            else -> {
                val cleanTitle = prompt.trim()
                    .take(20)
                    .replace(Regex("[^a-zA-Z0-9 ]"), "")
                    .ifBlank { "AI Smart App" }
                val appName = if (cleanTitle.endsWith("App")) cleanTitle else "$cleanTitle App"
                val pkgName = "com.aistudio.${appName.lowercase().replace(" ", "")}"
                val extra = emptyList<FileEntity>()
                val mainCode = """
package $pkgName

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GeneratedAppScreen(prompt = "$prompt")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneratedAppScreen(prompt: String) {
    var count by remember { mutableStateOf(0) }
    var note by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("✨ $appName", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AI Generated Application", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Prompt: \"$prompt\"", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("App Workspace Input") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { count++ }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Counter (${'$'}count)")
                }
                OutlinedButton(onClick = { count = 0 }) {
                    Text("Reset")
                }
            }
        }
    }
}
                """.trimIndent()
                Quadruple(appName, pkgName, mainCode, extra)
            }
        }

        val baseFiles = createEmptyComposeProjectFiles(projectId, appName, pkgName).toMutableList()

        // Replace MainActivity content in baseFiles
        val mainIndex = baseFiles.indexOfFirst { it.name == "MainActivity.kt" }
        if (mainIndex >= 0) {
            baseFiles[mainIndex] = baseFiles[mainIndex].copy(content = mainActivityContent)
        }

        // Add any extra files
        baseFiles.addAll(extraFiles)

        val projectInfo = ProjectEntityInfo(
            id = projectId,
            name = appName,
            packageName = pkgName
        )

        return Pair(projectInfo, baseFiles)
    }
}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
data class ProjectEntityInfo(val id: String, val name: String, val packageName: String)

