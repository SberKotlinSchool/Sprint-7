package com.sbuniver.homework

import com.sbuniver.homework.repository.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    lateinit var repository: UserRepository

    @Test
    fun getByName(){
        val user = repository.findByUsername("admin").get()
        assert(user.username == "admin")
    }

}

