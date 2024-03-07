package com.nbc.shownect.ui.search

data class SearchFIlterData(val name : String, val code : String)

object SearchFilterManager {
    val genreList = listOf<SearchFIlterData>(
        SearchFIlterData("서커스/마술","EEEB"),
        SearchFIlterData("클래식","CCCA"),
        SearchFIlterData("cpBottomFutility","BBBC"),
        SearchFIlterData("복합" , "EEEA"),
        SearchFIlterData("뮤지컬" , "GGGA"),
        SearchFIlterData("국악" , "CCCC"),
        SearchFIlterData("대중음악" , "CCCD"),
        SearchFIlterData("대중무용" , "BBBE"),
        SearchFIlterData("연극", "AAAA")
    )

    val addrList = listOf<SearchFIlterData>(
        SearchFIlterData("서울","11"),
        SearchFIlterData("부산","26"),
        SearchFIlterData("대구","27"),
        SearchFIlterData("인천","28"),
        SearchFIlterData("광주","29"),
        SearchFIlterData("대전","30"),
        SearchFIlterData("울산","31"),
        SearchFIlterData("세종","36"),
        SearchFIlterData("경기","41"),
        SearchFIlterData("강원","51"),
        SearchFIlterData("충북","43"),
        SearchFIlterData("충남","44"),
        SearchFIlterData("전북","45"),
        SearchFIlterData("전남","46"),
        SearchFIlterData("경북","47"),
        SearchFIlterData("경남","48"),
        SearchFIlterData("제주","50")
    )

    val childList = listOf<SearchFIlterData>(
        SearchFIlterData("가능" , "Y"),
        SearchFIlterData("불가능" , "N")
    )

}