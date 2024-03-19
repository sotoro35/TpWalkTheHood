package com.hsr2024.tpwalkthehood.data

data class KakaoRegionResponse(var documents:List<RegionItem>)

data class RegionItem(
    var region_type:String,
    var address_name:String,
    var region_1depth_name:String,



)

//region_type	String	H(행정동) 또는 B(법정동)
//address_name	String	전체 지역 명칭
//region_1depth_name	String	지역 1Depth, 시도 단위
//바다 영역은 존재하지 않음
//region_2depth_name	String	지역 2Depth, 구 단위
//바다 영역은 존재하지 않음
//region_3depth_name	String	지역 3Depth, 동 단위
//바다 영역은 존재하지 않음
//region_4depth_name	String	지역 4Depth
//region_type이 법정동이며, 리 영역인 경우만 존재
//code	String	region 코드
//x	Double	X 좌표값, 경위도인 경우 경도(longitude)
//y	Double	Y 좌표값, 경위도인 경우 위도(latitude)


