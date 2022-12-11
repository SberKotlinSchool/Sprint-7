package com.example.demo

import com.example.demo.persistance.Country
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ValueSource
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.stream.Stream

@AutoConfigureMockMvc
@SpringBootTest
internal class CountryControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc


    @ParameterizedTest
    @ValueSource(ints = [1, 2, 3, 4, 5])
    fun getOne(id: Int) {
        val expected = countries[id - 1]

        mockMvc.perform(MockMvcRequestBuilders.get("/api/country/$id"))
            .andExpect { MockMvcResultMatchers.status().isOk }
            .andExpect { MockMvcResultMatchers.jsonPath("$.name").value(expected.name) }
    }

    companion object {

        @JvmStatic
        private val countries = arrayOf(
            Country(1, "USA"),
            Country(2, "France"),
            Country(3, "Brazil"),
            Country(4, "Italy"),
            Country(5, "Canada"),
        )

        @JvmStatic
        private fun provideArgsForRepositoryTest() =
            Stream.of(
                Arguments.of(1L, countries[0]),
                Arguments.of(2L, countries[1]),
                Arguments.of(3L, countries[2]),
                Arguments.of(4L, countries[3]),
                Arguments.of(5L, countries[4]),
            )

    }
}