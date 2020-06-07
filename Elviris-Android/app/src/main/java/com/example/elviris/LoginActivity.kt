package com.example.elviris

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.damkeep.api.response.LoginDTO
import com.example.elviris.api.TokenInterceptor
import com.example.elviris.common.Resource
import com.example.elviris.di.MyApp
import com.example.elviris.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var sharedPref: SharedPreferences

    @Inject
    lateinit var tokenInterceptor: TokenInterceptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        (applicationContext as MyApp).appComponent.inject(this)

        var username = editTextUsername
        var password = editTextPassword
        var login = buttonLogin
        var register = buttonRegister
        var card = cardLogin
        var loaginProgressBar = loginProgressBar

        login.setOnClickListener(View.OnClickListener {
            loginViewModel.login(LoginDTO(username.text.toString(),password.text.toString()))
            loginViewModel.login.observe(this, Observer {
                when(it){
                    is Resource.Success -> {
                        Log.i("logueado",""+it.data)
                        tokenInterceptor.token = it.data?.token
                        var i= Intent(this, MainActivity::class.java).apply {
                        }
                        startActivity(i)
                        finish()
                    }
                    is Resource.Error ->{
                        hideProgressBar()
                        it.message?.let { message ->
                            Log.e("login", "Error en el login / $message")
                        }
                        Toast.makeText(this, "Usuario o Contrañesa incorrectos.", Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            })
        })

        register.setOnClickListener(View.OnClickListener {
            var i= Intent(this, RegisterActivity::class.java).apply {
            }
            startActivity(i)
        })
    }

    private fun hideProgressBar() {
        cardLogin.visibility = View.VISIBLE
        loginProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        cardLogin.visibility = View.INVISIBLE
        loginProgressBar.visibility = View.VISIBLE
    }
}
