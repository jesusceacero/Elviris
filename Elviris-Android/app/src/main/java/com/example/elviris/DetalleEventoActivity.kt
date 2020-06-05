package com.example.elviris

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import coil.api.load
import com.example.elviris.common.Resource
import com.example.elviris.di.MyApp
import com.example.elviris.viewmodel.EventosViewModel
import kotlinx.android.synthetic.main.activity_detalle_evento.*
import kotlinx.android.synthetic.main.fragment_perfil.*
import org.w3c.dom.Text
import javax.inject.Inject

class DetalleEventoActivity : AppCompatActivity() {

    @Inject
    lateinit var eventosViewModel: EventosViewModel

    lateinit var foto : ImageView
    lateinit var titulo: TextView
    lateinit var descrip : TextView
    lateinit var aforo : TextView
    lateinit var count : TextView
    lateinit var fecha : TextView
    lateinit var id : String
    lateinit var card : CardView
    lateinit var editar : Button
    lateinit var borrar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_evento)
        (applicationContext as MyApp).appComponent.inject(this)
        id = intent.getStringExtra("id")
        foto = imageViewfotoDetalle
        titulo = textViewTituloDetalle
        descrip = textViewDescripcionDetalle
        aforo = textViewAforoDetalle
        count = textViewCountDetalle
        fecha = textViewFechaDetalle
        card = cardDetalle
        editar = buttonEditarEvento
        borrar = buttonEliminarEvento


        borrar.setOnClickListener(View.OnClickListener {
            val alertDialogBuilder =
                AlertDialog.Builder(
                    this
                )
            alertDialogBuilder.setTitle("Borrado")
            alertDialogBuilder
                .setMessage("¿Estas seguro que deseas borrar este evento?")
                .setCancelable(false)
                .setPositiveButton("Si",
                    DialogInterface.OnClickListener { dialog, idd ->
                        eventosViewModel.deleteEventoCargar(id)
                        eventosViewModel.deleteEvento.observe(this, Observer {
                            when(it){
                                is Resource.Loading -> {
                                }
                                is Resource.Success -> {
                                    Toast.makeText(this, "Evento borrado correctamente", Toast.LENGTH_LONG).show()
                                    finish()
                                }
                                is Resource.Error ->{
                                    Toast.makeText(MyApp.instance, "Evento borrado correctamente", Toast.LENGTH_LONG).show()
                                    finish()
                                }
                            }
                        })
                    })
                .setNegativeButton("No",
                    DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        })
        editar.setOnClickListener(View.OnClickListener {
            var i= Intent(MyApp.instance, AddPutEventoActivity::class.java).apply {
                putExtra("add", false)
                putExtra("id", id)
            }
            startActivity(i)
        })

        eventosViewModel.eventoId(id)
        eventosViewModel.evendoDetalle.observe(this, Observer { evento ->
            when(evento){
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    hideProgressBar()
                    if(evento.data?.foto != null){
                        foto.load(evento.data?.foto){
                            crossfade(true)
                        }
                    }else{
                        foto.load(R.drawable.ic_foto)
                    }
                    titulo.text = evento.data?.titulo
                    descrip.text = evento.data?.descripcion
                    aforo.text = evento.data?.aforo.toString()
                    count.text = evento.data?.usuarios?.count().toString()
                    fecha.text = evento.data?.fecha
                }
                is Resource.Error ->{
                    Toast.makeText(MyApp.instance, "No existe este evento", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        })

    }

    private fun hideProgressBar() {
        detalleProgressBar.visibility = View.INVISIBLE
        foto.visibility = View.VISIBLE
        card.visibility = View.VISIBLE
    }

    private fun showProgressBar() {
        detalleProgressBar.visibility = View.VISIBLE
        foto.visibility = View.INVISIBLE
        card.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        eventosViewModel.eventoId(id)
        eventosViewModel.evendoDetalle.observe(this, Observer { evento ->
            when(evento){
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    hideProgressBar()
                    if(evento.data?.foto != null){
                        foto.load(evento.data?.foto){
                            crossfade(true)
                        }
                    }else{
                        foto.load(R.drawable.ic_foto)
                    }
                    titulo.text = evento.data?.titulo
                    descrip.text = evento.data?.descripcion
                    aforo.text = evento.data?.aforo.toString()
                    count.text = evento.data?.usuarios?.count().toString()
                    fecha.text = evento.data?.fecha
                }
                is Resource.Error ->{
                    Toast.makeText(MyApp.instance, "No existe este evento", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        })
    }
}
