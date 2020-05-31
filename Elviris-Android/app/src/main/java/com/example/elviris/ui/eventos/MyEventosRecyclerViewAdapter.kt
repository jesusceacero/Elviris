package com.example.elviris.ui.eventos

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.airbnb.lottie.LottieAnimationView
import com.example.elviris.R
import com.example.elviris.api.response.Evento
import com.example.elviris.common.Resource
import com.example.elviris.di.MyApp
import com.example.elviris.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_eventos.view.*
import javax.inject.Inject


class MyEventosRecyclerViewAdapter(
    private var ctx : LifecycleOwner,
    private var userViewModel: UserViewModel,
    private var reservas : List<String>?,
    private var rol: String?,
    private var mValues: List<Evento> = ArrayList()
) : RecyclerView.Adapter<MyEventosRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Evento
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_eventos, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        holder.titulo.text = item.titulo
        holder.descrip.text = item.descripcion
        holder.aforo.text = item.aforo.toString()
        holder.fecha.text = item.fecha
        holder.count.text = item.usuarios.count().toString()

        if (reservas!!.contains(item.id)){
            holder.reservar.visibility = View.GONE
        }

        holder.reservar.setOnClickListener(View.OnClickListener {
            userViewModel.reserverEvento(item.id)
            userViewModel.reserva.observe(ctx ,Observer {
                when(it) {
                    is Resource.Success -> {
                        Toast.makeText(MyApp.instance, "Reserva realizada correctamente", Toast.LENGTH_LONG).show()
                        holder.cargando.visibility = View.GONE
                    }
                    is Resource.Error -> {
                        it.message?.let { message ->
                            Log.e("Error", "An error occured: $message")
                            holder.reservar.visibility = View.VISIBLE
                            holder.cargando.visibility = View.GONE
                        }
                    }
                    is Resource.Loading -> {
                        holder.reservar.visibility = View.GONE
                        holder.cargando.visibility = View.VISIBLE
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
        mValues = listadoNotas
        notifyDataSetChanged()
    }
    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val titulo : TextView = mView.textViewTitulo
        val descrip : TextView = mView.textViewDescrip
        val aforo : TextView = mView.textViewAforo
        val fecha : TextView = mView.textViewFecha
        val foto : ImageView = mView.imageViewCartel
        val reservar : Button = mView.buttonReservar
        val cargando : LottieAnimationView = mView.reservaProgressBar
        val count : TextView = mView.textViewCount
    }
}
