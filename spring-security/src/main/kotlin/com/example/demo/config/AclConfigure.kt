package com.example.demo.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.ehcache.EhCacheFactoryBean
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.acls.AclPermissionCacheOptimizer
import org.springframework.security.acls.AclPermissionEvaluator
import org.springframework.security.acls.domain.*
import org.springframework.security.acls.jdbc.BasicLookupStrategy
import org.springframework.security.acls.jdbc.JdbcMutableAclService
import org.springframework.security.acls.jdbc.LookupStrategy
import org.springframework.security.acls.model.PermissionGrantingStrategy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import javax.sql.DataSource

@EnableCaching
@Configuration
class AclConfigure {

    @Autowired
    lateinit var dataSource: DataSource

    @Bean
    fun aclAuthorizationStrategy(): AclAuthorizationStrategy {
        return AclAuthorizationStrategyImpl(
            SimpleGrantedAuthority("ROLE_ADMIN")
        )
    }

    @Bean
    fun permissionGrantingStrategy(): PermissionGrantingStrategy {
        return DefaultPermissionGrantingStrategy(
            ConsoleAuditLogger()
        )
    }

    @Bean
    fun aclCache(
        cacheFactory: EhCacheFactoryBean,
        permissionGrantingStrategy: PermissionGrantingStrategy,
        authorizationStrategy: AclAuthorizationStrategy
    ): EhCacheBasedAclCache {
        return EhCacheBasedAclCache(
            cacheFactory.getObject(),
            permissionGrantingStrategy,
            authorizationStrategy
        )
    }

    @Bean
    fun aclCacheManager(): EhCacheManagerFactoryBean {
        return EhCacheManagerFactoryBean()
    }

    @Bean
    fun ehCacheFactoryBean(cacheManagerFactory: EhCacheManagerFactoryBean): EhCacheFactoryBean {
        val ehCacheFactoryBean = EhCacheFactoryBean()
        ehCacheFactoryBean.setCacheManager(cacheManagerFactory.getObject()!!)
        ehCacheFactoryBean.setCacheName("aclCache")
        return ehCacheFactoryBean
    }

    @Bean
    fun lookupStrategy(
        aclCache: EhCacheBasedAclCache,
        authorizationStrategy: AclAuthorizationStrategy
    ): LookupStrategy {
        return BasicLookupStrategy(
            dataSource,
            aclCache,
            authorizationStrategy,
            ConsoleAuditLogger()
        )
    }

    @Bean
    fun aclService(lookupStrategy: LookupStrategy, aclCache: EhCacheBasedAclCache): JdbcMutableAclService {
        return JdbcMutableAclService(
            dataSource, lookupStrategy, aclCache
        )
    }

    @Bean
    fun permissionEvaluator(aclService: JdbcMutableAclService): PermissionEvaluator {
        return AclPermissionEvaluator(aclService)
    }

    @Bean
    fun expressionHandlerAcl(
        aclService: JdbcMutableAclService,
        aclPermissionEvaluator: PermissionEvaluator
    ): MethodSecurityExpressionHandler {
        val expressionHandler = DefaultMethodSecurityExpressionHandler()
        expressionHandler.setPermissionEvaluator(aclPermissionEvaluator)
        expressionHandler.setPermissionCacheOptimizer(AclPermissionCacheOptimizer(aclService))
        return expressionHandler
    }

}