package org.gdsc.jmt.model

import android.util.Log
import org.gdsc.jmt.network.LoginAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginDataSourceImpl {
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
        val service = retrofit.create(LoginAPI::class.java)
        service.sendUserGoogleToken(GoogleLoginRequest(token!!)).enqueue(object : retrofit2.Callback<GoogleLoginRequest> {
            override fun onResponse(
                call: Call<GoogleLoginRequest>,
                response: Response<GoogleLoginRequest>
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

            override fun onFailure(call: Call<GoogleLoginRequest>, t: Throwable) {
                Log.d("response", "error : ${t.message.toString()}")
            }
        })
    }

    fun postAppleToken(email:String, clientId:String) {
        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)

        val retrofit = Retrofit.Builder().baseUrl("https://gdsc-jmt.loca.lt/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build()).build()

        val service = retrofit.create(LoginAPI::class.java)
        service.sendUserAppleToken(AppleLoginRequest(email, clientId)).enqueue(object : retrofit2.Callback<AppleLoginRequest> {
            override fun onResponse(call: Call<AppleLoginRequest>, response: Response<AppleLoginRequest>) {
                if(response.isSuccessful) {
                    Log.d("response", "success : ${response.body().toString()}")
                }
                else {
                    Log.d("response", "fail : ${response.body().toString()}")
                    Log.d("response", "fail : ${response.message()}")
                    Log.d("response", "fail : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AppleLoginRequest>, t: Throwable) {
                Log.d("response", "error : ${t.message.toString()}")
            }

        })
    }
}