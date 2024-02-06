package org.gdsc.presentation.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.gdsc.presentation.R

class RecommendPopularRestaurantTitleAdapter(private val titleText: String)
    : RecyclerView.Adapter<RecommendPopularRestaurantTitleAdapter.TitleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommend_popular_restaurant_title, parent, false)
        return TitleViewHolder(view)
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        holder.bind(titleText)
    }

    override fun getItemCount(): Int {
        return 1
    }

    class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTv)

        fun bind(titleText: String) {
            titleTextView.text = titleText
        }
    }
}