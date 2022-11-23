package com.waslim.storyapp.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.waslim.storyapp.MainCoroutinesRule
import com.waslim.storyapp.model.Result
import com.waslim.storyapp.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class RegisterRepositoryTest {
    private lateinit var registerRepository: RegisterRepository

    private val name = "Lim"
    private val email = "waslim@gmail.com"
    private val password = "Waslim123"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainCoroutinesRule()

    @Before
    fun setup() {
        val apiService = Module.providesRetrofit()
        registerRepository = RegisterRepository(apiService)
    }

    @Test
    fun `Register failed`() = runTest {
        val actualRegister = registerRepository.registerUser(name, email, password)
        assertTrue(actualRegister is Result.Failure)
    }

    @Test
    fun `Register successful`() = runTest {
        val actualRegister = registerRepository.registerUser(name, "${java.util.UUID.randomUUID()}@gmail.com", password)
        assertTrue(actualRegister is Result.Success)
    }
}