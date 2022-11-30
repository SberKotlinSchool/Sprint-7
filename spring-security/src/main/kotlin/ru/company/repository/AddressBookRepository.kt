package ru.company.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.company.model.Client
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger



interface AddressBookRepository {
   fun add(client: Client)
   fun getClients(fio: String?, address: String?, phone: String?, email: String?): List<Client>
   fun getClientById(id: Long): Client?
   fun updateClient(client: Client)
   fun deleteClient(id: Long)
}

@Repository
class AddressBookRepositoryImpl : AddressBookRepository {
   private val addressBook = ConcurrentHashMap<Long, Client>()
   private val idGen: AtomicInteger = AtomicInteger(0)

   override fun add(client: Client) {
      val id = idGen.incrementAndGet()
      addressBook[id.toLong()] = client.copy(id = id.toLong())
   }


   override fun getClients(fio: String?, address: String?, phone: String?, email: String?): List<Client> {
      var result: List<Client> = addressBook.values.toList()
      if (fio != null) result = result.filter { it.fio == fio }
      if (address != null) result = result.filter { it.address == address }
      if (phone != null) result = result.filter { it.phone == phone }
      if (email != null) result = result.filter { it.email == email }
      return result
   }

   override fun getClientById(id: Long): Client? = addressBook[id]

   override fun updateClient(client: Client) {
      addressBook[client.id!!] = client
   }

   override fun deleteClient(id: Long) {
      addressBook.remove(id)
   }
}