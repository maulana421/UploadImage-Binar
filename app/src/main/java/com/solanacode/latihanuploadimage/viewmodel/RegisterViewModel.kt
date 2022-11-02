package com.solanacode.latihanuploadimage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.solanacode.latihanuploadimage.api.ApiService
import com.solanacode.latihanuploadimage.api.RetrofitClient
import com.solanacode.latihanuploadimage.model.ResponseRegisterItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    lateinit var addLiveDataCar : MutableLiveData<ResponseRegisterItem>

    init {
        addLiveDataCar = MutableLiveData()
    }

    fun postLiveDataCar() : MutableLiveData<ResponseRegisterItem>{
        return addLiveDataCar
    }

    fun postApiCar(fullname : RequestBody, email : RequestBody, password : RequestBody, phone_number : RequestBody, address : RequestBody, city : RequestBody, image : MultipartBody.Part){
        RetrofitClient.instance.addUserAdmin(fullname,email,password,phone_number,address, city, image)
            .enqueue(object : Callback<ResponseRegisterItem> {
                override fun onResponse(
                    call: Call<ResponseRegisterItem>,
                    response: Response<ResponseRegisterItem>
                ) {
                    if (response.isSuccessful){
                        addLiveDataCar.postValue(response.body())
                    }else{
                        addLiveDataCar.postValue(null)
                    }

                }

                override fun onFailure(call: Call<ResponseRegisterItem>, t: Throwable) {
                    addLiveDataCar.postValue(null)
                }

            })
    }

}