package com.example.elviris

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import coil.api.load
import com.example.elviris.api.response.Evento
import com.example.elviris.common.Resource
import com.example.elviris.di.MyApp
import com.example.elviris.viewmodel.EventosViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_put_evento.*
import kotlinx.android.synthetic.main.fragment_eventos.*
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject

class AddPutEventoActivity : AppCompatActivity() {

    @Inject
    lateinit var eventosViewModel: EventosViewModel

    var nombreFichero: String? = null
    var uriSelected: Uri? = null
    private val READ_REQUEST_CODE = 42

    lateinit var foto : ImageView
    lateinit var titulo : EditText
    lateinit var detalle : EditText
    lateinit var dia :EditText
    lateinit var mes : EditText
    lateinit var anio : EditText
    lateinit var aforo : EditText
    lateinit var guardar : Button
    lateinit var d : String
    lateinit var m : String
    var add : Boolean = true
    lateinit var id : String
    lateinit var evento : Evento

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_put_evento)
        (applicationContext as MyApp).appComponent.inject(this)

        foto = imageViewFotoAddPut
        titulo = editTextTituloAddPut
        detalle = editTextDescricionAddPut
        dia = editTextDia
        mes = editTextMes
        anio = editTextAnio
        aforo = editTextAforoAddPut
        guardar = buttonGuardatAddPut
        add = intent.getBooleanExtra("add",true)

        foto.setOnClickListener(View.OnClickListener {
            performFileSearch()
        })

        if (!add){
            id = intent.getStringExtra("id")
            eventosViewModel.eventoId(id)
            eventosViewModel.evendoDetalle.observe(this, Observer {
                when(it){
                    is Resource.Success -> {
                        titulo.text.append(it.data?.titulo)
                        detalle.text.append(it.data?.descripcion.toString())
                        aforo.text.append(it.data?.aforo.toString())
                        dia.text.append(it.data?.fecha!!.split("/")[0])
                        mes.text.append(it.data?.fecha!!.split("/")[1])
                        anio.text.append(it.data?.fecha!!.split("/")[2])
                        evento = it.data
                        if(it.data.foto != null){
                            foto.load(it.data.foto)
                        }
                    }
                    is Resource.Loading ->{

                    }
                    is Resource.Error -> {
                        it.message?.let { message ->
                            Log.e("Error", "An error occured: $message")
                        }
                    }
                }
            })
        }

        guardar.setOnClickListener(View.OnClickListener {
            if(add){
                if(titulo.text.isEmpty() || detalle.text.isEmpty() || dia.text.isEmpty() || mes.text.isEmpty() ||
                    anio.text.isEmpty() || aforo.text.isEmpty()){
                    Toast.makeText(MyApp.instance, "Debe rellenar todos los campos", Toast.LENGTH_LONG).show()
                }else{
                    try {
                        var body: MultipartBody.Part? = null
                        if (uriSelected != null) {
                            var inputStream =
                                contentResolver.openInputStream(uriSelected!!)
                            var baos = ByteArrayOutputStream()

                            var bufferedInputStream =
                                BufferedInputStream(inputStream)
                            var cantBytes: Int
                            var buffer = ByteArray(1024 * 4)

                            while (bufferedInputStream.read(buffer, 0, 1024 * 4)
                                    .also { cantBytes = it } != -1
                            ) {
                                baos.write(buffer, 0, cantBytes)
                            }

                            var requestFile : RequestBody = RequestBody.create(contentResolver.getType(uriSelected!!)?.let { it1 ->
                                it1
                                    .toMediaTypeOrNull()
                            },baos.toByteArray())

                            body = MultipartBody.Part.createFormData("file", nombreFichero, requestFile)

                        }

                        if(dia.text.toString().toInt() < 10) {
                            if (!dia.text.toString().contains("0")){
                                d = "0"+dia.text.toString()
                            }else{
                                d = dia.text.toString()
                            }
                        }
                        else  d = dia.text.toString()

                        if(mes.text.toString().toInt() < 10) {
                            if(!mes.text.toString().contains("0")){
                                m = "0"+mes.text.toString()
                            }else{
                                m = mes.text.toString()
                            }
                        }
                        else m = mes.text.toString()

                        var f : String = d+"/"+m+"/"+anio.text.toString()
                        Log.i("entra","add")
                        var e : Evento = Evento(titulo.text.toString(),detalle.text.toString(),aforo.text.toString().toInt(),f)

                        val json = Gson().toJson(e)

                        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

                        eventosViewModel.addEventoCargar(body,requestBody)
                        eventosViewModel.addEvento.observe(this, Observer {
                            when(it){
                                is Resource.Success -> {
                                    finish()
                                }
                                is Resource.Loading ->{

                                }
                                is Resource.Error -> {
                                    it.message?.let { message ->
                                        Log.e("Error", "An error occured: $message")
                                    }
                                }
                            }
                        })



                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }else{
                if(titulo.text.isEmpty() || detalle.text.isEmpty() || dia.text.isEmpty() || mes.text.isEmpty() ||
                    anio.text.isEmpty() || aforo.text.isEmpty()){
                    Toast.makeText(MyApp.instance, "Debe rellenar todos los campos", Toast.LENGTH_LONG).show()
                }else{
                    try {
                        var body: MultipartBody.Part? = null
                        if (uriSelected != null) {
                            var inputStream =
                                contentResolver.openInputStream(uriSelected!!)
                            var baos = ByteArrayOutputStream()

                            var bufferedInputStream =
                                BufferedInputStream(inputStream)
                            var cantBytes: Int
                            var buffer = ByteArray(1024 * 4)

                            while (bufferedInputStream.read(buffer, 0, 1024 * 4)
                                    .also { cantBytes = it } != -1
                            ) {
                                baos.write(buffer, 0, cantBytes)
                            }

                            var requestFile : RequestBody = RequestBody.create(contentResolver.getType(uriSelected!!)?.let { it1 ->
                                it1
                                    .toMediaTypeOrNull()
                            },baos.toByteArray())

                            body = MultipartBody.Part.createFormData("file", nombreFichero, requestFile)

                        }

                        if(dia.text.toString().toInt() < 10) {
                            if (!dia.text.toString().contains("0")){
                                d = "0"+dia.text.toString()
                            }else{
                                d = dia.text.toString()
                            }
                        }
                        else  d = dia.text.toString()

                        if(mes.text.toString().toInt() < 10) {
                            if(!mes.text.toString().contains("0")){
                                m = "0"+mes.text.toString()
                            }else{
                                m = mes.text.toString()
                            }
                        }
                        else m = mes.text.toString()

                        var f : String = d+"/"+m+"/"+anio.text.toString()
                        Log.i("entra","edit")
                        evento.titulo = titulo.text.toString()
                        evento.descripcion = detalle.text.toString()
                        evento.aforo = aforo.text.toString().toInt()
                        evento.fecha = f

                        val json = Gson().toJson(evento)

                        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

                        eventosViewModel.editEventoCargar(body,requestBody)
                        eventosViewModel.editEvento.observe(this, Observer {
                            when(it){
                                is Resource.Success -> {
                                    finish()
                                }
                                is Resource.Loading ->{

                                }
                                is Resource.Error -> {
                                    it.message?.let { message ->
                                        Log.e("Error", "An error occured: $message")
                                    }
                                }
                            }
                        })



                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        })

    }

    fun performFileSearch() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, READ_REQUEST_CODE)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            var uri: Uri? = null
            if (data != null) {
                uri = data.data
                foto.load(uri){
                }

                uriSelected = uri
                val returnCursor =
                    contentResolver.query(uri!!, null, null, null, null)
                val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                returnCursor.moveToFirst()
                nombreFichero = returnCursor.getString(nameIndex)
            }
        }
    }
}
