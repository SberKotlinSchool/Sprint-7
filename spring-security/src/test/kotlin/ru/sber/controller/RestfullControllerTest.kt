package ru.sber.controller

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import ru.sber.api.dto.ListRecordResponse
import ru.sber.api.dto.RecordRequest
import ru.sber.api.dto.RecordResponse
import ru.sber.domain.Record
import ru.sber.domain.RecordRepository


@ActiveProfiles("test-api")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestfullControllerTest {

    @LocalServerPort
    private var port: Int = 80

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var repository: RecordRepository

    fun initRepository(name: String): Record = repository.save(
        Record(
            name = name,
            lastName = "lastname",
            secondName = "secondname",
            phoneNumber = "134354",
            city = "city",
            street = "street",
            houseNumber = 1,
            postcode = 123234,
            username = "api"
        )
    )

    private fun url(path: String): String {
        return "http://localhost:${port}/${path}"
    }

    @BeforeEach
    fun setUp() {
        repository.deleteAll()
        restTemplate = TestRestTemplate(
            TestRestTemplate.HttpClientOption.ENABLE_REDIRECTS
        )
    }

    @Test
    fun getList401ForUnauthorizedUser() {
        val resp = restTemplate.exchange(
            url("api/list"), HttpMethod.GET, null, Any::class.java
        )
        assert(resp.statusCode.is4xxClientError)
    }

    @Test
    fun getListForbiddenForAppUser() {
        val resp = restTemplate.withBasicAuth("app", "app")
            .exchange(url("api/list"), HttpMethod.GET, null, Any::class.java)

        assert(resp.statusCode.is4xxClientError)
    }

    @Test
    fun getListSuccessForAdminUser() {
        val name = "testApiList"
        val record = initRepository(name)

        val resp = restTemplate.withBasicAuth("admin", "admin").exchange(
            url("api/list"), HttpMethod.GET, null, ListRecordResponse::class.java
        )

        assertNotNull(resp.body)
        assertEquals(1, resp.body!!.list.size)
        assertEquals(record, resp.body!!.list.first())
    }

    @Test
    fun getListSuccessForApiUser() {
        val name = "testApiList"
        val record = initRepository(name)

        val resp = restTemplate.withBasicAuth("api", "api").exchange(
            url("api/list"), HttpMethod.GET, null, ListRecordResponse::class.java
        )

        assertNotNull(resp.body)
        assertEquals(1, resp.body!!.list.size)
        assertEquals(record, resp.body!!.list.first())
    }

    @Test
    fun getSearchListSuccess() {
        val name = "testApiSearchList"
        val record = initRepository(name)
        val resp = restTemplate.withBasicAuth("api", "api").exchange(
            url("api/list"), HttpMethod.GET, null, ListRecordResponse::class.java
        )

        assertNotNull(resp.body)
        assertEquals(1, resp.body!!.list.size)
        assertEquals(record, resp.body!!.list.first())
    }

    @Test
    fun addRecordSuccess() {
        val name = "testApiAdd"
        val record = Record(name = name)

        val respAdd = restTemplate.withBasicAuth("api", "api")
            .exchange(
                url("api/add"), HttpMethod.POST, HttpEntity(RecordRequest(record), null), Any::class.java
            )

        assertEquals(HttpStatus.OK, respAdd.statusCode)

        val respList = restTemplate.withBasicAuth("api", "api").exchange(
            url("api/list"), HttpMethod.GET, null, ListRecordResponse::class.java
        )

        assertNotNull(respList.body)
        assertEquals(1, respList.body!!.list.size)
        assertEquals(record.name, respList.body!!.list.first().name)
    }

    @Test
    fun viewRecordSuccess() {
        val name = "testApiView"
        val record = initRepository(name)

        val resp = restTemplate.withBasicAuth("api", "api").exchange(
            url("api/${record.id}/view"), HttpMethod.GET, null, RecordResponse::class.java
        )

        assertNotNull(resp.body)
        assertEquals(RecordResponse.fromRecord(record), resp.body!!)
    }

    @Test
    fun editRecordSuccess() {
        val name = "testApi"
        val record = initRepository(name)
        val newName = "testApiEdit"
        val recordWithNewName = record.copy(name = newName)

        val respEdit = restTemplate.withBasicAuth("api", "api").exchange(
            url("api/${record.id}/edit"),
            HttpMethod.POST,
            HttpEntity(RecordRequest(recordWithNewName)),
            Any::class.java)

        assertEquals(HttpStatus.OK, respEdit.statusCode)

        val respList = restTemplate.withBasicAuth("api", "api").exchange(
            url("api/list"),
            HttpMethod.GET,
            null,
            ListRecordResponse::class.java)

        assertNotNull(respList.body)
        assertEquals(1, respList.body!!.list.size)
        assertEquals(recordWithNewName, respList.body!!.list.first())
    }

    @Test
    fun deleteRecordSuccess() {
        val name = "testApiDelete"
        val record = initRepository(name)

        val respEdit = restTemplate.withBasicAuth("api", "api").exchange(
            url("api/${record.id}/delete"),
            HttpMethod.POST,
            null,
            Any::class.java)

        assertEquals(HttpStatus.OK, respEdit.statusCode)

        val respList = restTemplate.withBasicAuth("api", "api").exchange(
            url("api/list"),
            HttpMethod.GET,
            null,
            ListRecordResponse::class.java)

        assertNotNull(respList.body)
        assertEquals(0, respList.body!!.list.size)
    }
}