package com.patorika.feature_list_presentation.usecase

import com.patorika.feature_controller.domain.repository.ControllerRepository
import com.patorika.feature_controller.presentation.ext.generateControllerId
import com.patorika.feature_controller.presentation.model.ControllerModel

class DuplicateControllerUseCase(
    private val repository: ControllerRepository,
) {
    suspend fun execute(model: ControllerModel): Result<Unit> {
        val newController =
            model.copy(
                id = generateControllerId(),
                elements = model.elements.map { element -> element.createElementWithNewId() },
            )
        return repository.insertController(newController)
    }
}
