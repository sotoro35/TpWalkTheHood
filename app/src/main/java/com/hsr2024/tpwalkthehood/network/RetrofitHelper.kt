package com.hsr2024.tpwalkthehood.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitHelper {


    // 레트로핏의 1단계 작업이 너무 길어서 전역으로 만듬
    companion object{
        fun getRetrofitInstance(baseUrl:String) : Retrofit{
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // 연결 시간 초과 시간 설정 (예: 30초)
                .readTimeout(30, TimeUnit.SECONDS) // 읽기 시간 초과 시간 설정
                .writeTimeout(30, TimeUnit.SECONDS) // 쓰기 시간 초과 시간 설정
                .build()

            val builder = Retrofit.Builder()
            builder.baseUrl(baseUrl)
            builder.client(okHttpClient)
            builder.addConverterFactory(ScalarsConverterFactory.create())
            builder.addConverterFactory(GsonConverterFactory.create())

            return builder.build() // 문자열과 json으로 생성.
        }
    }
}