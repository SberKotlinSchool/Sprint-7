package com.firebat.addressbook.filter

import java.time.LocalDateTime
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter(urlPatterns = ["/app/*", "/api/*"])
class AuthFilter : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        request as HttpServletRequest
        if (isAuthenticated(request)) {
            chain!!.doFilter(request, response)
        } else {
            response as HttpServletResponse
            response.sendRedirect("/login")
        }
    }

    private fun isAuthenticated(request: HttpServletRequest): Boolean {
        return !request.cookies.isNullOrEmpty() && request.cookies.any { it.name == "auth1" && LocalDateTime.parse(it.value) > LocalDateTime.now() }
    }
}