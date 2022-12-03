package com.example.demo.service

import com.example.demo.persistance.Author
import com.example.demo.persistance.AuthorRepository
import javassist.NotFoundException
import org.springframework.security.access.prepost.PostFilter
import org.springframework.security.acls.domain.BasePermission
import org.springframework.security.acls.domain.GrantedAuthoritySid
import org.springframework.security.acls.domain.ObjectIdentityImpl
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.acls.jdbc.JdbcMutableAclService
import org.springframework.stereotype.Service

@Service
class AuthorService(val repo: AuthorRepository, val aclService: JdbcMutableAclService) {
    
    @PostFilter("hasRole('ADMIN') or hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'DELETE')")
    fun getAuthors(): Iterable<Author> = repo.findAll()

    fun getByUserId(id: Long): Iterable<Author> = repo.findByOwner(id)

    fun getById(id: Long): Author = repo.findById(id).get()

    fun remove(id: Long) {
        repo.deleteById(id)
    }

    fun add(draft: Author, username: String): Author {
        val author = repo.save(draft)
        createAclForOwner(author, username)
        return author
    }

    private fun createAclForOwner(
        author: Author,
        username: String
    ) {
        val identity = ObjectIdentityImpl(author)

        val acl = aclService.createAcl(identity)
        acl.insertAce(acl.entries.size, BasePermission.DELETE, PrincipalSid(username), true)
        if (author.owner == 0L) {
            acl.insertAce(acl.entries.size, BasePermission.READ, GrantedAuthoritySid("ROLE_USER"), true)
        }
        acl.isEntriesInheriting = false

        aclService.updateAcl(acl)
    }

    fun update(author: Author): Author {
        val persisted = repo.findById(author.id).orElseThrow { NotFoundException("not found") }
        persisted.secondName = author.secondName
        persisted.firstName = author.firstName
        repo.save(author)
        return persisted
    }
}