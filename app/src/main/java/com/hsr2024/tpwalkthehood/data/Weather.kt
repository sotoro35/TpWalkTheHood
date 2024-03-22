package com.hsr2024.tpwalkthehood.data

import com.hsr2024.tpwalkthehood.R

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

        companion object{
                fun getSkyString(rainType:String, sky :String):String{
                        return if (rainType == "0"){
                                when(sky){
                                        "1" -> "맑음"//맑음
                                        "3" -> "구름" //구름 많음
                                        "4" -> "흐림" //흐림
                                        else -> "오류"
                                }
                        } else {
                                when(rainType){
                                        "1" -> "비" //비
                                        "2" -> "비/눈" //비/눈
                                        "3" -> "눈" //눈
                                        "5" -> "비"
                                        "6" -> "비/눈"
                                        "7" -> "눈"
                                        else -> "오류"
                                }
                        }
                }//getskyString...

                fun getSky(rainType:String, sky :String):Int{
                        return if (rainType == "0"){
                                when(sky){
                                        "1" -> R.drawable.weathericon_sunny //맑음
                                        "3" -> R.drawable.weathericon_cloudy1 //그름 많음
                                        "4" -> R.drawable.weathericon_cloudy2 //흐림
                                        else -> R.drawable.icon_reload
                                }
                        } else {
                                when(rainType){
                                        "1" -> R.drawable.weathericon_rain //비
                                        "2" -> R.drawable.weathericon_snowrain //비/눈
                                        "3" -> R.drawable.weathericon_snow //눈
                                        "5" -> R.drawable.weathericon_rain
                                        "6" -> R.drawable.weathericon_snowrain
                                        "7" -> R.drawable.weathericon_snow
                                        else -> R.drawable.icon_reload
                                }
                        }
                }// getSky

                fun convertTime(time: String): String {
                        val hour = time.substring(0, 2).toInt()
                        val minute = time.substring(2).toInt()

                        val amPm: String
                        val convertedHour: Int

                        if (hour >= 12) {
                                amPm = "오후"
                                convertedHour = if (hour == 12) hour else hour - 12
                        } else {
                                amPm = "오전"
                                convertedHour = if (hour == 0) 12 else hour
                        }

                        return "$amPm\n${convertedHour}시"
                }
        }

    //- 하늘상태(SKY)코드 :맑음(1), 구름많음(3), 흐림(4)
    //- 강수형태(PTY)코드 :(초단기)없음(0), 비(1), 비/눈(2), 눈(3), 빗방울(5), 빗방울눈날림(6), 눈날림(7)
    // (단기)없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
}