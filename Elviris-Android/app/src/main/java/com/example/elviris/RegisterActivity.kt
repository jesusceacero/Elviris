package com.example.elviris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import com.example.elviris.api.response.CreateUserDTO
import com.example.elviris.common.Resource
import com.example.elviris.di.MyApp
import com.example.elviris.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import javax.inject.Inject

class RegisterActivity : AppCompatActivity() {

    @Inject
    lateinit var registerViewModel: RegisterViewModel
    lateinit var username : EditText
    lateinit var nombre : EditText
    lateinit var password : EditText
    lateinit var password2 : EditText
    lateinit var card : CardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        (applicationContext as MyApp).appComponent.inject(this)

        username = editTextUsernameRegister
        nombre = editTextNombreRegister
        password = editTextPassawordRegister
        password2 = editTextPassword2Register
        var register = buttonRegisterRegister

        register.setOnClickListener(View.OnClickListener {
            var u = username.text
            var n = nombre.text
            var p = password.text
            var p2 = password2.text
            if (p.toString().equals(p2.toString())){
                registerViewModel.register(CreateUserDTO(u.toString(),n.toString(),p.toString(),p2.toString()))
                registerViewModel.user.observe(this, Observer {
                    when(it){
                        is Resource.Success -> {
                            Log.i("registrado",""+it.data)
                            finish()
                        }
                        is Resource.Error ->{
                            it.message?.let { message ->
                                hideProgressBar()
                                Log.e("registro", "Error en el registro / $message")
                            }
                        }
                        is Resource.Loading -> {
                            showProgressBar()
                        }
                    }
                })
            }else{
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun hideProgressBar() {
        cardViewRegister.visibility = View.VISIBLE
        registerProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        cardViewRegister.visibility = View.INVISIBLE
        registerProgressBar.visibility = View.VISIBLE
    }
}
