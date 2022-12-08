package com.example.events_live.common.response

import com.example.events_live.domain.models.Ads

object ListResponse {
    var adsArrayList: ArrayList<Ads> = ArrayList<Ads>()
    var mapArrayList: ArrayList<com.example.events_live.domain.models.Map> = ArrayList<com.example.events_live.domain.models.Map>()
    var prompt_frequency: String? = null
    var prompt_title: String? = null
    var redirect_url: String? = null
    var open_type: String? = null
    var button: String? = null
    var prompt_message: String? = null
    var repeat_status = 0
    var repeat_time:Int = 0

}
