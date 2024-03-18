package com.hsr2024.tpwalkthehood.network


import com.hsr2024.tpwalkthehood.data.CategoryGroupCode
import com.hsr2024.tpwalkthehood.data.KakaoSearchPlaceResponse
import com.hsr2024.tpwalkthehood.data.UserAccount
import com.hsr2024.tpwalkthehood.data.UserLoginData
import com.hsr2024.tpwalkthehood.data.UserLoginResponse
import com.hsr2024.tpwalkthehood.data.UserSignupData
import com.hsr2024.tpwalkthehood.data.Weather
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
    fun userDataToServer(@Body userData: UserSignupData): Call<String>

    @GET("/WalkTheHood/userTest.php")
    fun userCheckNickname(@Query("nickname") ChecknickName: String): Call<String>

    @GET("/WalkTheHood/userTest.php")
    fun userCheckEmail(@Query("email") CheckEmail: String): Call<String>

    @POST("/WalkTheHood/userLogin.php")
    fun userLoginToServer(@Body userData: UserLoginData): Call<UserLoginResponse>


    // 카카오톡 API
    @Headers("Authorization: KakaoAK e70102dbac743d584daa83924b1a25d2")
    @GET("v2/local/search/keyword.json")
    fun searchPlaceToString(
        @Query("query") query: String, @Query("x") longitude: String,
        @Query("y") latitude: String, @Query("category_group_code") category: String,
        @Query("radius") radius: Int = 5000, @Query("sort") sort: String = "distance"
    ): Call<String>


    @Headers("Authorization: KakaoAK e70102dbac743d584daa83924b1a25d2")
    @GET("v2/local/search/keyword.json")
    fun searchPlaceToKakao(
        @Query("query") query: String, @Query("x") longitude: String,
        @Query("y") latitude: String, @Query("category_group_code") category: String,
        @Query("radius") radius: Int = 5000, @Query("sort") sort: String = "distance"
    ): Call<KakaoSearchPlaceResponse>

    @GET("getUltraSrtFcst?serviceKey=1TBrPTECd6gt7PR%2FK29IQYy7BEH1YV%2FQxqn8XOOg1ZQ7ujvcn1HCfL0ln4BYyF9jIHgGVq25bquADIFdixh3Mg%3D%3D")
    fun GetWeather(
        @Query("numOfRows") num_of_rows: Int,     // 한 페이지 경과 수
        @Query("pageNo") page_no: Int,               // 페이지 번호
        @Query("dataType") data_type: String,        // 응답 자료 형식
        @Query("base_date") base_date: String,       // 발표 일자
        @Query("base_time") base_time: String,       // 발표 시각
        @Query("nx") nx: String,                     // 예보지점 X 좌표
        @Query("ny") ny: String                    // 예보지점 Y 좌표
    ): Call<Weather>


    @GET("getUltraSrtFcst?serviceKey=1TBrPTECd6gt7PR%2FK29IQYy7BEH1YV%2FQxqn8XOOg1ZQ7ujvcn1HCfL0ln4BYyF9jIHgGVq25bquADIFdixh3Mg%3D%3D")
    fun GetWeatherToString(
        @Query("numOfRows") num_of_rows: Int,     // 한 페이지 경과 수
        @Query("pageNo") page_no: Int,               // 페이지 번호
        @Query("dataType") data_type: String,        // 응답 자료 형식
        @Query("base_date") base_date: String,       // 발표 일자
        @Query("base_time") base_time: String,       // 발표 시각
        @Query("nx") nx: String,                     // 예보지점 X 좌표
        @Query("ny") ny: String                       // 예보지점 Y 좌표
    ): Call<String>


}




