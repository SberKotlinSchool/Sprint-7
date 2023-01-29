package ru.morningcake.addressbook.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.HttpMethod
import org.springframework.security.test.context.support.TestExecutionEvent
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.morningcake.addressbook.BaseNotesTest
import java.util.*
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql("classpath:sql/userInit.sql")
internal class UserControllerTest : BaseNotesTest() {

    private val baseUri = "/user/admin_panel"
    private val userId = UUID.fromString("9cb2f4dc-ba70-4fae-bd2a-547de191219a")

    @Test
    @DisplayName("GET /user/admin_panel")
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    fun adminPanel() {
        val mvcResult = GET(baseUri, MockMvcResultMatchers.status().isOk)

        val html = getHtmlResponse(mvcResult)
        assertTrue(html.contains("<td>deleter</td>"))
        assertTrue(html.contains("<td>user</td>"))
    }

    @Test
    @DisplayName("POST /user/admin_panel/filter")
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    fun showUsers() {
        val mvcResult = POST("$baseUri/filter", "filter=del", MockMvcResultMatchers.status().isOk)

        val html = getHtmlResponse(mvcResult)
        assertTrue(html.contains("<td>deleter</td>"))
        assertFalse(html.contains("<td>user</td>"))
    }

    @Test
    @DisplayName("GET /admin_panel/user/{id}/ban & GET /admin_panel/user/{id}/delete_ban")
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    fun userBanAndDeleteBan() {
        GET("$baseUri/user/$userId/ban", MockMvcResultMatchers.status().isOk)
        val mvcResult = GET("$baseUri/user/$userId/delete_ban", MockMvcResultMatchers.status().isOk)

        val html = getHtmlResponse(mvcResult)
        assertTrue(html.contains("<td>deleter</td>"))
        assertTrue(html.contains("<td>user</td>"))
    }

    @ParameterizedTest(name = "#{index} -  {0} {1}")
    @DisplayName("Auth test")
    @MethodSource("appAuthTestArguments")
    fun apiAuthTest(method : HttpMethod, uri : String, content : Any?) {
        val mvcResult = when (method) {
            HttpMethod.GET -> GET(uri, MockMvcResultMatchers.status().isFound) // 302 redirect
            HttpMethod.POST -> POST(uri, writeJson(content!!), MockMvcResultMatchers.status().isFound)
            else -> throw IllegalArgumentException("Неподдерживаемый метод для теста!")
        }
        assertEquals("http://localhost/login", mvcResult.response.getHeader("Location"))
    }

    private fun appAuthTestArguments() : Stream<Arguments> {
        return Stream.of(
            Arguments.of(HttpMethod.GET, baseUri, null),
            Arguments.of(HttpMethod.POST, "$baseUri/filter", "filter=del"),
            Arguments.of(HttpMethod.GET, "$baseUri/user/$userId/ban", null),
            Arguments.of(HttpMethod.GET, "$baseUri/user/$userId/delete_ban", null),
        )
    }
}