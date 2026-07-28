package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.model.AdbDevice
import com.example.model.SdkComponent
import com.example.model.SidePanelTab
import com.example.ui.theme.IdeBluePrimary
import com.example.ui.theme.IdeGreenSuccess
import com.example.ui.viewmodel.AiMessage

@Composable
fun SidePanels(
    activeTab: SidePanelTab,
    sdkComponents: List<SdkComponent>,
    adbDevices: List<AdbDevice>,
    aiMessages: List<AiMessage>,
    isAiGenerating: Boolean,
    isDarkTheme: Boolean,
    autoSaveEnabled: Boolean,
    fontSizeSp: Int,
    onSendAiPrompt: (String) -> Unit,
    onToggleSdkComponent: (String) -> Unit,
    onOpenAdbDialog: () -> Unit,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .width(280.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            when (activeTab) {
                SidePanelTab.PROJECT_EXPLORER -> {
                    // Rendered separately in main layout
                }
                SidePanelTab.GIT -> GitView()
                SidePanelTab.SEARCH -> SearchView()
                SidePanelTab.SDK_MANAGER -> SdkManagerView(sdkComponents, onToggleSdkComponent)
                SidePanelTab.WIRELESS_ADB -> WirelessAdbView(adbDevices, onOpenAdbDialog)
                SidePanelTab.AI_ASSISTANT -> AiAssistantView(aiMessages, isAiGenerating, onSendAiPrompt)
                SidePanelTab.SETTINGS -> SettingsView(isDarkTheme, autoSaveEnabled, fontSizeSp, onToggleTheme)
            }
        }
    }
}

@Composable
private fun GitView() {
    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        Text("GIT INTEGRATION", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Commit, contentDescription = null, tint = IdeGreenSuccess)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("Branch: main", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("Working tree clean", fontSize = 11.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(6.dp)
        ) {
            Icon(Icons.Default.Upload, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Commit & Push")
        }
    }
}

@Composable
private fun SearchView() {
    var query by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        Text("SEARCH & REPLACE", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search files...", fontSize = 12.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        )
    }
}

@Composable
private fun SdkManagerView(
    components: List<SdkComponent>,
    onToggleComponent: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        Text("SDK MANAGER", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(components) { comp ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(comp.name, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("${comp.sizeMb} MB | ${if (comp.isInstalled) "Installed" else "Available"}", fontSize = 10.sp, color = Color.Gray)
                    }
                    Checkbox(
                        checked = comp.isInstalled,
                        onCheckedChange = { onToggleComponent(comp.name) }
                    )
                }
            }
        }
    }
}

@Composable
private fun WirelessAdbView(
    devices: List<AdbDevice>,
    onOpenPairDialog: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        Text("WIRELESS ADB", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onOpenPairDialog,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(6.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Pair Device IP & Port")
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(devices) { dev ->
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Smartphone,
                            contentDescription = null,
                            tint = if (dev.isConnected) IdeGreenSuccess else Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(dev.name, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("${dev.ipAddress}:${dev.port} | Battery: ${dev.batteryPercent}%", fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AiAssistantView(
    messages: List<AiMessage>,
    isGenerating: Boolean,
    onSendPrompt: (String) -> Unit
) {
    var promptInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = IdeBluePrimary)
            Spacer(modifier = Modifier.width(6.dp))
            Text("AI ASSISTANT", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                val isUser = msg.sender == "USER"
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Surface(
                        color = if (isUser) IdeBluePrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = msg.content,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }

        if (isGenerating) {
            CircularProgressIndicator(modifier = Modifier.size(18.dp).align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(6.dp))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = promptInput,
                onValueChange = { promptInput = it },
                placeholder = { Text("Ask AI to write Kotlin/Compose code...", fontSize = 11.sp) },
                modifier = Modifier.weight(1f).height(46.dp).testTag("ai_prompt_input")
            )
            IconButton(onClick = {
                onSendPrompt(promptInput)
                promptInput = ""
            }) {
                Icon(Icons.Default.Send, contentDescription = "Send AI", tint = IdeBluePrimary)
            }
        }
    }
}

@Composable
private fun SettingsView(
    isDarkTheme: Boolean,
    autoSaveEnabled: Boolean,
    fontSizeSp: Int,
    onToggleTheme: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        Text("IDE PREFERENCES", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark Theme", fontSize = 12.sp)
            Switch(checked = isDarkTheme, onCheckedChange = { onToggleTheme() })
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Auto Save", fontSize = 12.sp)
            Switch(checked = autoSaveEnabled, onCheckedChange = {})
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("Editor Font Size: ${fontSizeSp}sp", fontSize = 12.sp)
    }
}
