package com.rahees.cleanfiles.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rahees.cleanfiles.data.model.FileCategory
import com.rahees.cleanfiles.data.model.StorageInfo
import com.rahees.cleanfiles.ui.theme.ChartBlue
import com.rahees.cleanfiles.ui.theme.ChartCyan
import com.rahees.cleanfiles.ui.theme.ChartGrey
import com.rahees.cleanfiles.ui.theme.ChartOrange
import com.rahees.cleanfiles.ui.theme.ChartPink
import com.rahees.cleanfiles.ui.theme.ChartPurple
import com.rahees.cleanfiles.ui.theme.ChartYellow
import com.rahees.cleanfiles.util.FileUtils

fun getCategoryColor(category: FileCategory): Color {
    return when (category) {
        FileCategory.IMAGES -> ChartBlue
        FileCategory.VIDEOS -> ChartOrange
        FileCategory.AUDIO -> ChartPurple
        FileCategory.DOCUMENTS -> ChartYellow
        FileCategory.APKS -> ChartPink
        FileCategory.DOWNLOADS -> ChartCyan
        FileCategory.OTHER -> ChartGrey
    }
}

@Composable
fun StorageBar(
    storageInfo: StorageInfo,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Internal Storage",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${FileUtils.formatFileSize(storageInfo.usedSpace)} used of ${FileUtils.formatFileSize(storageInfo.totalSpace)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                if (storageInfo.categories.isNotEmpty() && storageInfo.totalSpace > 0) {
                    Row(modifier = Modifier.matchParentSize()) {
                        storageInfo.categories.forEach { (category, size) ->
                            if (size > 0) {
                                val fraction = (size.toFloat() / storageInfo.totalSpace.toFloat())
                                    .coerceIn(0f, 1f)
                                Box(
                                    modifier = Modifier
                                        .weight(fraction.coerceAtLeast(0.001f))
                                        .height(12.dp)
                                        .background(getCategoryColor(category))
                                )
                            }
                        }
                        val freeRatio = storageInfo.freeSpace.toFloat() / storageInfo.totalSpace.toFloat()
                        if (freeRatio > 0) {
                            Box(
                                modifier = Modifier
                                    .weight(freeRatio.coerceAtLeast(0.001f))
                                    .height(12.dp)
                            )
                        }
                    }
                } else if (storageInfo.totalSpace > 0) {
                    val usedFraction = storageInfo.usedSpace.toFloat() / storageInfo.totalSpace.toFloat()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(usedFraction)
                            .height(12.dp)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${FileUtils.formatFileSize(storageInfo.usedSpace)} used",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
                Text(
                    text = "${FileUtils.formatFileSize(storageInfo.freeSpace)} free",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }

            if (storageInfo.categories.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                val visibleCategories = storageInfo.categories.filter { it.value > 0 }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    visibleCategories.entries.take(4).forEach { (category, size) ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(getCategoryColor(category))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = category.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}
