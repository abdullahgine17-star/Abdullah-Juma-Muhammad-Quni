package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.SidePanelTab
import com.example.ui.components.*
import com.example.ui.theme.IdeBluePrimary
import com.example.ui.viewmodel.IdeViewModel

@Composable
fun IdeScreen(
    viewModel: IdeViewModel,
    modifier: Modifier = Modifier
) {
    val currentProject by viewModel.currentProject.collectAsStateWithLifecycle()
    val projectsList by viewModel.projects.collectAsStateWithLifecycle()
    val fileTree by viewModel.fileTree.collectAsStateWithLifecycle()
    val rawFiles by viewModel.rawFiles.collectAsStateWithLifecycle()

    val openTabs by viewModel.openTabs.collectAsStateWithLifecycle()
    val activeTabIndex by viewModel.activeTabIndex.collectAsStateWithLifecycle()
    val splitScreenEnabled by viewModel.splitScreenEnabled.collectAsStateWithLifecycle()
    val splitTabIndex by viewModel.splitTabIndex.collectAsStateWithLifecycle()

    val activeSideTab by viewModel.activeSideTab.collectAsStateWithLifecycle()
    val sidePanelExpanded by viewModel.sidePanelExpanded.collectAsStateWithLifecycle()
    val activeBottomTab by viewModel.activeBottomTab.collectAsStateWithLifecycle()
    val bottomPanelExpanded by viewModel.bottomPanelExpanded.collectAsStateWithLifecycle()

    val diagnostics by viewModel.diagnostics.collectAsStateWithLifecycle()
    val buildStatus by viewModel.buildStatus.collectAsStateWithLifecycle()
    val buildSteps by viewModel.buildSteps.collectAsStateWithLifecycle()
    val buildLog by viewModel.buildOutputLog.collectAsStateWithLifecycle()
    val lastApkResult by viewModel.lastApkResult.collectAsStateWithLifecycle()

    val terminalOutput by viewModel.terminalOutput.collectAsStateWithLifecycle()
    val logcatEntries by viewModel.logcatEntries.collectAsStateWithLifecycle()
    val adbDevices by viewModel.adbDevices.collectAsStateWithLifecycle()
    val sdkComponents by viewModel.sdkComponents.collectAsStateWithLifecycle()
    val aiMessages by viewModel.aiMessages.collectAsStateWithLifecycle()
    val isAiGenerating by viewModel.isAiGenerating.collectAsStateWithLifecycle()

    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    val fontSizeSp by viewModel.fontSizeSp.collectAsStateWithLifecycle()
    val autoSaveEnabled by viewModel.autoSaveEnabled.collectAsStateWithLifecycle()
    val ramUsageMb by viewModel.ramUsageMb.collectAsStateWithLifecycle()

    val showNewProjectDialog by viewModel.showNewProjectDialog.collectAsStateWithLifecycle()
    val showAdbPairDialog by viewModel.showAdbPairDialog.collectAsStateWithLifecycle()
    val showShortcutsDialog by viewModel.showShortcutsDialog.collectAsStateWithLifecycle()
    val showAiAppBuilderDialog by viewModel.showAiAppBuilderDialog.collectAsStateWithLifecycle()

    val activeTab = openTabs.getOrNull(activeTabIndex)
    val splitTab = openTabs.getOrNull(splitTabIndex)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            StatusBar(
                gitBranch = currentProject?.gitBranch ?: "main",
                lineCount = activeTab?.lineCount ?: 1,
                language = activeTab?.language,
                ramUsageMb = ramUsageMb,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top Toolbar
            TopToolbar(
                currentProject = currentProject,
                projectsList = projectsList,
                buildStatus = buildStatus,
                isDarkTheme = isDarkTheme,
                splitScreenEnabled = splitScreenEnabled,
                adbConnected = adbDevices.any { it.isConnected },
                onMenuClick = { viewModel.toggleSidePanel() },
                onSelectProject = { viewModel.selectProject(it) },
                onRunBuildClick = { viewModel.triggerGradleBuild() },
                onSyncClick = { viewModel.triggerGradleBuild() },
                onToggleSplitScreen = { viewModel.toggleSplitScreen() },
                onToggleTheme = { viewModel.toggleTheme() },
                onOpenAdbDialog = { viewModel.showAdbPairDialog.value = true },
                onOpenNewProjectDialog = { viewModel.showNewProjectDialog.value = true },
                onOpenShortcutsDialog = { viewModel.showShortcutsDialog.value = true },
                onOpenAiAppBuilderDialog = { viewModel.showAiAppBuilderDialog.value = true },
                onToggleAiClick = { viewModel.setSideTab(SidePanelTab.AI_ASSISTANT) }
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            // Main IDE Workspace Layout
            Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                // Navigation Rail Sidebar Icons
                Column(
                    modifier = Modifier
                        .width(48.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    NavRailIconButton(
                        icon = Icons.Default.Folder,
                        label = "Project",
                        isSelected = sidePanelExpanded && activeSideTab == SidePanelTab.PROJECT_EXPLORER,
                        onClick = { viewModel.setSideTab(SidePanelTab.PROJECT_EXPLORER) }
                    )
                    NavRailIconButton(
                        icon = Icons.Default.Commit,
                        label = "Git",
                        isSelected = sidePanelExpanded && activeSideTab == SidePanelTab.GIT,
                        onClick = { viewModel.setSideTab(SidePanelTab.GIT) }
                    )
                    NavRailIconButton(
                        icon = Icons.Default.Search,
                        label = "Search",
                        isSelected = sidePanelExpanded && activeSideTab == SidePanelTab.SEARCH,
                        onClick = { viewModel.setSideTab(SidePanelTab.SEARCH) }
                    )
                    NavRailIconButton(
                        icon = Icons.Default.Download,
                        label = "SDK",
                        isSelected = sidePanelExpanded && activeSideTab == SidePanelTab.SDK_MANAGER,
                        onClick = { viewModel.setSideTab(SidePanelTab.SDK_MANAGER) }
                    )
                    NavRailIconButton(
                        icon = Icons.Default.WifiTethering,
                        label = "ADB",
                        isSelected = sidePanelExpanded && activeSideTab == SidePanelTab.WIRELESS_ADB,
                        onClick = { viewModel.setSideTab(SidePanelTab.WIRELESS_ADB) }
                    )
                    NavRailIconButton(
                        icon = Icons.Default.AutoAwesome,
                        label = "AI",
                        isSelected = sidePanelExpanded && activeSideTab == SidePanelTab.AI_ASSISTANT,
                        onClick = { viewModel.setSideTab(SidePanelTab.AI_ASSISTANT) }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    NavRailIconButton(
                        icon = Icons.Default.Settings,
                        label = "Settings",
                        isSelected = sidePanelExpanded && activeSideTab == SidePanelTab.SETTINGS,
                        onClick = { viewModel.setSideTab(SidePanelTab.SETTINGS) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                VerticalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                // Collapsible Side Panel
                if (sidePanelExpanded) {
                    Box(modifier = Modifier.width(260.dp).fillMaxHeight()) {
                        if (activeSideTab == SidePanelTab.PROJECT_EXPLORER) {
                            ProjectExplorer(
                                fileTree = fileTree,
                                rawFiles = rawFiles,
                                onFileClick = { viewModel.openFileInTab(it) },
                                onAddFileClick = { path -> viewModel.addNewFile(path) },
                                onDeleteFileClick = { path -> viewModel.removeFile(path) }
                            )
                        } else {
                            SidePanels(
                                activeTab = activeSideTab,
                                sdkComponents = sdkComponents,
                                adbDevices = adbDevices,
                                aiMessages = aiMessages,
                                isAiGenerating = isAiGenerating,
                                isDarkTheme = isDarkTheme,
                                autoSaveEnabled = autoSaveEnabled,
                                fontSizeSp = fontSizeSp,
                                onSendAiPrompt = { viewModel.sendAiPrompt(it) },
                                onToggleSdkComponent = { viewModel.toggleSdkComponent(it) },
                                onOpenAdbDialog = { viewModel.showAdbPairDialog.value = true },
                                onToggleTheme = { viewModel.toggleTheme() }
                            )
                        }
                    }

                    VerticalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                }

                // Editor Workspace
                Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    // Editor Tab Row
                    if (openTabs.isNotEmpty()) {
                        EditorTabs(
                            tabs = openTabs,
                            activeTabIndex = activeTabIndex,
                            onTabSelect = { viewModel.openFileInTab(rawFiles.find { f -> f.path == openTabs[it].filePath } ?: rawFiles.first()) },
                            onTabClose = { viewModel.closeTab(it) }
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    }

                    // Code Editor (Single or Split Screen)
                    Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        CodeEditor(
                            tab = activeTab,
                            fontSizeSp = fontSizeSp,
                            diagnostics = diagnostics,
                            onContentChange = { viewModel.updateActiveTabContent(it) },
                            modifier = Modifier.weight(1f)
                        )

                        if (splitScreenEnabled && splitTab != null) {
                            VerticalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                            CodeEditor(
                                tab = splitTab,
                                fontSizeSp = fontSizeSp,
                                diagnostics = emptyList(),
                                onContentChange = {},
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Bottom Panel
                    if (bottomPanelExpanded) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        BottomPanel(
                            activeTab = activeBottomTab,
                            buildStatus = buildStatus,
                            buildSteps = buildSteps,
                            buildLog = buildLog,
                            lastApkResult = lastApkResult,
                            terminalOutput = terminalOutput,
                            logcatEntries = logcatEntries,
                            diagnostics = diagnostics,
                            onTabSelect = { viewModel.setBottomTab(it) },
                            onTerminalCommandSubmit = { viewModel.executeTerminalCommand(it) },
                            onCloseClick = { viewModel.toggleBottomPanel() }
                        )
                    }
                }
            }
        }

        // Dialogs
        if (showAiAppBuilderDialog) {
            AiAppPromptDialog(
                onDismiss = { viewModel.showAiAppBuilderDialog.value = false },
                onGenerateApp = { viewModel.createProjectFromAiPrompt(it) }
            )
        }

        if (showNewProjectDialog) {
            NewProjectDialog(
                onDismiss = { viewModel.showNewProjectDialog.value = false },
                onCreateProject = { name, pkg, tmpl -> viewModel.createNewProject(name, pkg, tmpl) }
            )
        }

        if (showAdbPairDialog) {
            AdbPairDialog(
                onDismiss = { viewModel.showAdbPairDialog.value = false },
                onPairConnect = { ip, port -> viewModel.connectAdbDevice(ip, port) }
            )
        }

        if (showShortcutsDialog) {
            ShortcutsDialog(
                onDismiss = { viewModel.showShortcutsDialog.value = false }
            )
        }
    }
}

@Composable
private fun NavRailIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(36.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) IdeBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
    }
}
