package ru.sber

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import ru.sber.dao.CommonRepository
import ru.sber.entity.Department
import ru.sber.entity.DepartmentAddress
import ru.sber.entity.Employee
import ru.sber.entity.Position
import java.time.LocalDate

class EntityServiceTest() : EmbeddedDbTest() {

    @Test
    fun `test department create-delete`() {

        val sessionFactory = getSessionFactory()
        sessionFactory.use {
            val departmentRepository = CommonRepository(sessionFactory)
            val dep = Department(
                    fullName = "ИТ",
                    businessArea = "разработка"
            )
            departmentRepository.save(dep)

            var found = departmentRepository.find(dep.id, Department::class.java)
            assertEquals("ИТ", dep.fullName)
            assertEquals("разработка", dep.businessArea)

            departmentRepository.delete(found)
            found = departmentRepository.find(dep.id, Department::class.java)
            assertNull(found)
        }

    }


    @Test
    fun `create all entities`() {
        val sessionFactory = getSessionFactory()
        sessionFactory.use {
            val repository = CommonRepository(sessionFactory)
            val dep = Department(fullName = "ИТ", businessArea = "разработка")
            repository.save(dep)

            val addr = DepartmentAddress(fullAddress = "Москва", postalCode = 100500, department = dep)
            repository.save(addr)

            val pos = Position(name = "Программист", department = dep)
            repository.save(pos)

            val employee = Employee(name = "Аноним",
                    employmentDate = LocalDate.now(),
                    tableNum = "1000",
                    grade = 10,
                    position = pos)
            repository.save(employee)

            dep.departmentAddress = addr
            dep.positionList = mutableListOf(pos)
            pos.employeeList = mutableListOf(employee)

            //Проверка, что все связи сохранились.
            val foundEmp = repository.find(employee.id, Employee::class.java)
            assertEquals("Аноним", foundEmp?.name)

            assertEquals("Программист", foundEmp?.position?.name)
            assertEquals("ИТ", foundEmp?.position?.department?.fullName)
            assertEquals("Москва", foundEmp?.position?.department?.departmentAddress?.fullAddress)


            repository.delete(addr)
            //Проверка, что все связанные entity были удалены
            assertNull(repository.find(dep.id, Department::class.java))
            assertNull(repository.find(addr.id, DepartmentAddress::class.java))
            assertNull(repository.find(pos.id, Position::class.java))
            assertNull(repository.find(employee.id, Employee::class.java))
        }


    }

}