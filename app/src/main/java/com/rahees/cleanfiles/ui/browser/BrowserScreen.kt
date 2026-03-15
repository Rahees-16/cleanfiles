package com.rahees.cleanfiles.ui.browser

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DriveFileMove
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahees.cleanfiles.data.model.SortField
import com.rahees.cleanfiles.data.model.SortOption
import com.rahees.cleanfiles.data.model.SortOrder
import com.rahees.cleanfiles.ui.components.BreadcrumbBar
import com.rahees.cleanfiles.ui.components.FileGridItem
import com.rahees.cleanfiles.ui.components.FileListItem
import com.rahees.cleanfiles.util.FileUtils
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserScreen(
    initialPath: String,
    onNavigateBack: () -> Unit,
    onNavigateToSearch: () -> Unit,
    viewModel: BrowserViewModel = hiltViewModel()
) {
    val currentPath by viewModel.currentPath.collectAsStateWithLifecycle()
    val files by viewModel.files.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val selectedPaths by viewModel.selectedPaths.collectAsStateWithLifecycle()
    val sortOption by viewModel.sortOption.collectAsStateWithLifecycle()
    val isGridView by viewModel.isGridView.collectAsStateWithLifecycle()
    val clipboard by viewModel.clipboard.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val isSelectionMode = selectedPaths.isNotEmpty()

    var showSortMenu by remember { mutableStateOf(false) }
    var showNewFolderDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(initialPath) {
        viewModel.navigateTo(initialPath)
    }

    BackHandler {
        if (isSelectionMode) {
            viewModel.clearSelection()
        } else if (!viewModel.navigateUp()) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSelectionMode) {
                        Text("${selectedPaths.size} selected")
                    } else {
                        Text(File(currentPath).name.ifEmpty { "Storage" })
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (isSelectionMode) {
                                viewModel.clearSelection()
                            } else if (!viewModel.navigateUp()) {
                                onNavigateBack()
                            }
                        }
                    ) {
                        Icon(
                            if (isSelectionMode) Icons.Filled.Close else Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (isSelectionMode) {
                        IconButton(onClick = { viewModel.selectAll() }) {
                            Icon(Icons.Filled.SelectAll, contentDescription = "Select All")
                        }
                    } else {
                        IconButton(onClick = onNavigateToSearch) {
                            Icon(Icons.Filled.Search, contentDescription = "Search")
                        }
                        Box {
                            IconButton(onClick = { showSortMenu = true }) {
                                Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
                            }
                            SortDropdownMenu(
                                expanded = showSortMenu,
                                currentSort = sortOption,
                                onDismiss = { showSortMenu = false },
                                onSortSelected = {
                                    viewModel.setSortOption(it)
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (isSelectionMode) {
                BottomAppBar {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(onClick = {
                            viewModel.copyToClipboard(selectedPaths.toList())
                        }) {
                            Icon(Icons.Filled.ContentCopy, contentDescription = "Copy")
                        }
                        IconButton(onClick = {
                            viewModel.cutToClipboard(selectedPaths.toList())
                        }) {
                            Icon(Icons.Filled.ContentCut, contentDescription = "Cut")
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete")
                        }
                        IconButton(onClick = { viewModel.zipSelected() }) {
                            Icon(Icons.Filled.FolderZip, contentDescription = "Zip")
                        }
                        if (selectedPaths.size == 1) {
                            IconButton(onClick = {
                                val path = selectedPaths.first()
                                val file = File(path)
                                FileUtils.shareFile(context, file)
                            }) {
                                Icon(Icons.Filled.Share, contentDescription = "Share")
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            if (!isSelectionMode) {
                Column(horizontalAlignment = Alignment.End) {
                    if (clipboard != null) {
                        SmallFloatingActionButton(
                            onClick = { viewModel.paste() }
                        ) {
                            Icon(Icons.Filled.ContentPaste, contentDescription = "Paste")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    FloatingActionButton(
                        onClick = { showNewFolderDialog = true }
                    ) {
                        Icon(Icons.Filled.CreateNewFolder, contentDescription = "New Folder")
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            BreadcrumbBar(
                path = currentPath,
                onNavigateToPath = { viewModel.navigateTo(it) }
            )

            HorizontalDivider()

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (files.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "This folder is empty",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else if (isGridView) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(files, key = { it.path }) { fileItem ->
                        FileGridItem(
                            fileItem = fileItem,
                            isSelected = selectedPaths.contains(fileItem.path),
                            isSelectionMode = isSelectionMode,
                            onClick = {
                                if (isSelectionMode) {
                                    viewModel.toggleSelection(fileItem.path)
                                } else if (fileItem.isDirectory) {
                                    viewModel.navigateTo(fileItem.path)
                                    viewModel.onFileOpened(fileItem.path, fileItem.name)
                                } else {
                                    openFile(context, fileItem)
                                    viewModel.onFileOpened(fileItem.path, fileItem.name)
                                }
                            },
                            onLongClick = { viewModel.toggleSelection(fileItem.path) }
                        )
                    }
                }
            } else {
                LazyColumn {
                    items(files, key = { it.path }) { fileItem ->
                        FileListItem(
                            fileItem = fileItem,
                            isSelected = selectedPaths.contains(fileItem.path),
                            isSelectionMode = isSelectionMode,
                            onClick = {
                                if (isSelectionMode) {
                                    viewModel.toggleSelection(fileItem.path)
                                } else if (fileItem.isDirectory) {
                                    viewModel.navigateTo(fileItem.path)
                                    viewModel.onFileOpened(fileItem.path, fileItem.name)
                                } else {
                                    openFile(context, fileItem)
                                    viewModel.onFileOpened(fileItem.path, fileItem.name)
                                }
                            },
                            onLongClick = { viewModel.toggleSelection(fileItem.path) }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 68.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }

    if (showNewFolderDialog) {
        NewFolderDialog(
            onDismiss = { showNewFolderDialog = false },
            onCreate = { name ->
                viewModel.createFolder(name)
                showNewFolderDialog = false
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete") },
            text = { Text("Are you sure you want to delete ${selectedPaths.size} item(s)?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteSelected()
                    showDeleteDialog = false
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    showRenameDialog?.let { path ->
        val fileName = File(path).name
        RenameDialog(
            currentName = fileName,
            onDismiss = { showRenameDialog = null },
            onRename = { newName ->
                viewModel.renameFile(path, newName)
                showRenameDialog = null
            }
        )
    }
}

@Composable
private fun SortDropdownMenu(
    expanded: Boolean,
    currentSort: SortOption,
    onDismiss: () -> Unit,
    onSortSelected: (SortOption) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        SortField.entries.forEach { field ->
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = currentSort.field == field,
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            when (field) {
                                SortField.NAME -> "Name"
                                SortField.SIZE -> "Size"
                                SortField.DATE -> "Date"
                                SortField.TYPE -> "Type"
                            }
                        )
                    }
                },
                onClick = {
                    val newOrder = if (currentSort.field == field) {
                        if (currentSort.order == SortOrder.ASCENDING) SortOrder.DESCENDING
                        else SortOrder.ASCENDING
                    } else {
                        SortOrder.ASCENDING
                    }
                    onSortSelected(SortOption(field, newOrder))
                }
            )
        }
        HorizontalDivider()
        DropdownMenuItem(
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = currentSort.order == SortOrder.ASCENDING,
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ascending")
                }
            },
            onClick = { onSortSelected(currentSort.copy(order = SortOrder.ASCENDING)) }
        )
        DropdownMenuItem(
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = currentSort.order == SortOrder.DESCENDING,
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Descending")
                }
            },
            onClick = { onSortSelected(currentSort.copy(order = SortOrder.DESCENDING)) }
        )
    }
}

@Composable
private fun NewFolderDialog(
    onDismiss: () -> Unit,
    onCreate: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Folder") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Folder name") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = { if (name.isNotBlank()) onCreate(name.trim()) },
                enabled = name.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun RenameDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onRename: (String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rename") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("New name") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = { if (name.isNotBlank()) onRename(name.trim()) },
                enabled = name.isNotBlank() && name != currentName
            ) {
                Text("Rename")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

private fun openFile(context: android.content.Context, fileItem: com.rahees.cleanfiles.data.model.FileItem) {
    try {
        val file = File(fileItem.path)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val mimeType = fileItem.mimeType ?: "*/*"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        // No app to handle this file type
    }
}
