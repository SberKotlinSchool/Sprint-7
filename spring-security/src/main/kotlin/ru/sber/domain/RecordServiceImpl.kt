package ru.sber.domain

import org.springframework.stereotype.Service

@Service
class RecordServiceImpl(private val repository: RecordRepository) : RecordService {

    override fun createRecord(record: Record, username: String) {
        record.username = username
        repository.save(record)
    }

    override fun modifyRecord(record: Record) {
        repository.save(record)
    }

    override fun getRecordById(id: Long): Record? = repository.findById(id).orElse(null)

    override fun getRecordById(id: Long, username: String): Record? =
        repository.findByIdAndUsername(id, username)

    override fun getAllRecord(username: String): List<Record> = repository.findAllByUsername(username)

    override fun getAllRecord(): List<Record> = repository.findAll().toList()

    override fun deleteRecordById(id: Long) {
        repository.deleteById(id)
    }

    override fun search(query: String): List<Record> =
        repository.findByNameContainingIgnoreCase(query)

    override fun search(query: String, username: String): List<Record> =
        repository.findByNameContainingIgnoreCase(query, username)
}