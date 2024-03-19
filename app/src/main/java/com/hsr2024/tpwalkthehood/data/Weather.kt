package com.hsr2024.tpwalkthehood.data

data class Weather( val response:WeatherResponse)
data class WeatherResponse(val header : WeatherHeader, val body: WeatherBody)
data class WeatherHeader(val resultCode: Int,val resultMsg:String)
data class WeatherBody(val dataType:String ,val items:WeatherItems, val totalCount:Int)
data class WeatherItems(val item:List<Weatheritem>)
data class Weatheritem(val category : String, val fcstDate : String, val fcstTime : String, val fcstValue : String)
//category : 자료 구분 코드, fcstDate : 예측 날짜, fcstTime : 예측 시간, fcstValue : 예보 값


class ModelWeather{

        var rainType= ""    // 강수형태
        var sky = ""        //하늘 상태
        var temp = ""       //기온
        var fcstTime = ""   //예보시각

    //- 하늘상태(SKY)코드 :맑음(1), 구름많음(3), 흐림(4)
    //- 강수형태(PTY)코드 :(초단기)없음(0), 비(1), 비/눈(2), 눈(3), 빗방울(5), 빗방울눈날림(6), 눈날림(7)
    // (단기)없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
}