package ru.sber.springsecurity.filters

import ru.sber.springsecurity.utils.RequestUtils.Companion.LOG
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter(urlPatterns = ["/*"],
    servletNames = ["SpringMvcApplication"])
class LoggerFilter: Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val rq = request as HttpServletRequest
        val rs = response as HttpServletResponse

        LOG.info { "Processing ${rq.method} request: ${request.requestURI}" }
        chain.doFilter(rq, rs)
    }
}