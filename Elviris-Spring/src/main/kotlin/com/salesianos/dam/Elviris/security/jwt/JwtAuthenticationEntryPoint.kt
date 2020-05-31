package com.salesianos.dam.Elviris.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationEntryPoint (
        val mapper : ObjectMapper
)  : AuthenticationEntryPoint {


    override fun commence(request: HttpServletRequest?, response: HttpServletResponse?, authException: AuthenticationException?) {

        response?.status = 401
        response?.contentType = "application/json"

        response?.writer?.println(authException?.message)


    }

}