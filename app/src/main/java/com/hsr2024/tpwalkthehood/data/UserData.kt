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
    var nickname: String
)




