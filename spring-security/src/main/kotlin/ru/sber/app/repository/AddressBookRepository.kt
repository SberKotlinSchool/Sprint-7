package ru.sber.app.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.app.entity.ADDRESSBOOK

interface AddressBookRepository: JpaRepository<ADDRESSBOOK, Long>