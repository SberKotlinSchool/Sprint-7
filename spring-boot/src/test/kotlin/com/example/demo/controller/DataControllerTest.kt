package com.example.demo.controller

import com.example.demo.persistance.DataEntity
import com.example.demo.persistance.DataEntityRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
internal class DataControllerTest {

    @Autowired
    private lateinit var repository: DataEntityRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun getData() {
        // given
        val dataEntity = DataEntity(name = "test entity")
        repository.save(dataEntity)
        
        // when
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/app/data?id=${dataEntity.id}"))

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(dataEntity.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(dataEntity.name))
    }
}