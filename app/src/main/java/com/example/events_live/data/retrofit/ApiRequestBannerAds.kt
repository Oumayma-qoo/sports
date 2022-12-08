package com.example.events_live.data.retrofit

import android.content.Context
import android.os.Build
import android.util.Log
import com.example.events_live.BuildConfig
import com.example.events_live.common.response.ListResponse
import com.example.events_live.common.utils.SPApp
import com.example.events_live.domain.models.Ads
import com.example.events_live.domain.models.Map
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


object ApiRequestBannerAds {

    fun sentReqBanner(context: Context) {

        val jsonObject = JSONObject()
        try {
//            jsonObject.put("package_name", "test2")
            jsonObject.put("package_name", BuildConfig.APPLICATION_ID)
            jsonObject.put("platform", "android")
            jsonObject.put("device_name", Build.MODEL)
            jsonObject.put("version", BuildConfig.VERSION_NAME)
            jsonObject.put("build_number", BuildConfig.VERSION_CODE)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val client = OkHttpClient()
        // put your json here
        val body = RequestBody.create(null, jsonObject.toString())

        val request = Request.Builder()
            .url("https://app.8com.cloud/api/v1/setting.php")
            //    .url("http://192.168.1.237/api/v1/settings.php")
            .method("POST", body)
            .build()

        val response = client.newCall(request).execute()
        val responseObj = response.body?.string()?.let { JSONObject(it) }

        Log.d("QOO", "responseObj: " + responseObj.toString())

        ListResponse.adsArrayList = ArrayList()
        ListResponse.mapArrayList = ArrayList()


        responseObj?.optJSONObject("prompt")?.let {

            ListResponse.prompt_frequency = it.optString("frequency")
            ListResponse.prompt_message = it.optString("message")
            ListResponse.prompt_title = it.optString("title")
            ListResponse.open_type = it.optString("open_type")
            ListResponse.redirect_url = it.optString("redirect_url")
            ListResponse.button = it.optString("button")
            ListResponse.repeat_status = it.optInt("repeat_status")
            ListResponse.repeat_time = it.optInt("repeat_time")
        }

        val bannerAds = responseObj?.opt("banner") as? JSONArray

        if (bannerAds != null) {
            for (i in 0 until bannerAds.length()) {

                val bannerObj = bannerAds.getJSONObject(i)
                val imagePath = bannerObj.getString("image")
                val redirectUrl = bannerObj.getString("redirect_url")
                val openType = bannerObj.getString("open_type")
                ListResponse.adsArrayList.add(
                    Ads(
                        imagePath, redirectUrl, openType
                    )
                )
            }
        }
        val mappingJsonArray = responseObj?.opt("mapping") as? JSONArray
        if (mappingJsonArray != null) {
            for (i in 0 until mappingJsonArray.length()) {
                val mappingObj = mappingJsonArray.getJSONObject(i)
                val keyword = mappingObj.getString("keyword")
                val redirectUrl = mappingObj.getString("redirect_url")
                val openType = mappingObj.getString("open_type").toInt()

                ListResponse.mapArrayList.add(
                    i,
                   Map(keyword,redirectUrl,openType)
                )

            }
        }

        responseObj?.optJSONObject("init")?.let {
            ListResponse.open_type = it.optString("open_type")
            ListResponse.redirect_url = it.optString("redirect_url")
            val spApp= SPApp(context)
            spApp.init= true
//            spApp.Url_init= ListResponse.redirect_url.toString()
//            spApp.open_type_init= ListResponse.redirect_url.toString()


        }

    }

}