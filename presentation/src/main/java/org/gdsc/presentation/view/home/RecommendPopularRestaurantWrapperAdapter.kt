package org.gdsc.presentation.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.presentation.R

class RecommendPopularRestaurantWrapperAdapter(
    private val dataList: List<RegisteredRestaurant>
) : RecyclerView.Adapter<RecommendPopularRestaurantWrapperAdapter.WrapperViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WrapperViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommend_populard_restaurant_wrapper, parent, false)
        return WrapperViewHolder(view)
    }

    override fun onBindViewHolder(holder: WrapperViewHolder, position: Int) {
        holder.bind(dataList)
    }

    override fun getItemCount(): Int {
        return 1
    }

    class WrapperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataList: List<RegisteredRestaurant>) {
            val wrapperRecyclerView: RecyclerView = itemView.findViewById(R.id.wrapper_recycler_view)
            val adapter = RecommendPopularRestaurantAdapter(dataList)

            wrapperRecyclerView.adapter = adapter
            wrapperRecyclerView.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        }
    }
}