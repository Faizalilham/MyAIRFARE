package binar.finalproject.MyAirFare.ui.activities

import binar.finalproject.MyAirFare.api.AuthEndPoint
import binar.finalproject.MyAirFare.model.login.UserLoginRequest
import binar.finalproject.MyAirFare.model.login.UserLoginResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Call

internal class LoginActivityTest{
    private lateinit var service: AuthEndPoint

    @Before
    fun setUp() {
        service = mockk()
    }

    @Test
    fun getUser(): Unit = runBlocking {
        val response = mockk<Call<UserLoginResponse>>()
        every {
            service.doUserLogin(
                UserLoginRequest(
                    email = "@mail.com",
                    password = "pass"
                )
            )
        } returns response

        val result = service.doUserLogin(
            UserLoginRequest(
                email = "@mail.com",
                password = "pass"
            )
        )

        verify {
            service.doUserLogin(
                UserLoginRequest(
                    email = "@mail.com",
                    password = "pass"
                )
            )
        }
        Assert.assertEquals(result, response)
    }
}

