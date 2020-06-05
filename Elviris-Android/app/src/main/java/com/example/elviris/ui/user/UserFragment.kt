package com.example.elviris.ui.user

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.elviris.R
import com.example.elviris.common.Resource
import com.example.elviris.di.MyApp
import com.example.elviris.viewmodel.EventosViewModel
import com.example.elviris.viewmodel.UserViewModel
import javax.inject.Inject


class UserFragment : Fragment() {

    @Inject
    lateinit var userViewModel: UserViewModel

    @Inject
    lateinit var eventosViewModel: EventosViewModel

    private var columnCount = 1
    lateinit var listado : MyUserRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as MyApp).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_list, container, false)

        listado = MyUserRecyclerViewAdapter()
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = listado
            }

            userViewModel.usuariosEventoCargar(activity?.intent!!.getStringExtra("id"))
            userViewModel.usuariosEvento.observe(viewLifecycleOwner, Observer {user ->
                when(user) {
                    is Resource.Success -> {
                        user.data?.let { results ->
                            listado.setData(results)
                            Log.i("usuarios",""+results)
                            if (results.isEmpty()) {
                                Toast.makeText(MyApp.instance, "No tiene reservas realizadas", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    is Resource.Error -> {
                        user.message?.let { message ->
                            Log.e("Error", "An error occured: $message")
                        }
                    }
                    is Resource.Loading -> {
                    }
                }
            })
        }
        return view
    }

}
