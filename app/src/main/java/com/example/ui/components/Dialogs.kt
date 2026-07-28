package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IdeBluePrimary

@Composable
fun AiAppPromptDialog(
    onDismiss: () -> Unit,
    onGenerateApp: (prompt: String) -> Unit
) {
    var promptInput by remember { mutableStateOf("Build a Todo & Task Planner app with search and categories") }

    val presetPrompts = listOf(
        "📝 Todo List & Task Planner",
        "🧮 Scientific Calculator",
        "☀️ Weather & Forecast Hub",
        "🍳 Recipe Finder & Cookbook",
        "🏋️ Fitness & Workout Tracker"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = IdeBluePrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("✨ AI App Builder / بناء تطبيق بالذكاء الاصطناعي", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Enter a prompt in Arabic or English to automatically generate Kotlin & Jetpack Compose source code, ViewModel architecture, and UI layout:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = promptInput,
                    onValueChange = { promptInput = it },
                    label = { Text("App Generation Prompt / وصف التطبيق") },
                    placeholder = { Text("e.g. Build a Expense Tracker app with categories...") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth().testTag("ai_app_prompt_input")
                )

                Text("Popular App Presets:", fontSize = 11.sp, fontWeight = FontWeight.Bold)

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(presetPrompts) { preset ->
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    promptInput = preset.substringAfter(" ").trim()
                                }
                        ) {
                            Text(
                                text = preset,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onGenerateApp(promptInput) },
                modifier = Modifier.testTag("ai_app_generate_confirm_button")
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Generate App")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun NewProjectDialog(
    onDismiss: () -> Unit,
    onCreateProject: (name: String, packageName: String, template: String) -> Unit
) {
    var name by remember { mutableStateOf("MyAwesomeApp") }
    var packageName by remember { mutableStateOf("com.aistudio.myawesomeapp") }
    var selectedTemplate by remember { mutableStateOf("Compose Activity") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Android Project", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        packageName = "com.aistudio.${it.lowercase()}"
                    },
                    label = { Text("Application Name") },
                    modifier = Modifier.fillMaxWidth().testTag("new_project_name_input")
                )
                OutlinedTextField(
                    value = packageName,
                    onValueChange = { packageName = it },
                    label = { Text("Package Name") },
                    modifier = Modifier.fillMaxWidth().testTag("new_project_package_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onCreateProject(name, packageName, selectedTemplate) },
                modifier = Modifier.testTag("create_project_confirm_button")
            ) {
                Text("Create Project")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun AdbPairDialog(
    onDismiss: () -> Unit,
    onPairConnect: (ip: String, port: Int) -> Unit
) {
    var ip by remember { mutableStateOf("192.168.1.108") }
    var port by remember { mutableStateOf("5555") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.WifiTethering, contentDescription = null, tint = IdeBluePrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pair Wireless ADB Device", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = ip,
                    onValueChange = { ip = it },
                    label = { Text("IP Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = port,
                    onValueChange = { port = it },
                    label = { Text("Port (Default 5555)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onPairConnect(ip, port.toIntOrNull() ?: 5555) }) {
                Text("Connect")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun ShortcutsDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Keyboard, contentDescription = null, tint = IdeBluePrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Keyboard Shortcuts", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ShortcutItem("Ctrl + S", "Save current open file")
                ShortcutItem("Ctrl + B / F5", "Run Gradle Build & Assemble APK")
                ShortcutItem("Ctrl + Shift + F", "Global Search & Replace")
                ShortcutItem("Ctrl + \\", "Toggle Split Screen Editor")
                ShortcutItem("Ctrl + Shift + A", "Open AI Assistant")
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) { Text("Got it") }
        }
    )
}

@Composable
private fun ShortcutItem(keys: String, description: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)) {
            Text(keys, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
        }
        Text(description, fontSize = 11.sp)
    }
}
