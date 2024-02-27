package org.gdsc.presentation.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.gdsc.presentation.R

class EmptyAdapter: RecyclerView.Adapter<EmptyAdapter.EmptyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmptyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_empty_, parent, false)
        return EmptyViewHolder(view)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: EmptyViewHolder, position: Int) { }

    class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}