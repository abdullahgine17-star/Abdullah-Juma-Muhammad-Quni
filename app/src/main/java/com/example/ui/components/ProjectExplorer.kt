package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.entity.FileEntity
import com.example.model.FileNode
import com.example.ui.theme.IdeBluePrimary
import com.example.ui.theme.IdeGreenSuccess

@Composable
fun ProjectExplorer(
    fileTree: FileNode?,
    rawFiles: List<FileEntity>,
    onFileClick: (FileEntity) -> Unit,
    onAddFileClick: (parentPath: String) -> Unit,
    onDeleteFileClick: (path: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val expandedPaths = remember { mutableStateMapOf<String, Boolean>() }

    // Expand root by default
    LaunchedEffect(fileTree) {
        fileTree?.let { expandedPaths[it.path] = true }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Explorer Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "PROJECT EXPLORER",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row {
                IconButton(
                    onClick = { onAddFileClick("Project/app/src/main/java/") },
                    modifier = Modifier.size(24.dp).testTag("explorer_add_file")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New File",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

        if (fileTree == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                renderNode(
                    node = fileTree,
                    depth = 0,
                    expandedPaths = expandedPaths,
                    rawFiles = rawFiles,
                    onFileClick = onFileClick,
                    onToggleExpand = { path ->
                        expandedPaths[path] = !(expandedPaths[path] ?: false)
                    },
                    onAddFileClick = onAddFileClick,
                    onDeleteFileClick = onDeleteFileClick
                )
            }
        }
    }
}

private fun androidx.compose.foundation.lazy.LazyListScope.renderNode(
    node: FileNode,
    depth: Int,
    expandedPaths: Map<String, Boolean>,
    rawFiles: List<FileEntity>,
    onFileClick: (FileEntity) -> Unit,
    onToggleExpand: (String) -> Unit,
    onAddFileClick: (String) -> Unit,
    onDeleteFileClick: (String) -> Unit
) {
    val isExpanded = expandedPaths[node.path] ?: false

    item(key = node.path) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
                .padding(start = (depth * 14 + 8).dp, end = 8.dp)
                .clip(RoundedCornerShape(4.dp))
                .clickable {
                    if (node.isDirectory) {
                        onToggleExpand(node.path)
                    } else {
                        val fileEntity = rawFiles.find { it.path == node.path }
                        fileEntity?.let { onFileClick(it) }
                    }
                }
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Expansion Icon or Spacer
            if (node.isDirectory) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Spacer(modifier = Modifier.width(16.dp))
            }

            Spacer(modifier = Modifier.width(4.dp))

            // File / Directory Icon
            FileIcon(node = node, isExpanded = isExpanded)

            Spacer(modifier = Modifier.width(6.dp))

            // Node Name
            Text(
                text = node.name,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 13.sp,
                fontWeight = if (node.isDirectory) FontWeight.SemiBold else FontWeight.Normal,
                color = if (node.isGitModified) IdeGreenSuccess else MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            if (!node.isDirectory) {
                IconButton(
                    onClick = { onDeleteFileClick(node.path) },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Delete File",
                        tint = Color.Gray,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }

    if (node.isDirectory && isExpanded) {
        node.children.forEach { child ->
            renderNode(
                node = child,
                depth = depth + 1,
                expandedPaths = expandedPaths,
                rawFiles = rawFiles,
                onFileClick = onFileClick,
                onToggleExpand = onToggleExpand,
                onAddFileClick = onAddFileClick,
                onDeleteFileClick = onDeleteFileClick
            )
        }
    }
}

@Composable
fun FileIcon(node: FileNode, isExpanded: Boolean) {
    if (node.isDirectory) {
        Icon(
            imageVector = if (isExpanded) Icons.Default.FolderOpen else Icons.Default.Folder,
            contentDescription = null,
            tint = IdeBluePrimary,
            modifier = Modifier.size(16.dp)
        )
    } else {
        when {
            node.name.endsWith(".kt") -> {
                BadgeText("KT", Color(0xFF7F52FF))
            }
            node.name.endsWith(".java") -> {
                BadgeText("J", Color(0xFFE76F51))
            }
            node.name.endsWith(".xml") -> {
                BadgeText("XML", Color(0xFF2A9D8F))
            }
            node.name.endsWith(".kts") || node.name.endsWith(".gradle") -> {
                BadgeText("G", Color(0xFF0284C7))
            }
            node.name.endsWith(".json") -> {
                BadgeText("{ }", Color(0xFFF4A261))
            }
            else -> {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun BadgeText(label: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(3.dp),
        modifier = Modifier.height(16.dp)
    ) {
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
        )
    }
}
