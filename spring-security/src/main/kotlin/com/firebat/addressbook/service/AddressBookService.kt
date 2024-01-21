package com.firebat.addressbook.service

import com.firebat.addressbook.model.Entry
import org.springframework.security.acls.domain.BasePermission
import org.springframework.security.acls.domain.GrantedAuthoritySid
import org.springframework.security.acls.domain.ObjectIdentityImpl
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.acls.jdbc.JdbcMutableAclService
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Service
class AddressBookService(val aclService: JdbcMutableAclService) {
    private var database: ConcurrentHashMap<Long, Entry> = ConcurrentHashMap()
    private var id: AtomicLong = AtomicLong(0)

    init {
        database[0] = Entry(0, "Unknown", "InitialAddress")
        database[1] = Entry(1, "admin", "InitialAddress")
        database[2] = Entry(2, "user", "InitialAddress")
        database[3] = Entry(3, "user_api", "InitialAddress")
        database[4] = Entry(4, "user_api_delete", "InitialAddress")
        id = AtomicLong(4)
    }

    fun addEntry(entry: Entry): Long {
        val entryId = id.incrementAndGet()
        entry.id = entryId
        database[entryId] = entry
        createAclForOwner(entry, entry.name)
        return entryId
    }

    fun getEntries(query: String?): Set<Map.Entry<Long, Entry>> {
        if (!query.isNullOrEmpty()) {
            return database.entries.apply { filter { it.value.address.startsWith(query, true) } }
        }
        return database.entries
    }

    fun findEntryById(id: Long): Entry? {
        return database[id]
    }

    fun editEntry(id: Long, entry: Entry): Entry? {
        database[id] ?: return null
        database[id] = entry
        return entry
    }

    fun deleteEntry(id: Long): Entry? {
        val result = database[id] ?: return null
        database.remove(id)
        return result
    }

    private fun createAclForOwner(
        entry: Entry,
        name: String
    ) {
        val identity = ObjectIdentityImpl(entry)

        val acl = aclService.createAcl(identity)
        // FIXME не удается сохранить новые sid в ACL: Transaction must be running
        acl.insertAce(acl.entries.size, BasePermission.DELETE, PrincipalSid(name), true)
        if (entry.name == "Unknown") { // считаю это условием публичной записи
            acl.insertAce(
                acl.entries.size,
                BasePermission.DELETE,
                GrantedAuthoritySid("ROLE_USER"), // FIXME не удалось осуществить на пользователе с группой ROLE_USER, нужны дополнительные связи в БД?
                true
            )
        }
        acl.isEntriesInheriting = false // не наследуем права
        aclService.updateAcl(acl)
    }
}