package com.patorika.feature_editor_presentation.usecase

import com.patorika.feature_controller.domain.repository.ControllerRepository
import com.patorika.feature_controller.presentation.model.ControllerModel

class GetControllerByIdUseCase(
    private val repository: ControllerRepository,
) {
    suspend fun execute(id: String): Result<ControllerModel> = repository.getControllerById(id)
}
