package com.example.elviris.ui.perfil

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import coil.api.load

import com.example.elviris.R
import com.example.elviris.api.response.Evento
import com.example.elviris.api.response.IdDTO
import com.example.elviris.api.response.User
import com.example.elviris.common.Resource
import com.example.elviris.di.MyApp
import com.example.elviris.viewmodel.UserViewModel
import com.google.gson.Gson
import com.mikhaellopez.circularimageview.CircularImageView
import kotlinx.android.synthetic.main.fragment_eventos_list.*
import kotlinx.android.synthetic.main.fragment_perfil.*
import kotlinx.android.synthetic.main.fragment_perfil.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PerfilFragment : Fragment() {

    var nombreFichero: String? = null
    var uriSelected: Uri? = null
    private val READ_REQUEST_CODE = 42

    private lateinit var foto : CircularImageView
    private lateinit var username : TextView
    private lateinit var nombre : TextView
    private lateinit var check : ImageView
    private lateinit var cancel : ImageView
    @Inject
    lateinit var userViewModel : UserViewModel
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as MyApp).appComponent.inject(this)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_perfil, container, false)
        username = view.textViewUsernamePerfil
        nombre = view.textViewFullNamePerfil
        foto = view.imageViewfotoPerfil
        check = view.imageViewValidarFoto
        cancel = view.imageViewCancelarFoto
        foto.borderWidth = 3F
        foto.borderColor = Color.WHITE

        foto.setOnClickListener(View.OnClickListener {
            performFileSearch()
        })
        cancel.setOnClickListener(View.OnClickListener {
            if(user.foto == null){
                foto.load(R.drawable.ic_user){
                    crossfade(true)
                }
            }else{
                foto.load(user.foto){
                    crossfade(true)
                }
            }

            check.visibility = View.GONE
            cancel.visibility = View.GONE
        })

        check.setOnClickListener(View.OnClickListener {
            try {
                var body: MultipartBody.Part? = null
                if (uriSelected != null) {
                    var inputStream =
                        activity?.contentResolver?.openInputStream(uriSelected!!)
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

                    var requestFile : RequestBody = RequestBody.create(activity?.contentResolver?.getType(uriSelected!!)?.let { it1 ->
                        it1
                            .toMediaTypeOrNull()
                    },baos.toByteArray())

                    body = MultipartBody.Part.createFormData("file", nombreFichero, requestFile)

                }

                val json = Gson().toJson(IdDTO(user.id))

                val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

                userViewModel.editFotoCargar(body,requestBody)
                userViewModel.editFoto.observe(viewLifecycleOwner, Observer {
                    when(it){
                        is Resource.Loading -> {
                        }
                        is Resource.Success -> {
                            Toast.makeText(activity, "Foto guardada correctamente", Toast.LENGTH_LONG).show()
                            user = it.data!!
                        }
                        is Resource.Error -> {

                        }
                    }
                })
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            check.visibility = View.GONE
            cancel.visibility = View.GONE
        })


        userViewModel.user.observe(viewLifecycleOwner, Observer {u ->
            when(u){
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    hideProgressBar()
                    user = u.data!!
                    nombre.text = u.data?.fullName
                    username.text = u.data?.username
                    if(u.data?.foto == null){
                        foto.load(R.drawable.ic_user){
                            crossfade(true)
                        }
                    }else{
                        foto.load(u.data.foto){
                            crossfade(true)
                        }
                    }
                }
                is Resource.Error -> {

                }
            }
        })

        return  view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PerfilFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun hideProgressBar() {
        perfilProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        perfilProgressBar.visibility = View.VISIBLE
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

                check.visibility = View.VISIBLE
                cancel.visibility = View.VISIBLE

                uriSelected = uri
                val returnCursor =
                    activity?.contentResolver?.query(uri!!, null, null, null, null)
                val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                returnCursor.moveToFirst()
                nombreFichero = returnCursor.getString(nameIndex)
            }
        }
    }
}
