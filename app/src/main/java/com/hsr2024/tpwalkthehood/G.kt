package com.hsr2024.tpwalkthehood

import android.content.SharedPreferences
import com.hsr2024.tpwalkthehood.data.UserAccount

class G {
    companion object{
        var userAccount:UserAccount? = null
        var categoryG:String = "FD6"
        var keywordG:String = "음식점"
        var testmessage:String? = ""
        }
}

class L{
    companion object{
        var login = false
    }
}

class FeedString{
    companion object{
        lateinit var email: String
        lateinit var nickname: String
        lateinit var title:String
        lateinit var text:String
        lateinit var date:String
        lateinit var downUrl:String
        lateinit var profile:String
        lateinit var fileName:String
        lateinit var documentId:String
        lateinit var like:String
        lateinit var likeNum:String
    }
}