package br.pucbr.authserver.security

import br.pucbr.authserver.users.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.jackson.io.JacksonDeserializer
import io.jsonwebtoken.jackson.io.JacksonSerializer
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

@Component
class Jwt {
    fun  createToken(user: User): String =
        UserToken(user).let {
            Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(SECRET.toByteArray()))
                .json(JacksonSerializer())
                .issuedAt(utcNow().toDate())
                .expiration(utcNow().plusHours(
                    if (it.isAdmin) ADMIN_EXPIRE_HOURS else EXPIRE_HOURS
                ).toDate())
                .issuer(ISSUER)
                .subject(user.id.toString())
                .claim(USER_FIELD, it)
                .compact()
        }

    fun extract (req: HttpServletRequest): Authentication? {
        try {
            val header = req.getHeader(AUTHORIZATION)
            if (header == null || !header.startsWith("Bearer")) return null
            val token = header.replace("Bearer", "").trim()

            val claims = Jwts
                .parser().verifyWith(Keys.hmacShaKeyFor(SECRET.toByteArray()))
                .json(JacksonDeserializer(mapOf(USER_FIELD to UserToken::class.java)))
                .build()
                .parseSignedClaims(token)
                .payload

            if (claims.issuer != ISSUER) return null
            return claims.get("user", UserToken::class.java).toAuthetication()
        } catch (e: Throwable) {
            log.debug("Token rejected", e)
            return null
        }
    }

    companion object{
         val log = LoggerFactory.getLogger(Jwt::class.java)
        const val SECRET = "32a8db2b916f39b2701874bcbe12a224422079ba"
        const val EXPIRE_HOURS = 48L
        const val ADMIN_EXPIRE_HOURS = 1L
        const val ISSUER = "PUCPR AuthServer"
        const val USER_FIELD = "user"

        private fun utcNow() = ZonedDateTime.now(ZoneOffset.UTC)
        private fun ZonedDateTime.toDate(): Date = Date.from(this.toInstant())
        private fun UserToken.toAuthetication(): Authentication {
            val authorities = roles.map { SimpleGrantedAuthority("ROLE_$it") }
            return UsernamePasswordAuthenticationToken.authenticated(this, id, authorities)
        }
    }
}