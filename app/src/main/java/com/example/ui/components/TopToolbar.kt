package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.ProjectEntity
import com.example.model.BuildStatus
import com.example.ui.theme.IdeBluePrimary
import com.example.ui.theme.IdeGreenSuccess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopToolbar(
    currentProject: ProjectEntity?,
    projectsList: List<ProjectEntity>,
    buildStatus: BuildStatus,
    isDarkTheme: Boolean,
    splitScreenEnabled: Boolean,
    adbConnected: Boolean,
    onMenuClick: () -> Unit,
    onSelectProject: (ProjectEntity) -> Unit,
    onRunBuildClick: () -> Unit,
    onSyncClick: () -> Unit,
    onToggleSplitScreen: () -> Unit,
    onToggleTheme: () -> Unit,
    onOpenAdbDialog: () -> Unit,
    onOpenNewProjectDialog: () -> Unit,
    onOpenShortcutsDialog: () -> Unit,
    onOpenAiAppBuilderDialog: () -> Unit,
    onToggleAiClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var projectMenuExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Side: Menu + Project Selector + New Proj
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier.testTag("toolbar_menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Project Navigation Drawer",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Project Dropdown
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable { projectMenuExpanded = true }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("project_selector_dropdown"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = "Active Project",
                            tint = IdeBluePrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = currentProject?.name ?: "No Project",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Project",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = projectMenuExpanded,
                        onDismissRequest = { projectMenuExpanded = false }
                    ) {
                        Text(
                            text = "Switch Project",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                        HorizontalDivider()
                        projectsList.forEach { proj ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(proj.name, fontWeight = FontWeight.SemiBold)
                                        Text(proj.packageName, fontSize = 10.sp, color = Color.Gray)
                                    }
                                },
                                onClick = {
                                    onSelectProject(proj)
                                    projectMenuExpanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (proj.id == currentProject?.id) Icons.Default.Check else Icons.Default.FolderOpen,
                                        contentDescription = null,
                                        tint = if (proj.id == currentProject?.id) IdeGreenSuccess else Color.Gray
                                    )
                                }
                            )
                        }
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("✨ AI App Builder (From Prompt)...", fontWeight = FontWeight.Bold, color = IdeBluePrimary) },
                            onClick = {
                                projectMenuExpanded = false
                                onOpenAiAppBuilderDialog()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = IdeBluePrimary)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Create Blank Project...", fontWeight = FontWeight.Bold) },
                            onClick = {
                                projectMenuExpanded = false
                                onOpenNewProjectDialog()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Add, contentDescription = null, tint = Color.Gray)
                            }
                        )
                    }
                }

                // AI Prompt App Builder Fast Button
                FilledTonalButton(
                    onClick = onOpenAiAppBuilderDialog,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.testTag("ai_app_builder_toolbar_button")
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = IdeBluePrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("AI Builder", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // Sync Button
                IconButton(
                    onClick = onSyncClick,
                    modifier = Modifier.testTag("toolbar_sync_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Sync Gradle Project",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Center / Right Actions: Run Build + Tools
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Run/Build Button
                Button(
                    onClick = onRunBuildClick,
                    enabled = buildStatus != BuildStatus.BUILDING,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IdeGreenSuccess,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("toolbar_run_button")
                ) {
                    if (buildStatus == BuildStatus.BUILDING) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.Black,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Building...", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Run App & Build APK",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Run App", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Wireless ADB Indicator
                AssistChip(
                    onClick = onOpenAdbDialog,
                    label = {
                        Text(
                            text = if (adbConnected) "ADB Active" else "Pair ADB",
                            fontSize = 11.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = if (adbConnected) Icons.Default.WifiTethering else Icons.Default.WifiOff,
                            contentDescription = "Wireless ADB",
                            tint = if (adbConnected) IdeGreenSuccess else Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                    },
                    modifier = Modifier.height(32.dp)
                )

                // Split Screen Toggle
                IconButton(onClick = onToggleSplitScreen) {
                    Icon(
                        imageVector = if (splitScreenEnabled) Icons.Default.VerticalSplit else Icons.Default.HorizontalSplit,
                        contentDescription = "Split Screen Editor",
                        tint = if (splitScreenEnabled) IdeBluePrimary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // AI Assistant Button
                IconButton(onClick = onToggleAiClick) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Coding Assistant",
                        tint = IdeBluePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Theme Toggle
                IconButton(onClick = onToggleTheme) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = "Toggle Dark/Light Theme",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Shortcuts Help
                IconButton(onClick = onOpenShortcutsDialog) {
                    Icon(
                        imageVector = Icons.Default.Keyboard,
                        contentDescription = "Keyboard Shortcuts",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
