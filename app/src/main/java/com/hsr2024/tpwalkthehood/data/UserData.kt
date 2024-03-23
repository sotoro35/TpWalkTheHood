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

data class favoriteItem(
    var placeName:String?,
    var roadAddress:String?,
    var distance:String?,
    var url:String?
)

data class FeedItem(

    var email: String,
    var nickname: String?,
    var title:String,
    var text:String,
    var date:String,
    var downUrl:String?,
    var profile:String?,
    var fileName:String
)







