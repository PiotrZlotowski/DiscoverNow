package com.discover.server.service

import com.discover.server.model.User
import com.discover.server.repository.UserRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.core.userdetails.UsernameNotFoundException

const val CORRECT_USER_EMAIL = "abc@gmail.com"
const val INCORRECT_USER_EMAIL = "xyz@gmail.com"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class UserServiceTests {

    @InjectMockKs
    private lateinit var sut: UserService

    @MockK
    private lateinit var userRepository: UserRepository

    @Test
    fun `loadUserByUsername should return appropriate user when invoked`() {
        // GIVEN
        val user = User(email = CORRECT_USER_EMAIL, password = "pwd1", roles = emptySet(), sources = emptyList())
        // AND
        every { userRepository.findByEmail(CORRECT_USER_EMAIL) } returns user

        // WHEN
        val actual = sut.loadUserByUsername(CORRECT_USER_EMAIL)

        // THEN
        then(actual).isNotNull.isEqualTo(user)
        verify { userRepository.findByEmail(CORRECT_USER_EMAIL) }

    }

    @Test
    fun `loadUserByUsername should throw an exception when incorrect user is provided`() {
        // GIVEN
        every { userRepository.findByEmail(INCORRECT_USER_EMAIL) } returns null

        // WHEN
        val execution: () -> User = { sut.loadUserByUsername(INCORRECT_USER_EMAIL) }

        // THEN
        assertThrows<UsernameNotFoundException> { execution() }
        verify { userRepository.findByEmail(INCORRECT_USER_EMAIL) }

    }
}