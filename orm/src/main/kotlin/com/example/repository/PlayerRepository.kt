package com.example.repository

import Player
import org.hibernate.SessionFactory

class PlayerRepository(private val sessionFactory: SessionFactory) {

    fun createPlayer(player: Player): Long {
        sessionFactory.openSession().use {
            it.beginTransaction()
            val playerId = it.save(player) as Long
            it.transaction.commit()
            return playerId
        }
    }

    fun getPlayerById(id: Long): Player? {
        sessionFactory.openSession().use {
            return it.get(Player::class.java, id)
        }
    }

    fun updatePlayer(player: Player) {
        sessionFactory.openSession().use {
            it.beginTransaction()
            it.update(player)
            it.transaction.commit()
        }
    }

    fun deletePlayer(player: Player) {
        sessionFactory.openSession().use {
            it.beginTransaction()
            it.delete(player)
            it.transaction.commit()
        }
    }
}