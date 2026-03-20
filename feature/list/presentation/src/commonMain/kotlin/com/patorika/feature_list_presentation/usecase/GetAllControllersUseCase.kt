package com.patorika.feature_list_presentation.usecase

import com.patorika.feature_controller.domain.repository.ControllerRepository
import com.patorika.feature_controller.main.model.ControllerModel
import kotlinx.coroutines.flow.Flow

class GetAllControllersUseCase(
    private val repository: ControllerRepository,
) {
    fun execute(): Flow<List<ControllerModel>> = repository.getAllControllers()
}
