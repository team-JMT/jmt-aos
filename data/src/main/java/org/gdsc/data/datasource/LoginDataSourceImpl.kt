package org.gdsc.data.datasource

import android.util.Log
import org.gdsc.data.network.LoginAPI
import org.gdsc.data.model.AppleLoginRequest
import org.gdsc.data.model.GoogleLoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class LoginDataSourceImpl @Inject constructor(
    private val loginAPI: LoginAPI
) : LoginDataSource {
    override fun postGoogleToken(token: String) {

        loginAPI.sendUserGoogleToken(GoogleLoginRequest(token))
            .enqueue(object : Callback<GoogleLoginRequest> {
                override fun onResponse(
                    call: Call<GoogleLoginRequest>,
                    response: Response<GoogleLoginRequest>
                ) {
                    if (response.isSuccessful) {
                        Log.d("response", "success : ${response.body().toString()}")
                    } else {
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

    override fun postAppleToken(email: String, clientId: String) {

        loginAPI.sendUserAppleToken(AppleLoginRequest(email, clientId))
            .enqueue(object : Callback<AppleLoginRequest> {
                override fun onResponse(
                    call: Call<AppleLoginRequest>,
                    response: Response<AppleLoginRequest>
                ) {
                    if (response.isSuccessful) {
                        Log.d("response", "success : ${response.body().toString()}")
                    } else {
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