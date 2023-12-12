package ru.shadowsith.addressbook.controllers


import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import ru.shadowsith.addressbook.dto.Record
import ru.shadowsith.addressbook.repositories.AddressBookRepository
import ru.shadowsith.addressbook.repositories.UserRepository
import java.net.URI
import java.util.*


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddressBookApiControllerTest {
    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var addressBookRepository: AddressBookRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    @Disabled
    fun addRecordTest() {
        val path = "/api/address-book"
        val record = Record(name = "qwe", address = "address", phone = "123")

        val actualResponse = restTemplate.exchange(
           URI("http://localhost:${port}$path"),
            HttpMethod.POST,
            HttpEntity(record, createHeadersAuthorization("adminapi","admin")),
            Record::class.java)
        val actualRecord = actualResponse.body
        assertNotNull(actualResponse)
        assertEquals(HttpStatus.CREATED, actualResponse.statusCode)
        assertEquals(record.copy(id = actualRecord?.id, createDataTime = actualRecord?.createDataTime!!), actualRecord)
    }

    @Test
    @Disabled
    fun getRecordTest() {
        val path = "/api/address-book"
        val recordOne = Record(name = "qwe", address = "address", phone = "123")
        addressBookRepository.save(recordOne)
        val recordTwo = Record(name = "asd", address = "asd", phone = "42332")
        addressBookRepository.save(recordTwo)
        val records = addressBookRepository.findAll().toList()

        val actualResponse = restTemplate.exchange(
            URI("http://localhost:${port}$path"),
            HttpMethod.GET,
            HttpEntity(null,createHeaders("adminapi","admin")),
            object : ParameterizedTypeReference<List<Record>>() {})
        val actualRecords = actualResponse.body!!
        assertNotNull(actualResponse)
        assertEquals(HttpStatus.OK, actualResponse.statusCode)
        assertEquals(records, actualRecords)
        assertEquals(2, actualRecords.size)
    }

    @Test
    @Disabled
    fun getRecordByNameTest() {
        val path = "/api/address-book?name=qwe11"
        val record = Record(name = "qwe11", address = "address", phone = "123")
        addressBookRepository.save(record)

        val records = addressBookRepository.findAllByName("qwe11")

        val actualResponse = restTemplate.exchange(
            URI("http://localhost:${port}$path"),
            HttpMethod.GET,
            HttpEntity(null, createHeaders("adminapi","admin")),
            object : ParameterizedTypeReference<List<Record>>() {})
        val actualRecords = actualResponse.body!!
        assertNotNull(actualResponse)
        assertEquals(HttpStatus.OK, actualResponse.statusCode)
        assertEquals(records, actualRecords)
        assertEquals(1, actualRecords.size)
    }
    @Test
    @Disabled
    fun getRecordByIdTest() {

        val record = Record(name = "qwe", address = "address", phone = "123")
        val id = addressBookRepository.save(record).id!!
        val path = "/api/address-book/$id"
        val expectedRecord = addressBookRepository.findById(id)

        val actualResponse = restTemplate.exchange(
            URI("http://localhost:${port}$path"),
            HttpMethod.GET,
            HttpEntity(null, createHeaders("adminapi","admin")),
            Record::class.java)
        val actualRecord = actualResponse.body!!
        assertNotNull(actualResponse)
        assertEquals(HttpStatus.OK, actualResponse.statusCode)
        assertEquals(expectedRecord, actualRecord)

    }

    @Test
    @Disabled
    fun changeRecordTest() {
        var record = Record(name = "qwe", address = "address", phone = "123")
        record = addressBookRepository.save(record)
        val expectedRecord = record.copy(name = "rtet", phone = "321")
        val path = "/api/address-book/${record.id}"

        val actualResponse = restTemplate.exchange(
            URI("http://localhost:${port}$path"),
            HttpMethod.PUT,
            HttpEntity(expectedRecord, createHeaders("adminapi","admin")),
            Record::class.java)
        val actualRecord = actualResponse.body!!
        assertNotNull(actualResponse)
        assertEquals(HttpStatus.OK, actualResponse.statusCode)
        assertEquals(expectedRecord, actualRecord)
    }

    @Test
    @Disabled
    fun deleteRecordTest() {
        var record = Record(name = "qwe", address = "address", phone = "123")
        record = addressBookRepository.save(record)

        val path = "/api/address-book/${record.id}"

        val actualResponse = restTemplate.exchange(
            URI("http://localhost:${port}$path"),
            HttpMethod.DELETE,
            HttpEntity(null, createHeaders("adminapi","admin")),
            Record::class.java)
        val actualRecord = actualResponse.body!!

        val expectedResult = addressBookRepository.findById(record.id!!)
        assertNull(expectedResult)
        assertNotNull(actualResponse)
        assertEquals(HttpStatus.OK, actualResponse.statusCode)
        assertEquals(record, actualRecord)
    }

    private fun createHeaders(username: String, password: String) : HttpHeaders {
        val login = restTemplate.postForEntity("/login", HttpEntity(mapOf("username" to username, "password" to password), HttpHeaders()), String::class.java)
        val headers = HttpHeaders()
        headers.add(HttpHeaders.COOKIE,login.headers["Set-Cookie"]!![0])
        return headers
    }

    private fun createHeadersAuthorization(username: String, password: String):  HttpHeaders {

        val headers = object :HttpHeaders() {
            init {
                val auth = "$username:$password"
                val encodedAuth = Base64.getEncoder().encodeToString(auth.toByteArray(Charsets.US_ASCII))
                val authHeader = "Basic $encodedAuth"
                set("Authorization", authHeader)
            }
        }
        return headers
    }

}
