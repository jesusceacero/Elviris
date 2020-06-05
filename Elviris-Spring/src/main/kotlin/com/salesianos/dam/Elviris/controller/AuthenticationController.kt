package com.salesianos.dam.Elviris.controller


import com.salesianos.dam.Elviris.DTOs.UserDTO
import com.salesianos.dam.Elviris.DTOs.toUserDTO
import com.salesianos.dam.Elviris.DTOs.toUserLoginDTO
import com.salesianos.dam.Elviris.model.MyUser
import com.salesianos.dam.Elviris.repository.UserRepository
import com.salesianos.dam.Elviris.security.jwt.JwtTokenProvider
import com.salesianos.dam.Elviris.services.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotBlank


@RestController
class AuthenticationController(
        private val authenticationManager: AuthenticationManager,
        private val jwtTokenProvider: JwtTokenProvider,
        private val repository: UserRepository,
        private val userService: UserService
) {

    @PostMapping("/auth/login")
    fun login(@Valid @RequestBody loginRequest : LoginRequest) : JwtUserResponse {
        print(loginRequest)
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        loginRequest.username, loginRequest.password
                )
        )

        SecurityContextHolder.getContext().authentication = authentication

        val user = authentication.principal as MyUser
        val jwtToken = jwtTokenProvider.generateToken(authentication)

        return JwtUserResponse(jwtToken, user.toUserLoginDTO())

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/me")
    fun me(@AuthenticationPrincipal myUser : MyUser) = userService.userURL(userService.usuariosId(myUser.id!!).toUserDTO())




}


data class LoginRequest(
        @NotBlank val username : String,
        @NotBlank val password: String
)

data class JwtUserResponse(
        val token: String,
        val user : UserDTO
        )