package ru.sber.addressbook.repository

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Repository
import ru.sber.addressbook.dto.AddressModel
import java.util.concurrent.ConcurrentHashMap


object GlobalState {
  private var value = 0;

  fun getNextInt(): Int = value++
}

var addresses: MutableMap<Int, AddressModel> = ConcurrentHashMap()
@Repository
class AddressRepository(
) {

  final fun saveAddress(address: AddressModel) = GlobalState.getNextInt()
      .let {
        addresses[it] = address.apply { id = it }
        addresses[it]
      }

  fun updateAddressById(id: Int, address: AddressModel): AddressModel? {
    addresses[id] = address.apply { this.id = id }
    return addresses[id]
  }

  fun deleteAddressById(id: Int) = addresses.remove(id)

  fun getAddressById(id: Int) = addresses[id]

  fun getAllAdresses() = addresses

}