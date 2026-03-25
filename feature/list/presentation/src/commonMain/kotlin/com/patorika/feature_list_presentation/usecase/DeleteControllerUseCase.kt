package com.patorika.feature_list_presentation.usecase

import com.patorika.feature_controller.domain.repository.ControllerRepository

class DeleteControllerUseCase(
    private val repository: ControllerRepository,
) {
    suspend fun execute(id: String): Result<Unit> = repository.removeController(id)
}
