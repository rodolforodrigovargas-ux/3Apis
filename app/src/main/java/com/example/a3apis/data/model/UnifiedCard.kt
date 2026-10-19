package com.example.a3apis.data.model

enum class CardCategory(
    val displayName: String,
    val iconName: String
) {
    POKEMON("Pokémon Guardián", "⚡"),
    TAROT("Carta Astral", "🎴"),
    ADVICE("Consejo Sabio", "💡")
}

data class UnifiedCard(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val imageUrl: String? = null,
    val category: CardCategory,
    val tags: List<String> = emptyList(),
    val extraInfo: String? = null
)
