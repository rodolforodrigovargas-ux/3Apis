package com.example.a3apis.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a3apis.data.model.UnifiedCard
import com.example.a3apis.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val appRepository: AppRepository = AppRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<OracleUiState>(OracleUiState.Loading)
    val uiState: StateFlow<OracleUiState> = _uiState.asStateFlow()

    private val _favoriteCards = MutableStateFlow<List<UnifiedCard>>(emptyList())
    val favoriteCards: StateFlow<List<UnifiedCard>> = _favoriteCards.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())

    init {
        observarFavoritos()
        loadDailyOracle()
    }

    private fun observarFavoritos() {
        appRepository.favoritosRepository?.let { favRepo ->
            viewModelScope.launch {
                favRepo.obtenerIdsFavoritos().collectLatest { ids ->
                    _favoriteIds.value = ids
                    val currentState = _uiState.value
                    if (currentState is OracleUiState.Success) {
                        _uiState.value = currentState.copy(favoriteIds = ids)
                    }
                }
            }
            viewModelScope.launch {
                favRepo.obtenerFavoritos().collectLatest { favoritos ->
                    _favoriteCards.value = favoritos
                }
            }
        }
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

    fun toggleFavorite(card: UnifiedCard) {
        viewModelScope.launch {
            appRepository.favoritosRepository?.toggleFavorito(card)
        }
    }

    fun filterByCategory(categoryName: String?) {
        val currentState = _uiState.value
        if (currentState is OracleUiState.Success) {
            _uiState.value = currentState.copy(selectedCategoryName = categoryName)
        }
    }
}
