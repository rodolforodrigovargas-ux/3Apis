package com.example.a3apis.data.repository

import com.example.a3apis.data.local.FavoritoDao
import com.example.a3apis.data.mapper.UnifiedMapper
import com.example.a3apis.data.model.UnifiedCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavoritosRepository(
    private val favoritoDao: FavoritoDao
) {

    fun obtenerFavoritos(): Flow<List<UnifiedCard>> {
        return favoritoDao.obtenerTodosLosFavoritos().map { listaEntities ->
            listaEntities.map { UnifiedMapper.mapFavoritoEntityToUnified(it) }
        }
    }

    fun obtenerIdsFavoritos(): Flow<Set<String>> {
        return favoritoDao.obtenerIdsFavoritos().map { it.toSet() }
    }

    suspend fun toggleFavorito(card: UnifiedCard) = withContext(Dispatchers.IO) {
        val esFav = favoritoDao.esFavorito(card.id)
        if (esFav) {
            favoritoDao.eliminarPorId(card.id)
        } else {
            favoritoDao.insertar(UnifiedMapper.mapUnifiedToFavoritoEntity(card))
        }
    }
}
