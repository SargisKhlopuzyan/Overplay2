package com.sargis.khlopuzyan.overplay.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sargis.khlopuzyan.overplay.MainCoroutineRule
import com.sargis.khlopuzyan.overplay.repository.OverplayRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Sargis Khlopuzyan on 3/30/2022.
 */
@ExperimentalCoroutinesApi
class OverplayViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: OverplayViewModel

    private val overplayRepositoryMock = mockk<OverplayRepository>(relaxed = true)

    @Before
    fun setup() {
        viewModel = OverplayViewModel(overplayRepositoryMock)
    }

    @After
    fun teardown() {
        clearMocks(overplayRepositoryMock)
    }

    @Test
    fun `in case rotation degree is smaller then -30, the text size should be 12`() {
        // The sessionEventValues is random value taken from real device when rotation degree is smaller then -30
        val sessionEventValues = floatArrayOf(0.57313144f, -0.343461f, 0.37411144f, 0.6431139f, 0.0f)
        every { overplayRepositoryMock.calculateDeviceZAxisRotationDegree(sessionEventValues) }  returns -32.0f

        viewModel.deviceSensorChanged(sessionEventValues)
        val textSize = viewModel.stateFlowTextSize.value
        assertThat(textSize).isEqualTo(12f)

        verify(exactly = 1) { overplayRepositoryMock.calculateDeviceZAxisRotationDegree(sessionEventValues) }
    }

    @Test
    fun `in case rotation degree is between -30 and 30, the text size should be 16`() {
        // The sessionEventValues is random value taken from real device when rotation degree is between -30 and 30
        val sessionEventValues = floatArrayOf(0.57313144f, -0.343461f, 0.37411144f, 0.6431139f, 0.0f)
        every { overplayRepositoryMock.calculateDeviceZAxisRotationDegree(sessionEventValues) }  returns -3.0f

        viewModel.deviceSensorChanged(sessionEventValues)
        val textSize = viewModel.stateFlowTextSize.value
        assertThat(textSize).isEqualTo(16f)

        verify(exactly = 1) { overplayRepositoryMock.calculateDeviceZAxisRotationDegree(sessionEventValues) }
    }

    @Test
    fun `in case rotation degree is greater then 30, the text size should be 20`() {
        // The sessionEventValues is random value taken from real device when rotation degree is greater then 30
        val sessionEventValues = floatArrayOf(0.57313144f, -0.343461f, 0.37411144f, 0.6431139f, 0.0f)
        every { overplayRepositoryMock.calculateDeviceZAxisRotationDegree(sessionEventValues) }  returns 30.1f

        viewModel.deviceSensorChanged(sessionEventValues)
        val textSize = viewModel.stateFlowTextSize.value
        assertThat(textSize).isEqualTo(20f)

        verify(exactly = 1) { overplayRepositoryMock.calculateDeviceZAxisRotationDegree(sessionEventValues) }
    }

    @Test
    fun `ui is updated on subsequent sensor changes`() = runBlocking {
        val sessionEventValues = floatArrayOf(0.5f, -0.32f, 0.374f, 0.643f, 0.0f)
        every { overplayRepositoryMock.calculateDeviceZAxisRotationDegree(sessionEventValues) } returns 30.1f

        viewModel.stateFlowTextSize.test {
            assertThat(awaitItem()).isEqualTo(0)

            viewModel.deviceSensorChanged(sessionEventValues)
            assertThat(awaitItem()).isEqualTo(20f)

            every { overplayRepositoryMock.calculateDeviceZAxisRotationDegree(sessionEventValues) } returns 20f
            viewModel.deviceSensorChanged(sessionEventValues)
            assertThat(awaitItem()).isEqualTo(16f)
        }

        verify(exactly = 2) { overplayRepositoryMock.calculateDeviceZAxisRotationDegree(sessionEventValues) }
    }

    @Test
    fun onStopTest() = runBlocking {
        val lifecycleOwner = mockk<LifecycleOwner>(relaxed = true)

        viewModel.onStop(lifecycleOwner)

        coVerify(exactly = 1) { overplayRepositoryMock.saveCurrentDateTime() }
    }

    @Test
    fun onStart_sessionNotSavedYet_does_nothing_Test() = runBlocking {
        val lifecycleOwner = mockk<LifecycleOwner>(relaxed = true)

        coEvery { overplayRepositoryMock.getLastSavedDateTime() } returns null

        viewModel.onStart(lifecycleOwner)

        viewModel.stateFlowSessionCount.test {
            assertThat(awaitItem()).isEqualTo(0)
        }
    }

    @Test
    fun onStart_updates_sessionCount_Test() = runBlocking {
//        val lifecycleOwner = mockk<LifecycleOwner>(relaxed = true)
//
//        coEvery { overplayRepositoryMock.getLastSavedDateTime() } returns null
//
//        viewModel.onStart(lifecycleOwner)
//
//        viewModel.stateFlowSessionCount.test {
//            assertThat(awaitItem()).isEqualTo(0)
//        }
    }


}