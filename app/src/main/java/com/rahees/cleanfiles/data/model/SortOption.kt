package com.rahees.cleanfiles.data.model

enum class SortField {
    NAME, SIZE, DATE, TYPE
}

enum class SortOrder {
    ASCENDING, DESCENDING
}

data class SortOption(
    val field: SortField = SortField.NAME,
    val order: SortOrder = SortOrder.ASCENDING
)
