package com.example.demo

import com.example.demo.persistance.Country
import com.example.demo.persistance.CountryRepository
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.stream.Stream

@DataJpaTest
class CountryRepositoryTest {

    @Autowired
    private lateinit var repository: CountryRepository

    @ParameterizedTest
    @MethodSource("provideArgsForRepositoryTest")
    fun `find by id should find country`(id: Long, country: Country) {
        val c = repository.findById(id)
        assert(c.get() == country)
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
        fun provideArgsForRepositoryTest(): Stream<Arguments> =
            Stream.of(
                Arguments.of(1L, countries[0]),
                Arguments.of(2L, countries[1]),
                Arguments.of(3L, countries[2]),
                Arguments.of(4L, countries[3]),
                Arguments.of(5L, countries[4]),
            )
    }
}