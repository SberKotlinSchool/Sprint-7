package com.example.adressbook.persistence.repository

import com.example.adressbook.persistence.entity.AddressUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface AddressUserRepository : JpaRepository<AddressUser, Long> {

    @Modifying
    @Transactional
    @Query("update AddressUser au set au.password=:newPassword where au.userName=:userName")
    fun changePasswordForUser(
        @Param("newPassword") newPassword: String,
        @Param("userName") userName: String
    )

    @Modifying
    @Transactional
    fun deleteByUserName(userName: String)

    fun findAddressUserByUserName(userName: String): AddressUser?

    fun existsByUserName(userName: String): Boolean
}