package com.patorika.feature_controller.data.repository

import app.cash.sqldelight.db.SqlDriver
import com.patorika.feature_controller.data.database.createTestDatabaseDriver
import com.patorika.feature_controller.presentation.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.presentation.elements.buttons.square.model.SquareButtonModel
import com.patorika.feature_controller.presentation.model.ControllerModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue

class ControllerRepositoryTest {
    private lateinit var driver: SqlDriver
    private lateinit var repository: ControllerRepositoryImpl

    @BeforeTest
    fun setup() {
        driver = createTestDatabaseDriver()
        repository = ControllerRepositoryImpl(driver)
    }

    @Test
    fun `check save controller success`() = runTest {
        val model = createFakeController()

        repository.insertController(model)

        var resultList = emptyList<ControllerModel>()
        repository.getAllControllers().first { result ->
            result.onSuccess { resultList = it }
            true
        }

        assertContentEquals(listOf(model), resultList)
    }

    @Test
    fun `check controllers order by name`() = runTest {
        val inputList = mutableListOf<ControllerModel>().apply {
            repeat(10) { number ->
                add(createFakeController(name = "Controller $number"))
            }
        }.shuffled()

        inputList.forEach {
            repository.insertController(it)
        }

        var resultList = emptyList<ControllerModel>()
        repository.getAllControllers().first { result ->
            result.onSuccess { resultList = it }
            true
        }

        assertContentEquals(inputList.sortedBy { it.name }, resultList)
    }

    @Test
    fun `check controller removal success`() = runTest {
        val model = createFakeController()
        var resultList = emptyList<ControllerModel>()

        //insert element
        repository.insertController(model)

        repository.getAllControllers().first { result ->
            result.onSuccess { resultList = it }
            true
        }

        assertContentEquals(listOf(model), resultList)

        //remove element
        repository.removeController(model.id)

        repository.getAllControllers().first { result ->
            result.onSuccess { resultList = it }
            true
        }

        assertContentEquals(emptyList(), resultList)
    }

    @Test
    fun `get non existent controller returns failure`() = runTest {
        val model = createFakeController()
        val result = repository.getControllerById(model.id)
        assertTrue(result.isFailure)
    }

    @AfterTest
    fun teardown() {
        driver.close()
    }

    private fun createFakeController(
        name: String = "",
        elementsCount: Int = 1
    ): ControllerModel {
        return ControllerModel(
            name = name,
            elements = if (elementsCount > 0) {
                mutableListOf<ControllerElementModel>().apply {
                    repeat(elementsCount) {
                        add(SquareButtonModel())
                    }
                }
            } else emptyList()
        )
    }
}
