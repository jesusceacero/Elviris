package com.example.elviris.ui.reservas

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.elviris.R
import com.example.elviris.api.response.Evento
import com.example.elviris.common.Resource
import com.example.elviris.di.MyApp
import com.example.elviris.viewmodel.EventosViewModel

import com.example.elviris.viewmodel.UserViewModel

import kotlinx.android.synthetic.main.fragment_reservas.view.*


class MyReservasRecyclerViewAdapter(
    private var ctx : LifecycleOwner,
    private var eventosViewModel: EventosViewModel,
    private var mValues: MutableList<Evento> = mutableListOf()
    ) : RecyclerView.Adapter<MyReservasRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Evento
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_reservas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        holder.titulo.text = item.titulo
        holder.aforo.text = item.aforo.toString()
        holder.count.text= item.usuarios.count().toString()
        holder.fecha.text= item.fecha

        holder.cancelar.setOnClickListener(View.OnClickListener {
            eventosViewModel.cancelarReserva(item.id.toString())
            eventosViewModel.cancelada.observe(ctx, Observer {
                when(it) {
                    is Resource.Loading -> {
                        holder.cargando.visibility = View.VISIBLE
                        holder.cancelar.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        holder.cargando.visibility = View.GONE
                        Toast.makeText(MyApp.instance, "Reserva Cancelada", Toast.LENGTH_LONG).show()
                        mValues.remove(item)
                        notifyDataSetChanged()
                    }
                    is Resource.Error -> {
                        holder.cancelar.visibility = View.VISIBLE
                        holder.cargando.visibility = View.GONE
                        it.message?.let { message ->
                            Log.e("Error", "An error occured: $message")
                        }
                    }
                }
            })
        })

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    fun setData(listadoNotas: List<Evento>) {
        mValues = listadoNotas.toMutableList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val titulo = mView.textViewTituloReserva
        val cartel = mView.imageViewCartelReserva
        val aforo = mView.textViewAforoReserva
        val count = mView.textViewCountReserva
        val cancelar = mView.buttonReservarReserva
        val fecha = mView.textViewFechaReserva
        val cargando = mView.reservaCancelarProgressBar

    }
}
