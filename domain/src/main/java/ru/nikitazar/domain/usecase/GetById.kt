package ru.nikitazar.domain.usecase

import ru.nikitazar.domain.repository.DrugsRepository
import javax.inject.Inject

class GetById @Inject constructor(
    private val repository: DrugsRepository
) {
    suspend fun execute(id: Int) = repository.getById(id)
}