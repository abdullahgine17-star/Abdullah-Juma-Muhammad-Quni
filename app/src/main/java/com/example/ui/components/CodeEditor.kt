package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DiagnosticItem
import com.example.model.DiagnosticSeverity
import com.example.model.EditorTab
import com.example.model.LanguageType
import com.example.ui.theme.*

@Composable
fun CodeEditor(
    tab: EditorTab?,
    fontSizeSp: Int,
    diagnostics: List<DiagnosticItem>,
    onContentChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (tab == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = IdeBluePrimary,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "CodeCraft Studio Mobile IDE",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Select a file from Project Explorer to start editing",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
        return
    }

    var codeText by remember(tab.fileId) { mutableStateOf(tab.content) }
    val verticalScroll = rememberScrollState()
    val horizontalScroll = rememberScrollState()

    // Common Compose & Kotlin Autocomplete triggers
    val commonCompletions = listOf(
        "remember", "mutableStateOf", "Column", "Row", "Box", "Text", "Button",
        "Modifier", "fillMaxSize", "padding", "dp", "sp", "fun", "class", "val", "var", "import"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Quick Autocomplete Assist Bar
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(commonCompletions) { keyword ->
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .onClickHelper {
                            codeText += " $keyword"
                            onContentChange(codeText)
                        }
                ) {
                    Text(
                        text = keyword,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = IdeBluePrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

        // Diagnostic Banner if errors exist
        if (diagnostics.isNotEmpty()) {
            val topDiag = diagnostics.first()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (topDiag.severity == DiagnosticSeverity.ERROR) IdeRedError.copy(alpha = 0.15f)
                        else IdeYellowWarning.copy(alpha = 0.15f)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Diagnostic",
                    tint = if (topDiag.severity == DiagnosticSeverity.ERROR) IdeRedError else IdeYellowWarning,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Line ${topDiag.line}: ${topDiag.message}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Main Editor Surface
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(verticalScroll)
        ) {
            // Line Numbers Gutter
            val lines = codeText.lines()
            Column(
                modifier = Modifier
                    .width(44.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                horizontalAlignment = Alignment.End
            ) {
                lines.forEachIndexed { idx, _ ->
                    Text(
                        text = "${idx + 1}",
                        fontSize = fontSizeSp.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Gray.copy(alpha = 0.6f),
                        lineHeight = (fontSizeSp + 6).sp
                    )
                }
            }

            VerticalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            // Highlighted Code Editor Surface
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .horizontalScroll(horizontalScroll)
                    .padding(8.dp)
            ) {
                BasicTextField(
                    value = codeText,
                    onValueChange = { newText ->
                        codeText = newText
                        onContentChange(newText)
                    },
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = fontSizeSp.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = (fontSizeSp + 6).sp
                    ),
                    cursorBrush = SolidColor(IdeBluePrimary),
                    visualTransformation = { text ->
                        androidx.compose.ui.text.input.TransformedText(
                            highlightSyntax(text.text, tab.language),
                            androidx.compose.ui.text.input.OffsetMapping.Identity
                        )
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("code_editor_text_field")
                )
            }
        }
    }
}

// Extension helper for click in Compose
private fun Modifier.onClickHelper(onClick: () -> Unit): Modifier {
    return this.then(
        Modifier.clickable { onClick() }
    )
}

// Lightweight Tokenizer & Syntax Highlighting for Kotlin, Java, XML, Gradle
private fun highlightSyntax(code: String, language: LanguageType): AnnotatedString {
    return buildAnnotatedString {
        append(code)

        val keywords = when (language) {
            LanguageType.KOTLIN, LanguageType.GRADLE -> listOf(
                "package", "import", "class", "interface", "object", "fun", "val", "var",
                "private", "public", "protected", "override", "return", "if", "else", "when",
                "for", "while", "data", "sealed", "enum", "suspend", "plugins", "dependencies",
                "implementation", "android", "defaultConfig", "buildTypes"
            )
            LanguageType.JAVA -> listOf(
                "package", "import", "public", "private", "protected", "class", "interface",
                "extends", "implements", "void", "int", "boolean", "String", "return", "if",
                "else", "for", "while", "new"
            )
            LanguageType.XML -> listOf("manifest", "application", "activity", "intent-filter", "action", "category", "resources", "string", "uses-permission")
            else -> emptyList()
        }

        // Apply Keyword Styling
        keywords.forEach { keyword ->
            var startIndex = 0
            while (startIndex < code.length) {
                val foundIndex = code.indexOf(keyword, startIndex)
                if (foundIndex == -1) break

                val isWordStart = foundIndex == 0 || !code[foundIndex - 1].isLetterOrDigit()
                val isWordEnd = foundIndex + keyword.length == code.length || !code[foundIndex + keyword.length].isLetterOrDigit()

                if (isWordStart && isWordEnd) {
                    addStyle(
                        style = SpanStyle(color = IdePurpleKeyword, fontWeight = FontWeight.Bold),
                        start = foundIndex,
                        end = foundIndex + keyword.length
                    )
                }
                startIndex = foundIndex + keyword.length
            }
        }
    }
}
