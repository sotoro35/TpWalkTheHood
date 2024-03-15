package com.hsr2024.tpwalkthehood.network


import com.hsr2024.tpwalkthehood.data.CategoryGroupCode
import com.hsr2024.tpwalkthehood.data.UserAccount
import com.hsr2024.tpwalkthehood.data.UserLoginData
import com.hsr2024.tpwalkthehood.data.UserLoginResponse
import com.hsr2024.tpwalkthehood.data.UserSignupData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PartMap
import retrofit2.http.Query

interface RetrofitService {

    // 1. POST 방식으로 사용자의 정보 서버에 전달
    //@Body로 보낸 json문자열을 $_POST라는 배열에 자동 저장되지 않음. Ex68번. 04Retrofit/bbb.php 참고
    @POST("/WalkTheHood/userData.php")
    fun userDataToServer(@Body userData:UserSignupData) : Call<String>
    @GET("/WalkTheHood/userTest.php")
    fun userCheckNickname(@Query("nickname") ChecknickName:String) : Call<String>

    @GET("/WalkTheHood/userTest.php")
    fun userCheckEmail(@Query("email") CheckEmail:String) : Call<String>

    @POST("/WalkTheHood/userLogin.php")
    fun userLoginToServer(@Body userData:UserLoginData) : Call<UserLoginResponse>


    // 카카오톡 API
    @Headers ("Authorization: KakaoAK e70102dbac743d584daa83924b1a25d2")
    @GET ("v2/local/search/keyword.json")
    fun searchPlaceToString(@Query("query")query:String, @Query("x") longitude:String,
                            @Query("y") latitude:String, @Query("category_group_code") category: CategoryGroupCode) : Call<String>




}