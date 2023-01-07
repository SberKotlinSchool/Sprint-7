package ru.sber.springsecurity.utils

import mu.KotlinLogging
import ru.sber.springsecurity.entities.Note
import java.util.concurrent.ConcurrentHashMap
import javax.servlet.http.Cookie

class RequestUtils {

    companion object {
        const val AUTH = "auth"
        const val USER = "user"
        const val ADMIN = "admin"
        const val DAY_MONTH_HMS = "dd.MM HH:mm:ss"
        val LOG = KotlinLogging.logger {}

        var usersDB = ConcurrentHashMap<String, String>()
        val notes = ArrayList<Note>()

        fun clearCookies(cookies: Array<Cookie>?) {
            if (cookies == null) return
            LOG.info { "Clearing cookies..." }
            for (cookie in cookies) {
                cookie.maxAge = 0
                when (cookie.name) {
                    AUTH -> cookie.value = Long.MAX_VALUE.toString()
                    USER -> cookie.value = ""
                }
            }
        }
    }
}