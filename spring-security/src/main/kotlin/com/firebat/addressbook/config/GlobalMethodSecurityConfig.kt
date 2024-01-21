package com.firebat.addressbook.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration

@EnableGlobalMethodSecurity(prePostEnabled = true)
class GlobalMethodSecurityConfig : GlobalMethodSecurityConfiguration() {
    @Autowired
    lateinit var aclExpressionHandler: MethodSecurityExpressionHandler

    override fun createExpressionHandler(): MethodSecurityExpressionHandler = aclExpressionHandler
}