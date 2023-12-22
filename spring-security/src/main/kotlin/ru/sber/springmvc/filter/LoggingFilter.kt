package ru.sber.springmvc.filter

import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory

@WebFilter("/*")
class LoggingFilter : HttpFilter() {
    private companion object {
        private val LOGGER = LoggerFactory.getLogger(LoggingFilter::class.java)
    }

    override fun doFilter(
        servletRequest: ServletRequest?,
        servletResponse: ServletResponse?, filterChain: FilterChain?
    ) {
        LOGGER.info(
            "${(servletRequest as HttpServletRequest).method}: auth: ${
                servletRequest.cookies?.any { it.name == "auth" }
            }"
        )
        filterChain?.doFilter(servletRequest, servletResponse)
    }
}