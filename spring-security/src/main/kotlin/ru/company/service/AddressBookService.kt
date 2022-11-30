package ru.company.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.company.model.Client
import ru.company.repository.AddressBookRepository
import java.util.*



interface AddressBookService {
    fun add(client: Client)
    fun getClients(fio: String?, address: String?, phone: String?, email: String?): List<Client>
    fun getClientById(id: Long): Optional<Client>
    fun updateClient(client: Client)
    fun deleteClient(id: Long)
}

@Service
class AddressBookServiceImpl @Autowired constructor(val repository: AddressBookRepository) : AddressBookService {
    override fun add(client: Client) {
        repository.add(client)
    }

    override fun getClients(fio: String?, address: String?, phone: String?, email: String?): List<Client> =
        repository.getClients(fio, address, phone, email)


    override fun getClientById(id: Long): Optional<Client> = Optional.ofNullable(repository.getClientById(id))

    override fun updateClient(client: Client) {
        repository.updateClient(client)
    }

    override fun deleteClient(id: Long) {
        repository.deleteClient(id)
    }


}