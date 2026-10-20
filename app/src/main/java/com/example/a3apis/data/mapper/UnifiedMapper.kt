package com.example.a3apis.data.mapper

import com.example.a3apis.data.local.FavoritoEntity
import com.example.a3apis.data.model.CardCategory
import com.example.a3apis.data.model.UnifiedCard
import com.example.a3apis.data.remote.model.AdviceSlipDto
import com.example.a3apis.data.remote.model.PokemonDto
import com.example.a3apis.data.remote.model.TarotCardDto

object UnifiedMapper {

    fun mapPokemonToUnified(dto: PokemonDto): UnifiedCard {
        val formattedName = dto.name.replaceFirstChar { it.uppercase() }
        val artwork = dto.sprites.other?.officialArtwork?.frontDefault
            ?: dto.sprites.frontDefault
        val typeNames = dto.types.map { it.type.name.replaceFirstChar { char -> char.uppercase() } }

        return UnifiedCard(
            id = "pokemon_${dto.id}",
            title = formattedName,
            subtitle = "Pokémon #${dto.id}",
            description = "Altura: ${dto.height / 10.0}m | Peso: ${dto.weight / 10.0}kg",
            imageUrl = artwork,
            category = CardCategory.POKEMON,
            tags = typeNames,
            extraInfo = "Tu compañero en esta lectura"
        )
    }

    fun mapTarotToUnified(card: TarotCardDto): UnifiedCard {
        val imageFilename = card.nameShort.lowercase() + ".jpg"
        val imageUrl = "https://raw.githubusercontent.com/sacred-texts/pkt/master/img/$imageFilename"

        return UnifiedCard(
            id = "tarot_${card.nameShort}",
            title = card.name,
            subtitle = "Arcano ${card.type.replaceFirstChar { it.uppercase() }}",
            description = card.meaningUp,
            imageUrl = imageUrl,
            category = CardCategory.TAROT,
            tags = listOf(card.type, card.value),
            extraInfo = card.desc
        )
    }

    fun mapAdviceToUnified(slip: AdviceSlipDto): UnifiedCard {
        return UnifiedCard(
            id = "advice_${slip.id}",
            title = "Consejo #${slip.id}",
            subtitle = "Reflexión Diaria",
            description = "\"${slip.advice}\"",
            imageUrl = null,
            category = CardCategory.ADVICE,
            tags = listOf("Sabiduría", "Reflexión"),
            extraInfo = "Aplica este mensaje durante tu día"
        )
    }

    fun mapUnifiedToFavoritoEntity(card: UnifiedCard): FavoritoEntity {
        return FavoritoEntity(
            id = card.id,
            title = card.title,
            subtitle = card.subtitle,
            description = card.description,
            imageUrl = card.imageUrl,
            categoryName = card.category.name,
            tags = card.tags.joinToString(","),
            extraInfo = card.extraInfo
        )
    }

    fun mapFavoritoEntityToUnified(entity: FavoritoEntity): UnifiedCard {
        val category = try {
            CardCategory.valueOf(entity.categoryName)
        } catch (e: Exception) {
            CardCategory.ADVICE
        }
        val tagsList = entity.tags.split(",").filter { it.isNotBlank() }

        return UnifiedCard(
            id = entity.id,
            title = entity.title,
            subtitle = entity.subtitle,
            description = entity.description,
            imageUrl = entity.imageUrl,
            category = category,
            tags = tagsList,
            extraInfo = entity.extraInfo
        )
    }
}
