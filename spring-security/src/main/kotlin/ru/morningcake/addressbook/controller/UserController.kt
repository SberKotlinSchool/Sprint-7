package ru.morningcake.addressbook.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.morningcake.addressbook.entity.User
import ru.morningcake.addressbook.service.UserService
import ru.morningcake.addressbook.utils.AppUtils
import java.util.*

@Controller
@RequestMapping("/user")
class UserController @Autowired constructor(private val service: UserService, private val appUtils: AppUtils) {

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin_panel")
    fun adminPanel(@AuthenticationPrincipal admin: User, model: Model): String {
        appUtils.addBaseUrlAndUserNameToModel(model, admin.username)
        addAllUsersToModel(admin, model)
        return "admin"
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/admin_panel/filter")
    fun showUsers(@AuthenticationPrincipal self: User, model: Model, filter: String): String {
        val users: List<User> = service.getUsersWithFilter(self, filter)
        appUtils.addBaseUrlAndUserNameToModel(model, self.username)
        model.addAttribute("users", users)
        model.addAttribute("filter", filter)
        return "admin"
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin_panel/user/{id}/ban")
    fun userBan(@AuthenticationPrincipal admin: User, model: Model, @PathVariable id: UUID): String {
        service.userBan(admin, id)
        appUtils.addBaseUrlAndUserNameToModel(model, admin.username)
        addAllUsersToModel(admin, model)
        return "admin"
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin_panel/user/{id}/delete_ban")
    fun deleteUserBan(@AuthenticationPrincipal admin: User, model: Model, @PathVariable id: UUID): String {
        service.deleteUserBan(id)
        appUtils.addBaseUrlAndUserNameToModel(model, admin.username)
        addAllUsersToModel(admin, model)
        return "admin"
    }

    private fun addAllUsersToModel(admin: User, model: Model) {
        val users: List<User> = service.getAllUsersWithoutSelf(admin)
        model.addAttribute("users", users)
    }
}