package com.patorika.feature_editor_presentation.usecase

import com.patorika.feature_controller.domain.repository.ControllerRepository
import com.patorika.feature_controller.main.model.ControllerModel

class SaveControllerUseCase(
    private val repository: ControllerRepository,
) {
    suspend fun execute(model: ControllerModel): Result<Unit> = repository.insertController(model)
}
