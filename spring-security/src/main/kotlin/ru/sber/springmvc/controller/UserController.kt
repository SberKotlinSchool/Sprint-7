package ru.sber.springmvc.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import ru.sber.springmvc.model.Address
import ru.sber.springmvc.model.FindUserDTO
import ru.sber.springmvc.model.User
import ru.sber.springmvc.model.UserEditDTO
import ru.sber.springmvc.service.UserService

@Controller
class UserController @Autowired constructor(private val userService: UserService) {

    @GetMapping("/")
    fun userListView(model: Model): String {
        val users = userService.getUserList()
        model.addAttribute("users", users)
        return "users"
    }

    @GetMapping("/user/search")
    fun userFindView(model: Model): String {
        val findUserDTO = FindUserDTO()
        model.addAttribute("findUser", findUserDTO)
        return "find-user"
    }

    @PostMapping("/user/find")
    fun userFindView(@ModelAttribute findUser: FindUserDTO, model: Model): String {
        val searchIsEmpty = !StringUtils.hasLength(findUser.name) &&
            !StringUtils.hasLength(findUser.login) &&
            !StringUtils.hasLength(findUser.address)

        return if (searchIsEmpty)
                "redirect:/user/search"
            else {
                model.addAttribute("users", userService.findUser(findUser))
                "find-user-results"
            }
    }

    @GetMapping("/user/create")
    fun userCreateView(model: Model): String {
        val user = UserEditDTO()
        model.addAttribute("user", user)
        return "user"
    }

    @GetMapping("/user/edit/{id}")
    fun userEditView(@PathVariable id: Long, model: Model): String {
        return userService.findById(id)
            .map {
                val user = UserEditDTO()
                user.id = it.id
                user.name = it.name
                user.login = it.login
                user.address = it.address?.addr
                model.addAttribute("user", user)
                "user"
            }
            .orElse("users")
    }

    @PostMapping("/user/save")
    fun userSave(@ModelAttribute user: UserEditDTO): String {
        userService.saveUser(User().also { userToSave ->
            userToSave.id = user.id
            userToSave.name = user.name.toString()
            userToSave.login = user.login.toString()
            userToSave.password = user.password.toString()
            userToSave.address = Address(0, user.address, userToSave)
        })
        return "redirect:/"
    }

    @GetMapping("/user/delete/{id}")
    @PreAuthorize("hasRole('DELETE_USER')")
    fun userDelete(@PathVariable id: Long) : String {
        userService.deleteUser(id)
        return "redirect:/"
    }
}
