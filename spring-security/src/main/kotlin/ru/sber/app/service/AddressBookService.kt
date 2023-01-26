package ru.sber.app.service

import javassist.NotFoundException
import org.springframework.security.access.prepost.PostFilter
import org.springframework.security.acls.domain.BasePermission
import org.springframework.security.acls.domain.GrantedAuthoritySid
import org.springframework.security.acls.domain.ObjectIdentityImpl
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.acls.jdbc.JdbcMutableAclService
import org.springframework.stereotype.Service
import ru.sber.app.entity.AddressBook
import ru.sber.app.repository.AddressBookRepository

@Service
class AddressBookService(val repo: AddressBookRepository, val aclService: JdbcMutableAclService) {

    /**
     * Выбираем все записи и возвращаем только доступные клиенту
     */
    @PostFilter("hasRole('ADMIN') or hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'DELETE')")
    fun getAddressBook(): Iterable<AddressBook> = repo.findAll()

    fun getById(id: Long): AddressBook = repo.findById(id).get()

    fun remove(id: Long) {
        repo.deleteById(id)
    }

    /**
     * Во время создания так же не забываем создавать сущности ACL
     */
    fun add(draft: AddressBook, username: String): AddressBook {
        val addressBook = repo.save(draft)
        createAclForOwner(addressBook, username)
        return addressBook
    }

    /**
     * Создание ACL_OBJECT_ENTRY и набор необходимых ACL_ENTRY.
     * Для личных контактов - добавляем DELETE для владельца
     * Для публичных - дополнительно даем право READ для ROLE_USER группы
     */
    private fun createAclForOwner(
        addressBook: AddressBook,
        username: String
    ) {
        val identity = ObjectIdentityImpl(addressBook)

        val acl = aclService.createAcl(identity)
        acl.insertAce(acl.entries.size, BasePermission.DELETE, PrincipalSid(username), true)
        if (addressBook.owner == 0L) {
            acl.insertAce(acl.entries.size, BasePermission.READ, GrantedAuthoritySid("ROLE_USER"), true)
        }
        acl.isEntriesInheriting = false

        aclService.updateAcl(acl)
    }

    fun update(addressBook: AddressBook): AddressBook {
        val persisted = repo.findById(addressBook.id).orElseThrow { NotFoundException("not found") }
        persisted.firstName = addressBook.firstName
        persisted.lastName = addressBook.lastName
        persisted.city = addressBook.city
        repo.save(addressBook)
        return persisted
    }
}