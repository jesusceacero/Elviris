package com.example.elviris.ui.eventos

import android.content.Intent
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
import com.example.elviris.AddPutEventoActivity
import com.example.elviris.DetalleEventoActivity
import com.example.elviris.R
import com.example.elviris.common.Resource
import com.example.elviris.di.MyApp
import com.example.elviris.viewmodel.EventosViewModel
import com.example.elviris.viewmodel.UserViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_eventos_list.*
import javax.inject.Inject


class EventosFragment : Fragment() {

    private var columnCount = 1
    private lateinit var listadoAdapter: MyEventosRecyclerViewAdapter
    @Inject lateinit var eventosViewModel: EventosViewModel
    @Inject lateinit var userViewModel : UserViewModel

    lateinit var add : FloatingActionButton
    lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as MyApp).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_eventos_list, container, false)
        add = view.findViewById(R.id.floatingActionButtonAddEvento)
        userViewModel.userCargar()
        userViewModel.user.observe(viewLifecycleOwner, Observer { user ->

            when(user){
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {

                    if(user.data?.roles == "ADMIN"){
                        add.visibility = View.VISIBLE
                    }

                    add.setOnClickListener(View.OnClickListener {
                        var i= Intent(MyApp.instance, AddPutEventoActivity::class.java).apply {
                            putExtra("add", true)
                        }
                        MyApp.instance.startActivity(i)
                    })
                    listadoAdapter = MyEventosRecyclerViewAdapter(viewLifecycleOwner,userViewModel, user.data!!.eventos,user.data!!.roles)

                    recyclerView = view.findViewById<RecyclerView>(R.id.list)

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

    override fun onResume() {
        super.onResume()
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
                is Resource.Loading -> {
                    showProgressBar()
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
