package ru.sber.AddressBook.Services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.sber.AddressBook.Model.CustomerModel
import ru.sber.AddressBook.Repositories.CustomerRepository

@Service
class AddressBookService @Autowired constructor(val customerRepository: CustomerRepository){
    fun addRow(customerModel: CustomerModel) {
        customerRepository.create(customerModel)
    }

    fun getAllRows() : Map<Int,CustomerModel> = customerRepository.getAll()

    fun getRowsByFio(firstName: String?) : Map<Int, CustomerModel> =
        customerRepository.getCustomersWithParam(firstName)

    fun getById(id: Int) : CustomerModel? = customerRepository.getById(id)

    fun updateRow(id: Int, customerModel: CustomerModel) {
        customerRepository.update(id, customerModel)
    }

    fun deleteRow(id: Int) = customerRepository.delete(id)
}