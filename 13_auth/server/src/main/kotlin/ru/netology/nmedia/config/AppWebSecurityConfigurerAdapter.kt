package ru.netology.nmedia.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import ru.netology.nmedia.dto.AnonymousUser
import ru.netology.nmedia.filter.AuthTokenFilter
import ru.netology.nmedia.service.UserService

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class AppWebSecurityConfigurerAdapter : WebSecurityConfigurerAdapter() {
    @Autowired
    @Lazy
    lateinit var userService: UserService

    @Bean
    fun passwordEncoder(): PasswordEncoder = SCryptPasswordEncoder()

    override fun configure(web: WebSecurity?) {
        web
            ?.ignoring()
            ?.antMatchers("/images/*", "/avatars/*", "/media/*")
    }

    override fun configure(http: HttpSecurity?) {
        http
            ?.csrf()?.disable()
            ?.exceptionHandling()?.and()
            ?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)?.and()
            ?.addFilterAfter(AuthTokenFilter(userService), BasicAuthenticationFilter::class.java)
            ?.anonymous {
                it.principal(AnonymousUser).authorities(*AnonymousUser.authorities.toTypedArray())
            }
            ?.authorizeRequests()?.anyRequest()?.permitAll()
    }
}