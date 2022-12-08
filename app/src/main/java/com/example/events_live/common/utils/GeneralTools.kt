package com.example.events_live.common.utils


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.events_live.BuildConfig
import com.example.events_live.R
import com.example.events_live.common.constant.Constants.GMT_OFFSET_IN_MS
import com.example.events_live.common.constant.Constants.HOUR_CONSTANT
import com.example.events_live.common.constant.Constants.MINS_CONSTANT
import com.example.events_live.common.response.ListResponse.prompt_message
import com.example.events_live.common.response.ListResponse.prompt_title
import com.example.events_live.domain.models.Match
import java.text.SimpleDateFormat
import java.util.*


object GeneralTools {
    val dateFormatType1="yyyy/M/dd HH:mm:ss"
    val dateFormatType2="yyyy-M-dd HH:mm:ss"
    fun messageDialog(activity:Activity){

        val dialog=Dialog(activity,android.R.style.ThemeOverlay)
        dialog.setContentView(R.layout.popup_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.findViewById<TextView>(R.id.title_message).setText(prompt_title)
        dialog.findViewById<TextView>(R.id.message).setText(prompt_message)

        dialog.findViewById<View>(R.id.ok_bt).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    fun returnTimeGeneralized(time: String):String{
        return try {
            val sdf= SimpleDateFormat(dateFormatType1)
            val sdf2= SimpleDateFormat("EEE, dd MMM")
            sdf2.format(sdf.parse(time).time)
        }catch (e:Exception){
            val sdf= SimpleDateFormat(dateFormatType2)
            val sdf2= SimpleDateFormat("EEE, dd MMM")
            sdf2.format(sdf.parse(time).time)
        }

    }
    @SuppressLint("SimpleDateFormat")
    fun getCalculatedDate(dateFormat: String?, days: Int): String{
        val cal: Calendar = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat)
        cal.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(cal.timeInMillis))
    }
    fun getPastWeekDates():List<String>{
        val datesList= ArrayList<String>()
        for (i in -1 downTo -8){
            datesList.add(getCalculatedDate("E|yyyy-MM-dd",i))
        }
        return datesList
    }
    fun getFutureDates():List<String>{
        val datesList= ArrayList<String>()
        for (i in 1 .. 8){
            datesList.add(getCalculatedDate("E|yyyy-MM-dd",i))
        }
        return datesList
    }

    fun return24HrsOnly(matchTime:String):String{
        val splitDateTime=matchTime.split(" ")[1].split(":")
        return splitDateTime[0]+":"+splitDateTime[1]
    }


    fun returnTimeInGmtCurrentLocale():Long{
        val timeZone= TimeZone.getDefault().getDisplayName(false,TimeZone.SHORT)
        val time=timeZone.substringAfter("+")
        var hours=time.substringBefore(":").toLong()
        var minutes=time.substringAfter(":").toLong()
        hours *= HOUR_CONSTANT
        minutes *= MINS_CONSTANT
        val timeFinal= if (timeZone.contains("+")){
            System.currentTimeMillis()-(hours+minutes)
        }else{
            System.currentTimeMillis()+(hours+minutes)
        }
        return timeFinal
    }
    fun returnGMTTimeInMs(time: String): Long {
        try {
            val sdf = SimpleDateFormat(dateFormatType1)
            return sdf.parse(time).time - GMT_OFFSET_IN_MS
        }catch (e:Exception){
            val sdf = SimpleDateFormat(dateFormatType2)
            return sdf.parse(time).time -GMT_OFFSET_IN_MS
        }

    }


    fun returnMinutes(dataObject: Match):String
    {
        val startTimeMs=returnGMTTimeInMs(dataObject.startTime)
        val matchTimeMs=returnGMTTimeInMs(dataObject.matchTime)
        var minutesElapsed=returnTimeInGmtCurrentLocale()-startTimeMs
        minutesElapsed /= 60000
        return minutesElapsed.toString()
    }

    fun returnStateDate(dataObject:Match):String{
        try {
            val sdf=SimpleDateFormat(dateFormatType1)
            val sdf2=SimpleDateFormat("EEE, dd MMM")
            return when(dataObject.state){
                0->{
                    sdf2.format(sdf.parse(dataObject.matchTime).time)
                }
                1->{
                    "FH "+returnMinutes(dataObject)+" \'"
                }
                2->{
                    "HT "+returnMinutes(dataObject)+" \'"
                }
                3->{
                    "SH "+returnMinutes(dataObject)+" \'"
                }
                4-> {
                    "OT "+returnMinutes(dataObject)+" \'"
                }
                5-> {
                    "PT "+returnMinutes(dataObject)+" \'"
                }
                -1-> {
                    "FT"
                }
                -10-> {
                    "CL"
                }
                -11-> {
                    "TBD"
                }
                -12-> {
                    "CIH"
                }
                -13-> {
                    "INT"
                }
                -14-> {
                    "DEL"
                }
                else -> {
                    "Soon"
                }
            }

        }
        catch (e:Exception){
            val sdf=SimpleDateFormat(dateFormatType2)
            val sdf2=SimpleDateFormat("EEE, dd MMM")
            return when(dataObject.state){
                0->{
                    sdf2.format(sdf.parse(dataObject.matchTime).time)
                }
                1->{
                    "FH "+returnMinutes(dataObject)+" \'"
                }
                2->{
                    "HT "+returnMinutes(dataObject)+" \'"
                }
                3->{
                    "SH "+returnMinutes(dataObject)+" \'"
                }
                4-> {
                    "OT "+returnMinutes(dataObject)+" \'"
                }
                5-> {
                    "PT "+returnMinutes(dataObject)+" \'"
                }
                -1-> {
                    "FT"
                }
                -10-> {
                    "CL"
                }
                -11-> {
                    "TBD"
                }
                -12-> {
                    "CIH"
                }
                -13-> {
                    "INT"
                }
                -14-> {
                    "DEL"
                }
                else -> {
                    "Soon"
                }
            }

        }

    }
    fun returnGMTToCurrentTimezone(timeMs:Long):Long{

        val timeZone= TimeZone.getDefault().getDisplayName(false,TimeZone.SHORT)
        val time=timeZone.substringAfter("+")
        var hours=time.substringBefore(":").toLong()
        var minutes=time.substringAfter(":").toLong()
        hours *= HOUR_CONSTANT
        minutes *= MINS_CONSTANT
        val timeFinal= if (timeZone.contains("+")){
            timeMs+(hours+minutes)
        }else{
            timeMs-(hours+minutes)
        }
        return timeFinal
    }

    fun returnTime(date:String):String{
        return try {
            val sdf = SimpleDateFormat(dateFormatType1)
            val time=returnGMTToCurrentTimezone(returnGMTTimeInMs(date))
            sdf.format(time)
        }catch (e:Exception){
            val sdf = SimpleDateFormat(dateFormatType2)
            val time=returnGMTToCurrentTimezone(returnGMTTimeInMs(date))
            sdf.format(time)
        }

    }
    fun exitDialog(activity:Activity){
        val dialog=Dialog(activity,android.R.style.ThemeOverlay)
        dialog.setContentView(R.layout.exit_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.findViewById<View>(R.id.yes_bt).setOnClickListener {
            activity.finish()
        }
        dialog.findViewById<ImageView>(R.id.ivCrossExitDialog).setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.no_bt).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    fun shareApp(activity: Activity){
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                activity.getResources().getString(R.string.app_name).toString() + " App"
            )
            var shareMessage = """
                 
                 Let me recommend you this application
                 
                 
                 """.trimIndent()
            shareMessage =
                """
                 ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                 
                 
                 """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            activity.startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
            e.toString()
        }
    }
    fun feedback(activity: Activity){

        val i = Intent(Intent.ACTION_SEND)
        i.type = "*/*"
        // i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(crashLogFile));
        // i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(crashLogFile));
        i.putExtra(
            Intent.EXTRA_EMAIL,
            arrayOf<String>(activity.getResources().getString(R.string.account_email))
        )
        i.putExtra(
            Intent.EXTRA_SUBJECT,
            activity.getResources().getString(R.string.app_name).toString() + " FeedBack"
        )

        try {
            activity.startActivity(createEmailOnlyChooserIntent(activity,i, "Send via email"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                activity,
                "There is no email client installed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun createEmailOnlyChooserIntent(
        activity: Activity,
        source: Intent?,
        chooserTitle: CharSequence?
    ): Intent? {
        val intents = Stack<Intent>()
        val i = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",
                "", null
            )
        )
        val activities: List<ResolveInfo> =activity.getPackageManager()
            .queryIntentActivities(i, 0)
        for (ri in activities) {
            val target = Intent(source)
            target.setPackage(ri.activityInfo.packageName)
            intents.add(target)
        }
        return if (!intents.isEmpty()) {
            val chooserIntent = Intent.createChooser(
                intents.removeAt(0),
                chooserTitle
            )
            chooserIntent.putExtra(
                Intent.EXTRA_INITIAL_INTENTS,
                intents.toTypedArray<Parcelable>()
            )
            chooserIntent
        } else {
            Intent.createChooser(source, chooserTitle)
        }
    }

    fun privacyPolicy(context: Context) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.privacy_policy_link)))
        context.startActivity(browserIntent)
    }

    fun rateUs(activity: Activity) {

        val appPackageName: String = activity.getPackageName()
        try {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri
                        .parse("market://details?id=$appPackageName")
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "http://play.google.com/store/apps/details?id="
                                + appPackageName
                    )
                )
            )
        }
    }


}