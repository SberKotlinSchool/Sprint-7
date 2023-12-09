package ru.sber.domain

interface RecordService {
  fun createRecord(record: Record, username: String)
  fun modifyRecord(record: Record)
  fun getRecordById(id: Long): Record?
  fun getRecordById(id: Long, username: String): Record?
  fun getAllRecord(username: String): List<Record>
  fun getAllRecord(): List<Record>
  fun deleteRecordById(id: Long)
  fun search(query: String): List<Record>
  fun search(query: String, username: String): List<Record>
}