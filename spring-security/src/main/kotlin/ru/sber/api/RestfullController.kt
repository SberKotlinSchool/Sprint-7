package ru.sber.api

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.sber.api.dto.ListRecordResponse
import ru.sber.api.dto.RecordRequest
import ru.sber.api.dto.RecordResponse
import ru.sber.domain.RecordService

@RestController
@RequestMapping("/api")
class RestfullController(val service: RecordService) {

  @GetMapping(path = ["/list"], produces = [MediaType.APPLICATION_JSON_VALUE])
  fun getList(@AuthenticationPrincipal user: User): ResponseEntity<ListRecordResponse> =
    ResponseEntity.ok(
      ListRecordResponse(
        if (user.authorities.contains(SimpleGrantedAuthority("ROLE_ADMIN")))
          service.getAllRecord()
        else
          service.getAllRecord(user.username)
      )
    )

  @GetMapping(path = ["/search"], produces = [MediaType.APPLICATION_JSON_VALUE])
  fun getSearchList(
    @RequestParam query: String,
    @AuthenticationPrincipal user: User
  ): ResponseEntity<ListRecordResponse> =
    ResponseEntity.ok(
      ListRecordResponse(
        if (user.authorities.contains(SimpleGrantedAuthority("ROLE_ADMIN")))
          service.search(query)
        else
          service.search(query, user.username)
      )
    )

  @PostMapping(
    path = ["/add"],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun addRecord(@RequestBody requestRecord: RecordRequest, @AuthenticationPrincipal user: User): ResponseEntity<Any> {
    service.createRecord(requestRecord.toRecord(), user.username)
    return ResponseEntity.ok().build()
  }

  @GetMapping(path = ["{id}/view"], produces = [MediaType.APPLICATION_JSON_VALUE])
  fun viewRecord(@AuthenticationPrincipal user: User, @PathVariable id: Long): ResponseEntity<RecordResponse> {
    val record = if (user.authorities.contains(SimpleGrantedAuthority("ROLE_ADMIN")))
      service.getRecordById(id)
    else
      service.getRecordById(id, user.username)
    return record?.let {
      ResponseEntity.ok(RecordResponse.fromRecord(it))
    } ?: ResponseEntity.notFound().build()
  }

  @PostMapping(
    path = ["/{id}/edit"],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun editRecord(
    @RequestBody requestRecord: RecordRequest,
    @AuthenticationPrincipal user: User,
    @PathVariable id: Long
  ): ResponseEntity<Any> {
    service.modifyRecord(requestRecord.toRecord(id, user.username))
    return ResponseEntity.ok().build()
  }

  @PostMapping(path = ["/{id}/delete"])
  fun deleteRecord(@PathVariable id: Long): ResponseEntity<Any> {
    service.deleteRecordById(id)
    return ResponseEntity.ok().build()
  }
}