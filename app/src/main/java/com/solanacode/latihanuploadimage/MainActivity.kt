package com.solanacode.latihanuploadimage

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.solanacode.latihanuploadimage.databinding.ActivityMainBinding
import com.solanacode.latihanuploadimage.viewmodel.RegisterViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class MainActivity : AppCompatActivity() {

    private var imageMultiPart: MultipartBody.Part? = null
    private var imageUri: Uri? = Uri.EMPTY
    private var imageFile: File? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var registerViewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        binding.addImage.setOnClickListener {
            openGallery()
        }
        binding.register.setOnClickListener {
            postDataCar()
        }

    }

    fun postDataCar(){
        val fullName = binding.fullName.text.toString().toRequestBody("multipart/form-data".toMediaType())
        val email = binding.email.text.toString().toRequestBody("multipart/form-data".toMediaType())
        val password = binding.password.text.toString().toRequestBody("multipart/form-data".toMediaType())
        val phoneNumber = binding.phoneNumber.text.toString().toRequestBody("multipart/form-data".toMediaType())
        val address = binding.address.text.toString().toRequestBody("multipart/form-data".toMediaType())
        val city = binding.phoneNumber.text.toString().toRequestBody("multipart/form-data".toMediaType())

        registerViewModel.addLiveDataCar.observe(this,{
            if (it != null){
                Toast.makeText(this, "Add Data Car Succeeded", Toast.LENGTH_SHORT).show()
            }
        })
        registerViewModel.postApiCar(fullName,email,password,phoneNumber,address, city, imageMultiPart!!)
    }

    fun openGallery(){
        getContent.launch("image/*")
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val contentResolver: ContentResolver = this!!.contentResolver
                val type = contentResolver.getType(it)
                imageUri = it

                val fileNameimg = "${System.currentTimeMillis()}.png"
                binding.addImage.setImageURI(it)
                Toast.makeText(this, "$imageUri", Toast.LENGTH_SHORT).show()

                val tempFile = File.createTempFile("and1-", fileNameimg, null)
                imageFile = tempFile
                val inputstream = contentResolver.openInputStream(uri)
                tempFile.outputStream().use    { result ->
                    inputstream?.copyTo(result)
                }
                val requestBody: RequestBody = tempFile.asRequestBody(type?.toMediaType())
                imageMultiPart = MultipartBody.Part.createFormData("image", tempFile.name, requestBody)
            }
        }

}