package com.hsr2024.tpwalkthehood.data

data class Weather( val response:WeatherResponse)
data class WeatherResponse(val header : WeatherHeader, val body: WeatherBody)
data class WeatherHeader(val resultCode: Int,val resultMsg:String)
data class WeatherBody(val dataType:String ,val items:WeatherItems, val totalCount:Int)
data class WeatherItems(val weatheritem:List<Weatheritem>)
data class Weatheritem(val category : String, val fcstDate : String, val fcstTime : String, val fcstValue : String)
//category : 자료 구분 코드, fcstDate : 예측 날짜, fcstTime : 예측 시간, fcstValue : 예보 값
