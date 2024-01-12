package ru.sber.filter

import org.slf4j.Logger
import javax.servlet.FilterChain
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RequestLogFilter(private val logger: Logger) : HttpFilter() {
    override fun doFilter(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        logger.info("${request.method}  > ${request.requestURL}")
        chain.doFilter(request, response)
    }
}