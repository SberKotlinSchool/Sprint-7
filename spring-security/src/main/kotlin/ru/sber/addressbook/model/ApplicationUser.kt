package ru.sber.addressbook.model

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority

@Entity
@Table(name = "users")
class ApplicationUser (
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  val id: Long = 0,

  val name: String,

  val userName: String,

  val email: String,

  val password: String,

  var roles: UserRoles,
)

enum class UserRoles(
    val authorities: Set<UserAuthority>
) {
  ROLE_API(
      setOf(UserAuthority.API_ACCESS)
  ),
  ROLE_APP(
      setOf(UserAuthority.APP_ACCESS)
  ),
  ROLE_ADMIN(
      setOf(UserAuthority.APP_ACCESS, UserAuthority.API_ACCESS)
  );
}

enum class UserAuthority : GrantedAuthority {
  API_ACCESS, APP_ACCESS;

  override fun getAuthority(): String = name
}