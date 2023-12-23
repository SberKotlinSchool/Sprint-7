import com.example.adresbook.dto.LogonDto
import com.example.adresbook.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@Controller
class LogonController(
    private val userDetailsService: UserService
) {

    @GetMapping("/logon")
    fun login(): String {
        return "logon"
    }

    @PostMapping("/logon")
    fun login(logonDto: LogonDto): String {
        println(logonDto)
        userDetailsService.logonUser(logonDto)
        return "redirect:/app/v1/list"
    }
}