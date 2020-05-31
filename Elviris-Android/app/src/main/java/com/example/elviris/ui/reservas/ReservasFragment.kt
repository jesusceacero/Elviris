package com.example.elviris.ui.reservas

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
import androidx.lifecycle.Observer
import com.example.elviris.R
import com.example.elviris.common.Resource
import com.example.elviris.di.MyApp
import com.example.elviris.viewmodel.EventosViewModel
import com.example.elviris.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_eventos_list.*
import kotlinx.android.synthetic.main.fragment_reservas_list.*
import javax.inject.Inject

class ReservasFragment : Fragment() {

    private var columnCount = 1
    private lateinit var listadoAdapter: MyReservasRecyclerViewAdapter
    @Inject
    lateinit var eventosViewModel: EventosViewModel
    @Inject
    lateinit var userViewModel : UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as MyApp).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reservas_list, container, false)

        listadoAdapter = MyReservasRecyclerViewAdapter(viewLifecycleOwner,eventosViewModel)
        val recyclerView = view.findViewById<RecyclerView>(R.id.listReservas)
        // Set the adapter
        with(recyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = listadoAdapter
            }
        }

        eventosViewModel.eventosReservados()
        eventosViewModel.reservados.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Loading -> {
                    showProgressBar()
                }
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

        return view
    }

    private fun hideProgressBar() {
        reservasProgressBar.visibility= View.INVISIBLE
    }

    private fun showProgressBar() {
        reservasProgressBar.visibility = View.VISIBLE
    }

}
