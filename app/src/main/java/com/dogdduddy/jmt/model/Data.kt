package com.dogdduddy.jmt.model

import android.util.Log
import com.dogdduddy.jmt.network.RetrofitAPI
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Data {
    fun postGoogleToken(token:String?) {
        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)

        //val gson : Gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("http://34.64.147.86:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build()).build()

        Log.d("response", "Data token : $token")
        val service = retrofit.create(RetrofitAPI::class.java)
        service.sendUserGoogleToken(JsonPlaceDTO(token!!)).enqueue(object : retrofit2.Callback<JsonPlaceDTO> {
            override fun onResponse(
                call: retrofit2.Call<JsonPlaceDTO>,
                response: retrofit2.Response<JsonPlaceDTO>
            ) {
                if(response.isSuccessful) {
                    Log.d("response", "success : ${response.body().toString()}")
                }
                else {
                    Log.d("response", "fail : ${response.body().toString()}")
                    Log.d("response", "fail : ${response.message()}")
                    Log.d("response", "fail : ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<JsonPlaceDTO>, t: Throwable) {
                Log.d("response", "error : ${t.message.toString()}")
            }
        })
    }

    fun postAppleToken(email:String, clientId:String) {
        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)

        //val gson : Gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("https://gdsc-jmt.loca.lt/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build()).build()

        val service = retrofit.create(RetrofitAPI::class.java)
        service.sendUserAppleToken(JsonPlaceDTO2(email, clientId)).enqueue(object : retrofit2.Callback<JsonPlaceDTO2> {
            override fun onResponse(call: Call<JsonPlaceDTO2>, response: Response<JsonPlaceDTO2>) {
                if(response.isSuccessful) {
                    Log.d("response", "success : ${response.body().toString()}")
                }
                else {
                    Log.d("response", "fail : ${response.body().toString()}")
                    Log.d("response", "fail : ${response.message()}")
                    Log.d("response", "fail : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<JsonPlaceDTO2>, t: Throwable) {
                Log.d("response", "error : ${t.message.toString()}")
            }

        })
    }

    fun testPost() {

        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)

        //val gson : Gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build()).build()
        val service = retrofit.create(RetrofitAPI::class.java)
        service.callPost("234").enqueue(object : retrofit2.Callback<JSONObject> {
            override fun onResponse(
                call: retrofit2.Call<JSONObject>,
                response: retrofit2.Response<JSONObject>
            ) {
                if(response.isSuccessful) {
                    Log.d("response", "success : ${response.body().toString()}")
                }
                else {
                    Log.d("response", "fail : ${response.body().toString()}")
                    Log.d("response", "fail : ${response.message()}")
                    Log.d("response", "fail : ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<JSONObject>, t: Throwable) {
                Log.d("response", "error : ${t.message.toString()}")
            }
        })
    }
}