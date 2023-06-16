package com.dicoding.ecosorter.view.camera

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.ecosorter.helper.ApiConfig
import com.dicoding.ecosorter.helper.ImageRequestBody
import com.dicoding.ecosorter.helper.ResponseScanner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CameraViewModel: ViewModel() {

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "CameraViewModel"
    }

    fun postImage(context: Context, image: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().postImage(ImageRequestBody(image))

        client.enqueue(object : Callback<ResponseScanner> {
            override fun onResponse(call: Call<ResponseScanner>, response: Response<ResponseScanner>) {
                _isLoading.postValue(false)
                Log.d("RESP", response.toString())
                if (response.isSuccessful) {
                    val result = response.body()?.result ?: ""
                    _response.postValue(result)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    showToast(context, "Failed to scan image, please try again!")
                }
            }

            override fun onFailure(call: Call<ResponseScanner>, t: Throwable) {
                _isLoading.postValue(false)
                Log.e(TAG, "onFailure: ${t.message}")
                showToast(context, "Failed to scan image, please try again!")
            }
        })
    }
}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
