package com.example.elviris.ui.user

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import coil.api.load
import coil.transform.CircleCropTransformation
import com.example.elviris.R
import com.example.elviris.api.response.User
import com.mikhaellopez.circularimageview.CircularImageView


import kotlinx.android.synthetic.main.fragment_user.view.*


class MyUserRecyclerViewAdapter(
) : RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private var listado : List<User> = ArrayList()

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as User
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listado[position]

        holder.nombre.text = item.fullName
        holder.foto.borderWidth = 3F
        holder.foto.borderColor = Color.WHITE
        if (item.foto != null){
            holder.foto.load(item.foto){
                crossfade(true)
            }
        }else{
            holder.foto.load(R.drawable.ic_user)
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = listado.size

    fun setData(list: List<User>?) {
        listado = list!!
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val foto : CircularImageView = mView.imageViewListadoUser
        val nombre : TextView = mView.textViewnombreListadoUser
    }
}
