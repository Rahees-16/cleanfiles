package com.rahees.cleanfiles.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rahees.cleanfiles.data.model.FileCategory
import com.rahees.cleanfiles.util.FileUtils

fun getCategoryIcon(category: FileCategory): ImageVector {
    return when (category) {
        FileCategory.IMAGES -> Icons.Filled.Image
        FileCategory.VIDEOS -> Icons.Filled.VideoFile
        FileCategory.AUDIO -> Icons.Filled.AudioFile
        FileCategory.DOCUMENTS -> Icons.Filled.Description
        FileCategory.APKS -> Icons.Filled.Android
        FileCategory.DOWNLOADS -> Icons.Filled.Download
        FileCategory.OTHER -> Icons.Filled.MoreHoriz
    }
}

@Composable
fun CategoryCard(
    category: FileCategory,
    fileCount: Int,
    totalSize: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = getCategoryIcon(category),
                contentDescription = category.displayName,
                modifier = Modifier.size(32.dp),
                tint = getCategoryColor(category)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = category.displayName,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "$fileCount items",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = FileUtils.formatFileSize(totalSize),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
