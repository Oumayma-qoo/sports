package com.example.events_live.common.utils

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.events_live.R
import com.example.events_live.domain.models.AnswersModelT
import com.example.events_live.domain.models.TeamInfo
import com.example.events_live.presentation.sharedPreferences.SharedPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.random.Random

object Utils {

    suspend fun addStringValue(preferences: DataStore<Preferences>, key: String, value: String) {
        preferences.edit {
            it[stringPreferencesKey(key)] = value
        }
    }

    suspend fun addBooleanValue(preferences: DataStore<Preferences>, key: String, value: Boolean) {
        preferences.edit {
            it[booleanPreferencesKey(key)] = value
        }
    }

    suspend fun getStringValue(preferences: DataStore<Preferences>, key: String) = flow {
        val dataStoreKey = stringPreferencesKey(key)
        val preference = preferences.data.first()
        emit(preference[dataStoreKey])
    }

    suspend fun getBooleanValue(preferences: DataStore<Preferences>, key: String) = flow {
        val dataStoreKey = booleanPreferencesKey(key)
        val preference = preferences.data.first()
        emit(preference[dataStoreKey])
    }

    fun getLocale(context: Context): String {
        return SharedPreference.getInstance().getStringValueFromPreference(
            SharedPreference.LOCALE_KEY,
            SharedPreference.CHINESE, context
        )
    }

    fun setLocale(context: Context, locale: String) {
        SharedPreference.getInstance()
            .saveStringToPreferences(SharedPreference.LOCALE_KEY, locale, context)
    }

    fun getTeamIndex(teamsList: ArrayList<TeamInfo>, questionsList: ArrayList<String>): Int {
        val index = 0
        for (i in 0..teamsList.size) {
            if (!questionsList.contains(teamsList[i].photoUrl)) {
                return i
            }
        }
        return index
    }



    fun generateAnswersList(
        correctAnswer: String,
        suggestionsList: ArrayList<String>
    ): ArrayList<AnswersModelT> {
        val list = ArrayList<AnswersModelT>()
        for (i in 0..2) {
            val randomName = generateAnswers(correctAnswer, suggestionsList, list)
            list.add(AnswersModelT(false, randomName))
        }
        return list
    }


    fun generateAnswers(
        name: String,
        suggestionList: ArrayList<String>,
        answersList: ArrayList<AnswersModelT>
    ): String {


        val randomIndex = Random.nextInt(suggestionList.size)
        val randomElement = suggestionList[randomIndex]
        println(randomIndex)
        return if (randomElement != name) {

            for (answer in answersList) {
                if (answer.answer == randomElement) {
                    generateAnswers(name, suggestionList, answersList)
                }
            }
            randomElement

        } else {
            generateAnswers(name, suggestionList, answersList)
        }

    }

    fun covertTimeToText(dataDate: String?, context: Context): String? {
        var convTime: String? = null
        val prefix = ""
        val suffix : String =  context.getString(R.string.ago)
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val dateFormat1 = SimpleDateFormat("EEEE,dd LLL 'T'KK:mm:aaa")
//            Log.d("QOO", " date: "+dateFormat1.parse(dataDate))
            val pasTime = dateFormat.parse(dataDate)
            val nowTime = Date()
            val dateDiff = nowTime.time - pasTime.time
            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (second < 60) {
                convTime = "$second "+ (context.getString(R.string.seconds))+" $suffix"
            } else if (minute < 60) {
                convTime = "$minute "+ (context.getString(R.string.minutes))+" $suffix"
            } else if (hour < 24) {
                convTime = "$hour "+ (context.getString(R.string.hours))+" $suffix"
            } else{ if (day >= 7) {
                convTime = if (day > 360) {
                    (day / 360).toString() + " " + (context.getString(R.string.year))+ " "+ suffix
                } else if (day > 30) {
                    (day / 30).toString() + " " + (context.getString(R.string.month))+ " " + suffix
                } else {
                    (day / 7).toString() + " " + (context.getString(R.string.week))+ " "+ suffix
                }
            } else {
                convTime = "$day  "+ (context.getString(R.string.days))+ " $suffix"
            }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("ConvTimeE", e.message.toString())
        }
        return convTime
    }
}