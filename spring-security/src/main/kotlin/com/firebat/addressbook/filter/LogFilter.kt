package com.firebat.addressbook.filter

import org.slf4j.Logger
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter(urlPatterns = ["/*"])
class LogFilter(private val logger: Logger) : Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest
        response as HttpServletResponse
        logger.info("Session ${request.session.id}: processing request method ${request.method} on URI ${request.requestURI}, status: ${response.status}")
        chain.doFilter(request, response)
    }
}