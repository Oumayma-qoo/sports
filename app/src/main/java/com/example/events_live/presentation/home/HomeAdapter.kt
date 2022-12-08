package com.example.events_live.presentation.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.events_live.common.constant.Constants
import com.example.events_live.common.utils.CustomBindingAdapters.loadAwayImage
import com.example.events_live.common.utils.CustomBindingAdapters.loadImage
import com.example.events_live.common.utils.GeneralTools
import com.example.events_live.common.utils.SPApp
import com.example.events_live.databinding.ItemHomeBinding
import com.example.events_live.domain.models.Match
import kotlinx.android.synthetic.main.item_home.*

class HomeAdapter(private var dataList: ArrayList<Match>, var loadMore :LoadMoreItems) :
    ListAdapter<Match, HomeAdapter.HomeViewHolder>(Companion) {


    interface OnItemTap {
        fun onTap(position: Int)
    }

    private var onTapListener: OnItemTap? = null
    private var context: Context? = null
    var hashMap: HashMap<String, String> = HashMap<String, String>()
    var time: String? = ""
    fun setItemTapListener(l: OnItemTap) {
        onTapListener = l
    }

    open inner class HomeViewHolder(val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<Match>() {
        override fun areItemsTheSame(
            oldItem: Match,
            newItem: Match
        ): Boolean =
            oldItem.matchId == newItem.matchId

        override fun areContentsTheSame(
            oldItem: Match,
            newItem: Match
        ): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeBinding.inflate(layoutInflater, parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val sp=SPApp(context!!)

        try {

            val currentItem = dataList[position]
            holder.binding.matchItem = currentItem
            holder.binding.tvHomeScore.text = currentItem.homeScore.toString()
            holder.binding.tvAwayScore.text = currentItem.awayScore.toString()
            loadImage(holder.binding.imgHomeTeam, currentItem.homeLogo)
            loadAwayImage(holder.binding.imgAwayTeam, currentItem.awayLogo)
            holder.binding.position = position
            holder.binding.clickListener = onTapListener
            holder.binding.executePendingBindings()
            if (!currentItem.startTime.isNullOrEmpty()) {
                // "matchTime": "2022/8/25 16:30:00",
                try {
                    holder.binding.tvTime.text = GeneralTools.returnStateDate(currentItem)
                    time = holder.binding.tvTime.text.toString()

                } catch (e: Exception) {
                    Log.d("EXCEPTION!!!", e.toString())

                }
            } else {
                holder.binding.tvTime.text =
                    GeneralTools.returnTimeGeneralized(currentItem.matchTime)
                time = holder.binding.tvTime.text.toString()

            }

            if (currentItem.havAnim){
                holder.binding.tvAnimation.setVisibility(View.VISIBLE)
            }
            else
                holder.binding.tvAnimation.setVisibility(View.INVISIBLE)


        } catch (e: Exception) {
        }

        if (position==dataList.size-1){
            loadMore.loadMore()
        }

    }

    fun getMatchId(position: Int): Int {
        val currentItem = getItem(position)
        return currentItem.matchId
    }

    fun CheckEvent(position: Int): Boolean {
        val currentItem = getItem(position)
        return currentItem.havEvent
    }
  fun CheckAnim(position: Int): Boolean {
        val currentItem = getItem(position)
        return currentItem.havAnim
    }




    fun getInformation(position: Int): HashMap<String, String> {
        val currentItem = getItem(position)
        hashMap["leagueName"] = currentItem.leagueName
        hashMap["homeName"] = currentItem.homeName
        hashMap["awayName"] = currentItem.awayName
        hashMap["homeScore"] = currentItem.homeScore.toString()
        hashMap["AwayScore"] = currentItem.awayScore.toString()
        hashMap["LogoHome"] = currentItem.homeLogo
        hashMap["AwayLogo"] = currentItem.awayLogo
        hashMap["time"] = time.toString()
        hashMap["animationUrl"] = currentItem.animateURLEn.toString()
        return hashMap
    }


    fun filterList(filterNames: ArrayList<Match>) {
        dataList = filterNames
        notifyDataSetChanged()
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    interface LoadMoreItems{
        fun loadMore()
    }
}