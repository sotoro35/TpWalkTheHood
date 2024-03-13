package com.hsr2024.tpwalkthehood.network

import com.hsr2024.tpwalkthehood.data.UserSignupData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PartMap
import retrofit2.http.Query

interface RetrofitService {

    // 1. POST 방식으로 사용자의 정보 서버에 전달
    //@Body로 보낸 json문자열을 $_POST라는 배열에 자동 저장되지 않음. Ex68번. 04Retrofit/bbb.php 참고
    @POST("/WalkTheHood/userData.php")
    fun userDataToServer(@Body userData:UserSignupData) : Call<String>
    @POST("/WalkTheHood/userCheck.php")
    fun userCheckNickname(@Query("nickname") ChecknickName:String) : Call<String>

    @POST("/WalkTheHood/userCheck.php")
    fun userCheckEmail(@Query("email") CheckEmail:String) : Call<String>


}