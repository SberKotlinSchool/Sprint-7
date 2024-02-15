package ru.sber.addressbook.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.sber.addressbook.dto.AddressModel
import ru.sber.addressbook.dto.ResponseDTO
import ru.sber.addressbook.repository.RoleRepository
import ru.sber.addressbook.repository.UserRepository
import ru.sber.addressbook.service.AddressService
import ru.sber.addressbook.service.CustomUserDetailsService

@RestController
@RequestMapping("/api")
class AddressControllerApi(
    private val addressService: AddressService,
    private val roleRepository: RoleRepository,
    private val userRepository: UserRepository,
    private val customUserDetailsService: CustomUserDetailsService
) {
  @GetMapping("/list")
  fun getListOfAllAddresses() = ResponseDTO(
      data = roleRepository.findAll()//addressService.getAddressList().values.toList()
  )

  @GetMapping("/ddd")
  fun saveRole() = ResponseDTO(
      data = userRepository.findAll() //addressService.getAddressList().values.toList()
  )

  @GetMapping("/zzz")
  fun saveUser() = ResponseDTO(
      data = customUserDetailsService.loadUserByUsername("admin")
  )
  @GetMapping("{id}")
  fun getAddress(@PathVariable id: Int) = ResponseDTO(
      data = addressService.getAddressById(id)
  )

  @PutMapping("{id}/update")
  fun updateAddress(@PathVariable id: Int, @RequestBody address: AddressModel) = ResponseDTO(
      data = addressService.updateAddress(id, address)
  )

  @DeleteMapping("{id}/delete")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun removeAddress(@PathVariable id: Int) {
    addressService.deleteAddressById(id)
  }
}