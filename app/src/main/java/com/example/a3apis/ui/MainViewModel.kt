package com.example.a3apis.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a3apis.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val appRepository: AppRepository = AppRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<OracleUiState>(OracleUiState.Loading)
    val uiState: StateFlow<OracleUiState> = _uiState.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())

    init {
        loadDailyOracle()
    }

    fun loadDailyOracle() {
        viewModelScope.launch {
            _uiState.value = OracleUiState.Loading
            val result = appRepository.getAll3OfEach()
            result.fold(
                onSuccess = { cards ->
                    _uiState.value = OracleUiState.Success(
                        cards = cards,
                        favoriteIds = _favoriteIds.value
                    )
                },
                onFailure = { error ->
                    _uiState.value = OracleUiState.Error(
                        message = error.localizedMessage ?: "Error al conectar con las APIs. Revisa tu conexión."
                    )
                }
            )
        }
    }

    fun toggleFavorite(cardId: String) {
        val currentFavorites = _favoriteIds.value.toMutableSet()
        if (currentFavorites.contains(cardId)) {
            currentFavorites.remove(cardId)
        } else {
            currentFavorites.add(cardId)
        }
        _favoriteIds.value = currentFavorites

        val currentState = _uiState.value
        if (currentState is OracleUiState.Success) {
            _uiState.value = currentState.copy(favoriteIds = currentFavorites)
        }
    }

    fun filterByCategory(categoryName: String?) {
        val currentState = _uiState.value
        if (currentState is OracleUiState.Success) {
            _uiState.value = currentState.copy(selectedCategoryName = categoryName)
        }
    }
}
