package ru.sbrf.addressbook.core

import org.apache.commons.lang3.StringUtils
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PostFilter
import org.springframework.security.acls.domain.BasePermission
import org.springframework.security.acls.domain.GrantedAuthoritySid
import org.springframework.security.acls.domain.ObjectIdentityImpl
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.acls.jdbc.JdbcMutableAclService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

interface AddressBookService {

    fun addEmployee(employee: Employee, userName: String): Employee

    fun getEmployeeById(id: Long): Employee?

    fun updateEmployee(employee: Employee): Employee

    fun removeEmployeeById(id: Long)

    fun getEmployees(
        firstName: String?,
        lastName: String?,
        phone: String?,
        email: String?
    ): List<Employee>


}

@Service
class AddressBookServiceImpl @Autowired constructor(val repository: EmployeeRepository, val aclService: JdbcMutableAclService) :
    AddressBookService {

    override fun addEmployee(employee: Employee, userName: String): Employee {
        val persisted  = repository.save(employee)
        createAclForOwner(persisted, userName)
        return persisted;
    }

    override fun getEmployeeById(id: Long): Employee? {
        return repository.findById(id).orElse(null)
    }

    override fun updateEmployee(employee: Employee): Employee {
        val persisted = repository.findById(employee.id).orElseThrow { EntityNotFoundException("not found") }
        BeanUtils.copyProperties(employee, persisted)
        repository.save(persisted)
        return persisted
    }

    override fun removeEmployeeById(id: Long) {
        repository.deleteById(id)
    }

    @PostFilter("hasRole('ADMIN') or hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'DELETE')")
    override fun getEmployees(
        firstName: String?,
        lastName: String?,
        phone: String?,
        email: String?
    ): List<Employee> {
        return repository.findAll()
            .filter { StringUtils.isBlank(firstName) || it.firstName == firstName }
            .filter { StringUtils.isBlank(lastName) || it.lastName == lastName }
            .filter { StringUtils.isBlank(phone) || it.phone == phone }
            .filter { StringUtils.isBlank(email) || it.email == email }
    }


    /**
     * Создание ACL_OBJECT_ENTRY и набор необходимых ACL_ENTRY.
     * Для личных записей - добавляем DELETE для владельца
     */
    private fun createAclForOwner(
        employee: Employee,
        username: String
    ) {
        val identity = ObjectIdentityImpl(employee)
        val acl = aclService.createAcl(identity)
        acl.insertAce(acl.entries.size, BasePermission.DELETE, PrincipalSid(username), true)
        if (employee.owner == 0L) {
            acl.insertAce(acl.entries.size, BasePermission.READ, GrantedAuthoritySid("ROLE_USER"), true)
        }
        acl.isEntriesInheriting = false
        aclService.updateAcl(acl)
    }

}

@Service
class UserDetailServiceImpl(val repo: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(p0: String?): UserDetails =
        repo.findByUsername(p0!!)?.toUserDetails() ?: throw UsernameNotFoundException("User not found")


}

