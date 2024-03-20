package com.hsr2024.tpwalkthehood.data

data class UserSignupData(
    var nickname:String,
    var email:String,
    var password:String
)

data class UserLoginData(
    var email:String,
    var password:String
)

data class UserLoginResponse(
    var rowNum:Int,
    var user: UserAccount
)


data class UserAccount(
    var email: String,
    var password: String,
    var nickname: String,
    var imgfile:String
)

data class CategoryGroupCode(

    var MT1: String= "", // 대형마트
    var CS2: String= "", // 편의점
    var BK9: String= "", // 은행
    var CT1: String= "", // 문화시설
    var CE7: String= "", // 카페
    var HP8: String= "", // 병원
    var PM9: String= "", // 약국
    var FD6: String= "" // 음식점

)

//카테고리 소분류




