package ru.sber.config

import mu.KotlinLogging
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan("ru.sber")
class RepositoryConfig {

//    @Bean
//    @Primary
//    // TODO когда подключим реальную базу, эти конфиги переедут в тесты
//    fun initUserDatabase(@Autowired repository: UserRepository): UserRepository {
//        repository.save(User(login = "login", password =  "password"))
//        repository.save(User(login = "u", password =  "u"))
//        log.info { "${repository.getUserByLogin("login")} ${repository.getUserByLogin("u")} " }
//        return repository
//    }

    companion object {
        val log = KotlinLogging.logger {}
    }
}