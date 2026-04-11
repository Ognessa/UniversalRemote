package com.patorika.feature_list_presentation.usecase

import com.patorika.feature_controller.domain.repository.ControllerRepository
import com.patorika.feature_controller.presentation.elements.buttons.square.model.SquareButtonModel
import com.patorika.feature_controller.presentation.model.ControllerModel
import com.patorika.feature_controller.presentation.model.ControllerOrientation
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.matcher.matches
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DuplicateControllerUseCaseTest {

    private val repository = mock<ControllerRepository>()
    private val useCase = DuplicateControllerUseCase(repository)

    @Test
    fun `execute returns success when repository succeeds`() = runTest {
        everySuspend { repository.insertController(any()) } returns Result.success(Unit)

        val result = useCase.execute(ControllerModel(elements = listOf(SquareButtonModel())))

        assertTrue(result.isSuccess)
    }

    @Test
    fun `execute returns failure when repository fails`() = runTest {
        val exception = RuntimeException("insert failed")
        everySuspend { repository.insertController(any()) } returns Result.failure(exception)

        val result = useCase.execute(ControllerModel(elements = listOf(SquareButtonModel())))

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `duplicate assigns new id to controller and all its elements`() = runTest {
        everySuspend { repository.insertController(any()) } returns Result.success(Unit)
        val original = ControllerModel(elements = listOf(SquareButtonModel(), SquareButtonModel()))

        useCase.execute(original)

        verifySuspend {
            repository.insertController(
                matches { duplicated ->
                    duplicated.id != original.id &&
                            duplicated.elements.size == original.elements.size &&
                            duplicated.elements.zip(original.elements)
                                .all { (dup, orig) -> dup.id != orig.id }
                }
            )
        }
    }

    @Test
    fun `duplicate preserves name, canvasRatio and orientation`() = runTest {
        everySuspend { repository.insertController(any()) } returns Result.success(Unit)
        val original = ControllerModel(
            name = "My Controller",
            canvasRatio = 1.5f,
            orientation = ControllerOrientation.LANDSCAPE,
            elements = listOf(SquareButtonModel()),
        )

        useCase.execute(original)

        verifySuspend {
            repository.insertController(
                matches { duplicated ->
                    duplicated.name == original.name &&
                            duplicated.canvasRatio == original.canvasRatio &&
                            duplicated.orientation == original.orientation
                }
            )
        }
    }
}