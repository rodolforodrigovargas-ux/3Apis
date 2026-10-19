package com.example.a3apis.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a3apis.data.model.CardCategory
import com.example.a3apis.data.model.UnifiedCard
import com.example.a3apis.ui.OracleUiState
import com.example.a3apis.ui.components.UnifiedCardItem
import com.example.a3apis.ui.theme._3apisTheme

@Composable
fun HomeScreen(
    uiState: OracleUiState,
    onRefreshClick: () -> Unit,
    onFavoriteToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // App Header
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "✨ El Oráculo Chill",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "\"Tu energía y compañero para el día de hoy\"",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        // Main Content depending on state
        when (uiState) {
            is OracleUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Consultando a los astros y las APIs...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            is OracleUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "⚠️ Oops!",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.message,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        OutlinedButton(onClick = onRefreshClick) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            is OracleUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.cards, key = { it.id }) { card ->
                        UnifiedCardItem(
                            card = card,
                            isFavorite = uiState.favoriteIds.contains(card.id),
                            onFavoriteToggle = onFavoriteToggle
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        // Generate New Reading Button
                        Button(
                            onClick = onRefreshClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = "🔮 Generar Nueva Lectura",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    _3apisTheme {
        HomeScreen(
            uiState = OracleUiState.Success(
                cards = listOf(
                    UnifiedCard(
                        id = "tarot_1",
                        title = "The Star (La Estrella)",
                        subtitle = "Arcano Mayor",
                        description = "Esperanza, inspiración y serenidad para guiar tus decisiones.",
                        category = CardCategory.TAROT,
                        tags = listOf("Mayor", "Esperanza"),
                        extraInfo = "Tu carta guía de hoy"
                    ),
                    UnifiedCard(
                        id = "pokemon_251",
                        title = "Celebi",
                        subtitle = "Pokémon #251",
                        description = "Altura: 0.6m | Peso: 5.0kg",
                        category = CardCategory.POKEMON,
                        tags = listOf("Planta", "Psíquico"),
                        extraInfo = "Tu compañero en esta lectura"
                    ),
                    UnifiedCard(
                        id = "advice_1",
                        title = "Consejo #1",
                        subtitle = "Reflexión Diaria",
                        description = "\"No te preocupes por el futuro, disfruta el presente un paso a la vez.\"",
                        category = CardCategory.ADVICE,
                        tags = listOf("Sabiduría", "Reflexión"),
                        extraInfo = "Aplica este mensaje durante tu día"
                    )
                )
            ),
            onRefreshClick = {},
            onFavoriteToggle = {}
        )
    }
}
