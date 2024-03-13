package com.hsr2024.tpwalkthehood.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitHelper {


    // 레트로핏의 1단계 작업이 너무 길어서 전역으로 만듬
    companion object{
        fun getRetrofitInstance() : Retrofit{
            val builder = Retrofit.Builder()
            builder.baseUrl("http://ruaris.dothome.co.kr")
            builder.addConverterFactory(ScalarsConverterFactory.create())
            builder.addConverterFactory(GsonConverterFactory.create())

            return builder.build() // 문자열과 json으로 생성.
        }
    }
}