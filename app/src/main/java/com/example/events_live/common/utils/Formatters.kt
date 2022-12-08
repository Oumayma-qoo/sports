package com.example.events_live.common.utils

import android.content.Context
import com.example.events_live.R
import com.example.events_live.domain.models.event.Event

import com.example.events_live.domain.models.event.EventBase
import com.example.events_live.domain.models.event.EventX
import com.example.events_live.domain.models.event.Technic
import com.example.events_live.domain.models.event.formatted.EventKind
import com.example.events_live.domain.models.event.formatted.FormattedEventG_S_F
import com.example.events_live.domain.models.event.formatted.FormattedTecnicEvent

object Formatters {

    fun formatTechnic(technicString: String): ArrayList<FormattedTecnicEvent> {
        val technicEventList=ArrayList<FormattedTecnicEvent>()

        val technics=technicString.split(";")
        for (technic in technics){
            val values=technic.split(",")
            technicEventList.add(FormattedTecnicEvent(values[0],values[1],values[2]))
        }
        return technicEventList
    }

    fun formatEvents(matchId:Int,eventBase: EventBase): ArrayList<FormattedEventG_S_F> {
        val list=ArrayList<FormattedEventG_S_F>()
        val goals=ArrayList<FormattedEventG_S_F>()
        val subs=ArrayList<FormattedEventG_S_F>()
        val fouls=ArrayList<FormattedEventG_S_F>()
        for (event in eventBase.eventList){
            if (event.matchId.toString()==matchId.toString()){
                for (eventX in event.event){
                    when(returnItemTypeForKind(eventX.kind.toString())){
                        EventKind.GOAL -> {
                            goals.add(FormattedEventG_S_F(EventKind.GOAL,eventX))
                        }
                        EventKind.GOAL_HEADING ->{}
                        EventKind.FOUL -> {
                            fouls.add(FormattedEventG_S_F(EventKind.FOUL,eventX))
                        }
                        EventKind.FOUL_HEADING -> {

                        }
                        EventKind.SUBSTITUTION_HEADING -> {

                        }
                        EventKind.SUBSTITUTION -> {
                            subs.add(FormattedEventG_S_F(EventKind.FOUL,eventX))
                        }
                    }
                }
            }
        }
        val emptyEvent= EventX()
        if (goals.isNotEmpty()){
            list.add(FormattedEventG_S_F(EventKind.GOAL_HEADING, emptyEvent))
        }
        list.addAll(goals)
        if (subs.isNotEmpty()){
            list.add(FormattedEventG_S_F(EventKind.SUBSTITUTION_HEADING,emptyEvent))
        }
        list.addAll(subs)

        if (fouls.isNotEmpty()){
            list.add(FormattedEventG_S_F(EventKind.FOUL_HEADING,emptyEvent))

        }
        list.addAll(fouls)

        return list

    }


    fun filterEvents(matchId:Int,eventBase:EventBase): EventBase {

        val filteredEventBase=EventBase()

        val eventList=eventBase.eventList
        val eventArrayList=ArrayList<Event>()
        val technicArrayList= ArrayList<Technic>()
        for (event in eventList){
            if (event.matchId.toString() == matchId.toString()){
                eventArrayList.add(event)
            }
        }
        for (technic in eventBase.technic){
            if (technic.matchId.toString()==matchId.toString()){
                technicArrayList.add(technic)
            }
        }
        
        filteredEventBase.eventList= eventArrayList
        filteredEventBase.technic=technicArrayList
        
        return filteredEventBase
    }

    fun returnItemTypeForKind(kindIndex: String):EventKind{
        return when(kindIndex){
            "1"->{
                EventKind.GOAL
            }
            "2"->{
                EventKind.FOUL
            }
            "3"->{
                EventKind.FOUL
            }
            "7"->{
                EventKind.GOAL
            }
            "8"->{
                EventKind.GOAL
            }
            "9"->{
                EventKind.FOUL
            }
            "11"->{
                EventKind.SUBSTITUTION
            }
            "13"->{
                EventKind.GOAL
            }
            else->{
                EventKind.FOUL
            }
        }
    }

     fun returnKind(kindIndex:String,context: Context):String{
        return when(kindIndex){
            "1"->{
              context.getString(R.string.goal)
            }
            "2"->{
                context.getString(R.string.red_card)
            }
            "3"->{
                context.getString(R.string.yellow_card)
            }
            "7"->{
                context.getString(R.string.penalty_kick)
            }
            "8"->{
                context.getString(R.string.own_goal)
            }
            "9"->{
                context.getString(R.string.two_y_to_red)
            }
            "11"->{
                context.getString(R.string.substitution)
            }
            "13"->{
                context.getString(R.string.missed_pen)
            }
            else->{
                context.getString(R.string.raw)
            }
        }
    }

     fun returnTechnicName(technicIndex:String,context: Context):String{
        return when(technicIndex){
            "1"->{
                context.getString(R.string.tee_off_first)
            }
            "2"->{
                context.getString(R.string.first_corner_kic)
            }
            "3"->{
                context.getString(R.string.first_yellow)
            }
            "4"->{
                context.getString(R.string.number_of_shots)
            }
            "5"->{
                context.getString(R.string.shots_on_tarhet)
            }
            "6"->{
                context.getString(R.string.number_of_fouls)
            }
            "7"->{
                context.getString(R.string.number_of_corners)
            }
            "8"->{
                context.getString(R.string.number_of_corners_ot)
            }
            "9"->{
                context.getString(R.string.free_kicks)
            }
            "10"->{
                context.getString(R.string.number_of_offsides)
            }
            "11"->{
                context.getString(R.string.own_goals)
            }
            "12"->{
                context.getString(R.string.yello_cards)
            }
            "13"->{
                context.getString(R.string.yellow_cards_ot)
            }
            "14"->{
                context.getString(R.string.red_cards)
            }
            "15"->{
                context.getString(R.string.ball_control)
            }
            "16"->{
                context.getString(R.string.header)
            }
            "17"->{
                context.getString(R.string.save_the_ball)
            }
            "18"->{
                context.getString(R.string.goalkeeper_strikes)
            }
            "19"->{
                context.getString(R.string.lose_the_ball)
            }
            "20"->{
                context.getString(R.string.successful_steal)
            }
            "21"->{
                context.getString(R.string.block)
            }
            "22"->{
                context.getString(R.string.long_pass)
            }
            "23"->{
                context.getString(R.string.short_pass)
            }
            "24"->{
                context.getString(R.string.assist)
            }
            "25"->{
                context.getString(R.string.successful_pass)
            }
            "26"->{
                context.getString(R.string.first_sub)
            }
            "27"->{
                context.getString(R.string.last_sub)
            }
            "28"->{
                context.getString(R.string.first_off)
            }
            "29"->{
                context.getString(R.string.last_off)
            }
            "30"->{
                context.getString(R.string.change_num_players)
            }
            "31"->{
                context.getString(R.string.last_corner_kick)
            }
            "32"->{
                context.getString(R.string.last_yello)
            }
            "33"->{
                context.getString(R.string.change_num_players_ot)
            }
            "34"->{
                context.getString(R.string.num_off_ot)
            }
            "35"->{
                context.getString(R.string.miss_goal)
            }
            "36"->{
                context.getString(R.string.middle_column)
            }
            "37"->{
                context.getString(R.string.headers)
            }
            "38"->{
                context.getString(R.string.blocked_shots)
            }
            "39"->{
                context.getString(R.string.tackles)
            }
            "40"->{
                context.getString(R.string.exceeding_times)
            }
            "41"->{
                context.getString(R.string.out_of_bounds)
            }
            "42"->{
                context.getString(R.string.number_of_passes)
            }
            "43"->{
                context.getString(R.string.pass_success_rate)
            }
            "44"->{
                context.getString(R.string.number_of_attacks)
            }
            "45"->{
                context.getString(R.string.dangerous_attacks)
            }
            "46"->{
                context.getString(R.string.half_time_corners)
            }
            "47"->{
                context.getString(R.string.posession)
            }
            else -> {
                "other"
            }
        }
    }

}
