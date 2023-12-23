import com.example.adresbook.dto.LoginDto
import com.example.adresbook.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@Controller
class LoginController(
    private val userDetailsService: UserService
) {

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @PostMapping("/login")
    fun login(loginDto: LoginDto): String {
        println(loginDto)
        userDetailsService.loginUser(loginDto)
        return "redirect:/app/v1/list"
    }
}