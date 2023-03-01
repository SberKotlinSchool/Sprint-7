package ru.sber.data.db.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.data.db.entity.Actor

interface ActorRepository: JpaRepository<Actor, Long>