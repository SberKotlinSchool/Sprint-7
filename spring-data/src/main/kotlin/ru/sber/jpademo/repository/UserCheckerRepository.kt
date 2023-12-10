package ru.sber.jpademo.repository

import ru.sber.jpademo.entity.UserChecker
import org.springframework.data.jpa.repository.JpaRepository

interface UserCheckerRepository : JpaRepository<UserChecker, Long>