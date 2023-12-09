package ru.sber

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import ru.sber.entity.Department
import ru.sber.entity.DepartmentAddress
import ru.sber.entity.Employee
import ru.sber.entity.Position
import ru.sber.jpa.DepartmentAddressRepository
import ru.sber.jpa.DepartmentRepository
import ru.sber.jpa.EmployeeRepository
import ru.sber.jpa.PositionRepository
import java.time.LocalDate

@DataJpaTest
class EntityServiceTest {

    @Autowired
    private lateinit var departmentRepository: DepartmentRepository

    @Autowired
    private lateinit var departmentAddressRepository: DepartmentAddressRepository

    @Autowired
    private lateinit var employeeRepository: EmployeeRepository

    @Autowired
    private lateinit var positionRepository: PositionRepository

    @Test
    fun `test department create-delete`() {

        val dep = Department(
                fullName = "ИТ",
                businessArea = "разработка"
        )
        departmentRepository.save(dep)

        var found = departmentRepository.findById(dep.id).get()
        assertEquals("ИТ", dep.fullName)
        assertEquals("разработка", dep.businessArea)

        departmentRepository.deleteById(found.id)
        assertTrue(departmentRepository.findById(dep.id).isEmpty)
    }

    @Test
    fun `create all entities`() {

        val dep = Department(fullName = "ИТ", businessArea = "разработка")
        departmentRepository.save(dep)

        val addr = DepartmentAddress(fullAddress = "Москва", postalCode = 100500, department = dep)
        departmentAddressRepository.save(addr)

        val pos = Position(name = "Программист", department = dep)
        positionRepository.save(pos)

        val employee = Employee(name = "Аноним",
                employmentDate = LocalDate.now(),
                tableNum = "1000",
                grade = 10,
                position = pos)
        employeeRepository.save(employee)

        dep.departmentAddress = addr
        dep.positionList = mutableListOf(pos)
        pos.employeeList = mutableListOf(employee)

        //Проверка, что все связи сохранились.
        val foundEmp = employeeRepository.findById(employee.id)
        assertEquals("Аноним", foundEmp.get().name)

        assertEquals("Программист", foundEmp.get().position?.name)
        assertEquals("ИТ", foundEmp.get().position?.department?.fullName)
        assertEquals("Москва", foundEmp.get().position?.department?.departmentAddress?.fullAddress)


        departmentAddressRepository.delete(addr)
        //Проверка, что все связанные entity были удалены
        assertTrue(departmentRepository.findById(dep.id).isEmpty)
        assertTrue(departmentAddressRepository.findById(addr.id).isEmpty)
        assertTrue(positionRepository.findById(pos.id).isEmpty)
        assertTrue(employeeRepository.findById(employee.id).isEmpty)
    }

}