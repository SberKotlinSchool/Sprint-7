package com.example.demo.entity

//import com.fasterxml.jackson.annotation.JsonIgnore
//import jakarta.persistence.*
//import org.springframework.security.core.userdetails.UserDetails
//
//@Entity
//@Table(name = "users")
//data class User(
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    val id: Long? = null,
//    private val username: String? = null,
//    private var password: String? = null,
//    @JsonIgnore
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//        name = "users_roles",
//        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
//        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
//    )
//    val roles: List<Role> = emptyList()
//):UserDetails {
//    override fun getAuthorities() = this.roles
//    override fun getPassword() = this.password!!
//    override fun getUsername()  = this.username!!
//    override fun isAccountNonExpired(): Boolean = true
//    override fun isAccountNonLocked(): Boolean = true
//    override fun isCredentialsNonExpired(): Boolean = true
//    override fun isEnabled(): Boolean = true
//}