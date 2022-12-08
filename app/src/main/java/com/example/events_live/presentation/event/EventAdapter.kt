package com.example.events_live.presentation.event

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.events_live.R
import com.example.events_live.common.utils.Formatters
import com.example.events_live.databinding.ItemEventBinding

import com.example.events_live.databinding.ItemTechniqBinding
import com.example.events_live.domain.models.event.formatted.EventKind
import com.example.events_live.domain.models.event.formatted.FormattedEventG_S_F
import com.example.events_live.domain.models.event.formatted.FormattedTecnicEvent

class EventAdapter(private var dataList: ArrayList<FormattedEventG_S_F>) : RecyclerView.Adapter< EventAdapter.EventViewHolder>() {


    private var context: Context? = null

    inner class EventViewHolder(val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemEventBinding.inflate(layoutInflater, parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val element = dataList[position].eventDetail
        val currentItem = dataList[position]
        holder.binding.eventItem = currentItem

        Log.d("QOO", " type :   ${dataList[position].eventType}")
        when (dataList[position].eventType) {
            EventKind.GOAL -> {
                if (element.isHome) {
                    holder.binding.tvHomePlayerName.text = Formatters.returnKind(
                        element.kind.toString(),
                        context!!
                    ) + ": " + element.nameEn
                    holder.binding.tvAwayGoalTime.text = element.time
                    holder.binding.tvKindEventHome.text = context!!.getString(R.string.goal)
                }
                else
                {
                        holder.binding.tvAwayPlayer.text = Formatters.returnKind(
                            element.kind.toString(),
                            context!!
                        ) + ": " + element.nameEn
                    holder.binding.tvAwayGoalTime.text = element.time
                    holder.binding.tvKindEventAway.text = context!!.getString(R.string.goal)

                }


            }

            EventKind.FOUL -> {
                if (element.isHome) {
                    holder.binding.tvHomePlayerName.text = Formatters.returnKind(
                        element.kind.toString(),
                        context!!
                    ) + ": " + element.nameEn
                    holder.binding.tvAwayGoalTime.text = element.time
                    holder.binding.tvKindEventHome.text = context!!.getString(R.string.fouls)

                } else {
                    holder.binding.tvAwayPlayer.text = Formatters.returnKind(
                        element.kind.toString(),
                        context!!
                    ) + ": " + element.nameEn
                    holder.binding.tvAwayGoalTime.text = element.time
                    holder.binding.tvKindEventAway.text = context!!.getString(R.string.fouls)

                }

            }

            EventKind.SUBSTITUTION -> {
                if (element.isHome) {
                    holder.binding.tvHomePlayerName.text =element.nameEn
                    holder.binding.tvAwayGoalTime.text = element.time
                    holder.binding.tvKindEventHome.text=context!!.getString(R.string.subs)

                } else {
                    holder.binding.tvAwayPlayer.text =element.nameEn
                    holder.binding.tvAwayGoalTime.text = element.time
                    holder.binding.tvKindEventAway.text = context!!.getString(R.string.subs)

                }
            }
            else -> {

            }
        }
    }



    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }



    override fun getItemCount(): Int {
        return dataList.size
    }
}