package ru.nikitazar.domain.usecase

import ru.nikitazar.domain.repository.DrugsRepository
import javax.inject.Inject

class GetPageUseCase @Inject constructor(
    private val repository: DrugsRepository
) {
    suspend fun execute(search: String) = repository.getPage(search)
}