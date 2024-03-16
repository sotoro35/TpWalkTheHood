package com.hsr2024.tpwalkthehood

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this,"d93d64947a13f3365a84a2b2ca5e432d")

    }
}