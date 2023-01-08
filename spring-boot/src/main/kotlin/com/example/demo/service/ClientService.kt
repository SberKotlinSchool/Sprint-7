package com.example.demo.service

import com.example.demo.persistance.ClientRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class ClientService() {

    lateinit var repository: ClientRepository
        @Autowired set

    fun getPhoneNumberById(id: Long) = repository.getById(id).phoneNumber
}