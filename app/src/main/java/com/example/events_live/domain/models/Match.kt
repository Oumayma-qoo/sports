package com.example.events_live.domain.models

import com.google.gson.annotations.SerializedName

data class Match(@SerializedName("animateURL")
                  var animateURL: String = "",
                 @SerializedName("animateURLCn")
                  var animateURLCn: String? = "",
                 @SerializedName("animateURLEn")
                  var animateURLEn: String? = "",
                 @SerializedName("awayCorner")
                  var awayCorner: Int = 0,
                 @SerializedName("awayHalfScore")
                  var awayHalfScore: Int = 0,
                 @SerializedName("awayId")
                  var awayId: Int = 0,
                 @SerializedName("awayLogo")
                  var awayLogo: String = "",
                 @SerializedName("awayName")
                  var awayName: String = "",
                 @SerializedName("awayRank")
                  var awayRank: String = "",
                 @SerializedName("awayRed")
                  var awayRed: Int = 0,
                 @SerializedName("awayScore")
                  var awayScore: Int = 0,
                 @SerializedName("awayYellow")
                  var awayYellow: Int = 0,
                 @SerializedName("color")
                  var color: String = "996600",
                 @SerializedName("explain")
                  var explain: String = "",
                 @SerializedName("extraExplain")
                  var extraExplain: String = "",
                 @SerializedName("groupId")
                  var groupId: Int? = 0,
                 @SerializedName("grouping")
                  var grouping: String = "",
                 @SerializedName("hasLineup")
                  var hasLineup: String = "",
                 @SerializedName("havAnim")
                  var havAnim: Boolean = false,
                 @SerializedName("havAnimCompareAway")
                  var havAnimCompareAway: String? = "",
                 @SerializedName("havAnimCompareHome")
                  var havAnimCompareHome: String? = "",
                 @SerializedName("havAnimComparePercent")
                  var havAnimComparePercent: Int? = 0,
                 @SerializedName("havBriefing")
                  var havBriefing: Boolean = false,
                 @SerializedName("havEvent")
                  var havEvent: Boolean = false,
                 @SerializedName("havLineup")
                  var havLineup: Boolean = false,
                 @SerializedName("havLiveAnchor")
                  var havLiveAnchor: Boolean = false,
                 @SerializedName("havLiveAnchorId")
                  var havLiveAnchorId: String = "",
                 @SerializedName("havLiveAnchorLocale")
                  var havLiveAnchorLocale: String? = "",
                 @SerializedName("havLiveVideo")
                  var havLiveVideo: Boolean = false,
                 @SerializedName("havOdds")
                  var havOdds: Boolean = false,
                 @SerializedName("havPlayerDetails")
                  var havPlayerDetails: Boolean = false,
                 @SerializedName("havTech")
                  var havTech: Boolean = false,
                 @SerializedName("havTextLive")
                  var havTextLive: Boolean = false,
                 @SerializedName("homeCorner")
                  var homeCorner: Int = 0,
                 @SerializedName("homeHalfScore")
                  var homeHalfScore: Int = 0,
                 @SerializedName("homeId")
                  var homeId: Int = 0,
                 @SerializedName("homeLogo")
                  var homeLogo: String = "",
                 @SerializedName("homeName")
                  var homeName: String = "",
                 @SerializedName("homeRank")
                  var homeRank: String = "",
                 @SerializedName("homeRed")
                  var homeRed: Int = 0,
                 @SerializedName("homeScore")
                  var homeScore: Int = 0,
                 @SerializedName("homeYellow")
                  var homeYellow: Int = 0,
                 @SerializedName("isHidden")
                  var isHidden: Boolean = false,
                 @SerializedName("isNeutral")
                  var isNeutral: Boolean = false,
                 @SerializedName("kind")
                  var kind: Int = 0,
                 @SerializedName("leagueId")
                  var leagueId: Int = 0,
                 @SerializedName("leagueName")
                  var leagueName: String = "",
                 @SerializedName("leagueNameShort")
                  var leagueNameShort: String = "",
                 @SerializedName("location")
                  var location: String = "",
                 @SerializedName("matchId")
                  var matchId: Int = 0,
                 @SerializedName("matchTime")
                  var matchTime: String = "",
                 @SerializedName("odds")
                  var odds: Odds = Odds(),
                 @SerializedName("round")
                  var round: String = "",
                 @SerializedName("season")
                  var season: String = "",
                 @SerializedName("startTime")
                  var startTime: String = "",
                 @SerializedName("state")
                  var state: Int = 0,
                 @SerializedName("subLeagueId")
                  var subLeagueId: String = "",
                 @SerializedName("subLeagueName")
                  var subLeagueName: String = "",
                 @SerializedName("temp")
                  var temp: String = "",
                 @SerializedName("videoDetail")
                  var videoDetail: VideoDetail? = VideoDetail(),
                 @SerializedName("videoId")
                  var videoId: String? = "",
                 @SerializedName("weather")
                  var weather: String = "")
