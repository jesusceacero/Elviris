package com.salesianos.dam.Elviris.security.jwt

import com.salesianos.dam.Elviris.model.User
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.*

@Component
class JwtTokenProvider() {

    companion object {
        const val TOKEN_HEADER = "Authorization"
        const val TOKEN_PREFIX = "Bearer "
        const val TOKEN_TYPE = "JWT"
    }

    private val jwtSecreto : String = "mJI.w|g!kCv(5bLr0A@\"wTC,N9mNM]Dd^19h0[?!KB1~I~kfA(,;T<S][_Pm_v(asdfghasdfg"
    private val jwtDuracionTokenEnSegundos : Int = 864000

    private val parser = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtSecreto.toByteArray())).build()

    private val logger : Logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)

    fun generateToken(authentication : Authentication) : String {
        val user : User = authentication.principal as User
        val tokenExpirationDate = Date(System.currentTimeMillis() + (jwtDuracionTokenEnSegundos * 1000))
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(jwtSecreto.toByteArray()), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", TOKEN_TYPE)
                .setSubject(user.id.toString())
                .setExpiration(tokenExpirationDate)
                .setIssuedAt(Date())
                .claim("fullname", user.fullName)
                .claim("roles", user.roles.joinToString())
                .compact()
    }

    fun getUserIdFromJWT(token: String): UUID = UUID.fromString(parser.parseClaimsJws(token).body.subject)

    fun validateToken(token : String) : Boolean {
        try {
            parser.parseClaimsJws(token)
            return true
        } catch (ex : Exception) {
            with(logger) {
                when (ex) {
                    is SignatureException -> info("Error en la firma del token ${ex.message}")
                    is MalformedJwtException -> info("Token malformado ${ex.message}")
                    is ExpiredJwtException -> info("Token expirado ${ex.message}")
                    is UnsupportedJwtException -> info("Token no soportado ${ex.message}")
                    is IllegalArgumentException -> info("Token incompleto (claims vacío) ${ex.message}")
                    else -> info("Error indeterminado")
                }
            }

        }

        return false

    }

}