package com.example.service

import Player
import com.example.repository.PlayerRepository
import org.springframework.stereotype.Service

@Service
class PlayerService(private val playerRepository: PlayerRepository) {

    fun createPlayer(player: Player): Player = playerRepository.save(player)

    fun getPlayerById(id: Long): Player = playerRepository.findById(id).orElse(null)

    fun updatePlayer(id: Long, newName: String) {
        playerRepository.findById(id).let {
            if (it.isPresent) {
                it.get().apply {
                    this.name = newName
                }
            }
        }
    }

    fun deletePlayer(id: Long) = playerRepository.deleteById(id)
}