package com.example.a3apis.ui

import com.example.a3apis.data.model.UnifiedCard

sealed interface OracleUiState {
    data object Loading : OracleUiState
    data class Success(
        val cards: List<UnifiedCard>,
        val favoriteIds: Set<String> = emptySet(),
        val selectedCategoryName: String? = null
    ) : OracleUiState
    data class Error(val message: String) : OracleUiState
}
