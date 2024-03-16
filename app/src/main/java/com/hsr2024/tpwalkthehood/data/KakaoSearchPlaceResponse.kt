package com.hsr2024.tpwalkthehood.data


data class KakaoSearchPlaceResponse(var documents:List<Place>)

data class Place( // 한 아이템에 있는 내용..
    var id: String,
    var place_name: String,
    var category_name: String,
    var phone: String,
    var address_name: String,
    var road_address_name: String,
    var x: String,
    var y: String,
    var place_url: String,
    var distance: String


)