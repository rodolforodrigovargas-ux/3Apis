package com.example.a3apis.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.a3apis.data.model.CardCategory
import com.example.a3apis.data.model.UnifiedCard
import com.example.a3apis.ui.OracleUiState
import com.example.a3apis.ui.components.UnifiedCardItem

@Composable
fun ExploreScreen(
    uiState: OracleUiState,
    onFilterSelected: (String?) -> Unit,
    onFavoriteToggle: (UnifiedCard) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "🔍 Explorar la Sabiduría",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Filtra tus cartas por categoría",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        if (uiState is OracleUiState.Success) {
            // Category Filter Chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                FilterChip(
                    selected = uiState.selectedCategoryName == null,
                    onClick = { onFilterSelected(null) },
                    label = { Text("Todos") },
                    shape = CircleShape
                )

                CardCategory.entries.forEach { category ->
                    FilterChip(
                        selected = uiState.selectedCategoryName == category.name,
                        onClick = {
                            if (uiState.selectedCategoryName == category.name) {
                                onFilterSelected(null)
                            } else {
                                onFilterSelected(category.name)
                            }
                        },
                        label = { Text("${category.iconName} ${category.displayName}") },
                        shape = CircleShape
                    )
                }
            }

            val filteredCards = if (uiState.selectedCategoryName == null) {
                uiState.cards
            } else {
                uiState.cards.filter { it.category.name == uiState.selectedCategoryName }
            }

            if (filteredCards.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron elementos en esta categoría.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(items = filteredCards, key = { card -> card.id }) { card ->
                        UnifiedCardItem(
                            card = card,
                            isFavorite = uiState.favoriteIds.contains(card.id),
                            onFavoriteToggle = onFavoriteToggle
                        )
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Carga una lectura en el Inicio para explorar sus cartas.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
