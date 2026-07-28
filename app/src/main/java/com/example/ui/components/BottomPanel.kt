package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.IdeBluePrimary
import com.example.ui.theme.IdeGreenSuccess
import com.example.ui.theme.IdeRedError
import com.example.ui.theme.IdeYellowWarning

@Composable
fun BottomPanel(
    activeTab: BottomPanelTab,
    buildStatus: BuildStatus,
    buildSteps: List<BuildStep>,
    buildLog: String,
    lastApkResult: ApkBuildResult?,
    terminalOutput: String,
    logcatEntries: List<LogcatEntry>,
    diagnostics: List<DiagnosticItem>,
    onTabSelect: (BottomPanelTab) -> Unit,
    onTerminalCommandSubmit: (String) -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Tab Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TabButton(
                        label = "Build Output",
                        icon = Icons.Default.Build,
                        isSelected = activeTab == BottomPanelTab.BUILD_OUTPUT,
                        onClick = { onTabSelect(BottomPanelTab.BUILD_OUTPUT) }
                    )
                    TabButton(
                        label = "Terminal",
                        icon = Icons.Default.Terminal,
                        isSelected = activeTab == BottomPanelTab.TERMINAL,
                        onClick = { onTabSelect(BottomPanelTab.TERMINAL) }
                    )
                    TabButton(
                        label = "Logcat (${logcatEntries.size})",
                        icon = Icons.Default.Notes,
                        isSelected = activeTab == BottomPanelTab.LOGCAT,
                        onClick = { onTabSelect(BottomPanelTab.LOGCAT) }
                    )
                    TabButton(
                        label = "Problems (${diagnostics.size})",
                        icon = Icons.Default.Warning,
                        isSelected = activeTab == BottomPanelTab.PROBLEMS,
                        onClick = { onTabSelect(BottomPanelTab.PROBLEMS) }
                    )
                }

                IconButton(onClick = onCloseClick, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Collapse Panel",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            // Tab Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                when (activeTab) {
                    BottomPanelTab.BUILD_OUTPUT -> BuildOutputView(buildStatus, buildSteps, buildLog, lastApkResult)
                    BottomPanelTab.TERMINAL -> TerminalView(terminalOutput, onTerminalCommandSubmit)
                    BottomPanelTab.LOGCAT -> LogcatView(logcatEntries)
                    BottomPanelTab.PROBLEMS -> ProblemsView(diagnostics)
                }
            }
        }
    }
}

@Composable
private fun TabButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            contentColor = if (isSelected) IdeBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
private fun BuildOutputView(
    status: BuildStatus,
    steps: List<BuildStep>,
    log: String,
    lastApkResult: ApkBuildResult?
) {
    Row(modifier = Modifier.fillMaxSize()) {
        // Steps checklist on left
        Column(
            modifier = Modifier
                .width(220.dp)
                .fillMaxHeight()
                .padding(end = 8.dp)
        ) {
            Text(
                text = "GRADLE TASKS",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(steps) { step ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (step.isCompleted) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = IdeGreenSuccess, modifier = Modifier.size(14.dp))
                        } else if (step.isCurrent) {
                            CircularProgressIndicator(modifier = Modifier.size(12.dp), strokeWidth = 2.dp, color = IdeBluePrimary)
                        } else {
                            Icon(Icons.Default.RadioButtonUnchecked, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(step.name, fontSize = 11.sp, maxLines = 1)
                    }
                }
            }

            if (lastApkResult != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = IdeGreenSuccess.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(6.dp)) {
                        Text("APK Ready: ${lastApkResult.apkName}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = IdeGreenSuccess)
                        Text("Size: ${lastApkResult.sizeMb} MB | Time: ${lastApkResult.buildTimeSeconds}s", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }

        VerticalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

        // Logs on right
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp)
        ) {
            item {
                Text(
                    text = log.ifBlank { "No build active. Click 'Run App' in top bar to assemble debug APK." },
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun TerminalView(
    output: String,
    onCommandSubmit: (String) -> Unit
) {
    var commandInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(
                text = output,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("$ ", fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = IdeBluePrimary)
            OutlinedTextField(
                value = commandInput,
                onValueChange = { commandInput = it },
                singleLine = true,
                placeholder = { Text("Enter command (e.g., 'gradle build', 'help')", fontSize = 11.sp) },
                textStyle = LocalTextStyle.current.copy(fontSize = 11.sp, fontFamily = FontFamily.Monospace),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    onCommandSubmit(commandInput)
                    commandInput = ""
                }),
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .testTag("terminal_input")
            )
            IconButton(onClick = {
                onCommandSubmit(commandInput)
                commandInput = ""
            }) {
                Icon(Icons.Default.Send, contentDescription = "Run Command", tint = IdeBluePrimary)
            }
        }
    }
}

@Composable
private fun LogcatView(entries: List<LogcatEntry>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(entries) { log ->
            val color = when (log.level) {
                LogLevel.ERROR -> IdeRedError
                LogLevel.WARNING -> IdeYellowWarning
                LogLevel.INFO -> IdeBluePrimary
                LogLevel.DEBUG -> IdeGreenSuccess
                else -> Color.Gray
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(log.timestamp, fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color.Gray)
                Spacer(modifier = Modifier.width(6.dp))
                Text("[${log.level.name.first()}]", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = color)
                Spacer(modifier = Modifier.width(6.dp))
                Text("${log.tag}: ", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = color)
                Text(log.message, fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
private fun ProblemsView(diagnostics: List<DiagnosticItem>) {
    if (diagnostics.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No syntax problems detected", fontSize = 12.sp, color = IdeGreenSuccess)
        }
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(diagnostics) { diag ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (diag.severity == DiagnosticSeverity.ERROR) Icons.Default.Error else Icons.Default.Warning,
                        contentDescription = null,
                        tint = if (diag.severity == DiagnosticSeverity.ERROR) IdeRedError else IdeYellowWarning,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("${diag.filePath}:${diag.line} ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = IdeBluePrimary)
                    Text(diag.message, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}
