package com.example.elviris.ui.eventos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.elviris.R
import com.example.elviris.common.Resource
import com.example.elviris.di.MyApp
import com.example.elviris.viewmodel.EventosViewModel
import com.example.elviris.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_eventos_list.*
import javax.inject.Inject


class EventosFragment : Fragment() {

    private var columnCount = 1
    private lateinit var listadoAdapter: MyEventosRecyclerViewAdapter
    @Inject lateinit var eventosViewModel: EventosViewModel
    @Inject lateinit var userViewModel : UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as MyApp).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_eventos_list, container, false)

        userViewModel.userCargar()
        userViewModel.user.observe(viewLifecycleOwner, Observer { user ->

            when(user){
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    listadoAdapter = MyEventosRecyclerViewAdapter(viewLifecycleOwner,userViewModel, user.data!!.eventos,user.data!!.roles)

                    val recyclerView = view.findViewById<RecyclerView>(R.id.list)

                    with(recyclerView) {
                        with(view) {
                            layoutManager = when {
                                columnCount <= 1 -> LinearLayoutManager(context)
                                else -> GridLayoutManager(context, columnCount)
                            }
                            adapter = listadoAdapter
                        }
                    }
                    eventosViewModel.eventosCargar()
                    eventosViewModel.eventos.observe(viewLifecycleOwner, Observer {
                        it.data?.let { it1 -> listadoAdapter.setData(it1) }
                        when(it) {
                            is Resource.Success -> {
                                hideProgressBar()
                                it.data?.let { results ->
                                    listadoAdapter.setData(results)
                                    recyclerView.scheduleLayoutAnimation()
                                }
                            }
                            is Resource.Error -> {
                                hideProgressBar()
                                it.message?.let { message ->
                                    Log.e("Error", "An error occured: $message")
                                }
                            }
                        }
                    })
                }
            }


        })



        return view
    }

    private fun hideProgressBar() {
        eventosProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        eventosProgressBar.visibility = View.VISIBLE
    }


}
