package ru.sber.mvc.repositories

import ru.sber.mvc.models.Address

interface AddressBookRepoControllable {

    // просмотр записей и поиск если будет переданы query параметры запроса
    fun getList(name: String?, phone: String?):List<Address>

    // просмотр конкретной записи
    fun getById(id: Int): Address?

    // вставить запись
    fun insert(row: Address)

    // редактировать запись
    fun update(row: Address)

    // удалить запись
    fun delete(id: Int)
}