package com.newOs.colorCraze.ui.mainMode.scoresHistory.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.newOs.colorCraze.R
import com.newOs.colorCraze.ui.mainMode.scoresHistory.model.ScoreItem

class RecyclerViewAdapter(private val scoresList:List<ScoreItem>): RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = scoresList[position]
        holder.scoreRank.text=currentItem.rank.toString()
        holder.scoreDate.text=currentItem.data
        holder.score.text=currentItem.score.toString()
    }

    override fun getItemCount(): Int = scoresList.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val scoreRank: TextView = itemView.findViewById(R.id.rank)
        val scoreDate: TextView = itemView.findViewById(R.id.date)
        val score: TextView = itemView.findViewById(R.id.score)
    }

}