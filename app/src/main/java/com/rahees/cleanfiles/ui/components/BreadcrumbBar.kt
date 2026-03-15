package com.rahees.cleanfiles.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BreadcrumbBar(
    path: String,
    onNavigateToPath: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val segments = buildPathSegments(path)

    LaunchedEffect(path) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Row(
        modifier = modifier
            .horizontalScroll(scrollState)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        segments.forEachIndexed { index, segment ->
            if (index > 0) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }

            val isLast = index == segments.size - 1
            Text(
                text = segment.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isLast) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isLast) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier
                    .clickable(enabled = !isLast) {
                        onNavigateToPath(segment.path)
                    }
                    .padding(horizontal = 4.dp, vertical = 8.dp)
            )
        }
    }
}

private data class PathSegment(val name: String, val path: String)

private fun buildPathSegments(path: String): List<PathSegment> {
    val segments = mutableListOf(PathSegment("Storage", "/storage/emulated/0"))

    val normalizedPath = path.removePrefix("/storage/emulated/0")
    if (normalizedPath.isNotEmpty() && normalizedPath != "/") {
        val parts = normalizedPath.removePrefix("/").split("/")
        var currentPath = "/storage/emulated/0"
        parts.forEach { part ->
            if (part.isNotEmpty()) {
                currentPath = "$currentPath/$part"
                segments.add(PathSegment(part, currentPath))
            }
        }
    }

    return segments
}
