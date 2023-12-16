package com.example

import Player
import com.example.repository.PlayerRepository
import org.hibernate.cfg.Configuration

fun main() {
    val configuration = Configuration().configure("hibernate.cfg.xml")
    val sessionFactory = configuration.buildSessionFactory()

    sessionFactory.use {
        val playerRepository = PlayerRepository(it)

        val player = Player(name = "Hugo")
        val playerId = playerRepository.createPlayer(player)

        val retrievedPlayer = playerRepository.getPlayerById(playerId)

        retrievedPlayer?.name = "Raze"
        if (retrievedPlayer != null) {
            playerRepository.updatePlayer(retrievedPlayer)
        }

        retrievedPlayer?.let {
            playerRepository.deletePlayer(it)
        }
    }

    sessionFactory.close()
}