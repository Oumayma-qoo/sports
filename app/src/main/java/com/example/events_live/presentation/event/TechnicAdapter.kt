package com.example.events_live.presentation.event

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.events_live.common.utils.Formatters
import com.example.events_live.databinding.ItemTechniqBinding
import com.example.events_live.domain.models.event.formatted.FormattedTecnicEvent

class TechnicAdapter(private var dataList: ArrayList<FormattedTecnicEvent>) : RecyclerView.Adapter< TechnicAdapter.TechnicViewHolder>() {


    private var context: Context? = null

    inner class TechnicViewHolder(val binding: ItemTechniqBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechnicViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTechniqBinding.inflate(layoutInflater, parent, false)
        return TechnicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TechnicViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.binding.eventItem = currentItem
        holder.binding.technicName.text= Formatters.returnTechnicName(dataList[position].tecnicName, context!!)
        holder.binding.homeTechnicValue.text=dataList[position].homeCount
        holder.binding.awayTechnicValue.text=dataList[position].awayCount

        if (dataList[position].homeCount.contains("%"))
        {
            val homeCountSplit=dataList[position].homeCount.substringBefore("%")
            val width=homeCountSplit.toDouble()/100
            (holder.binding.homeIndicator.layoutParams as ConstraintLayout.LayoutParams)
                .matchConstraintPercentWidth=width.toFloat()
        }
        else{
            try {
                (holder.binding.homeIndicator.layoutParams as ConstraintLayout.LayoutParams)
                    .matchConstraintPercentWidth=
                    returnHomePercentage(dataList[position].homeCount.toDouble(),dataList[position].awayCount.toDouble()).toFloat()
            }catch (e:Exception){

            }

        }
        holder.binding.executePendingBindings()


    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    private fun returnHomePercentage(homeScore: Double, awayScore: Double): Double {
        val total = homeScore + awayScore
        return homeScore / total
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}