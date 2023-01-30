package ru.sber.astafex.springdata.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.astafex.springdata.entity.Actor

@Repository
interface DirectorRepository : CrudRepository<Actor, Long>