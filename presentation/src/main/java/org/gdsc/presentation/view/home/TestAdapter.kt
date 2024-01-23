package org.gdsc.presentation.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.gdsc.presentation.databinding.TestItemBinding

class TestAdapter(val dataList: List<String>) : RecyclerView.Adapter<TestAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: TestItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(textString: String) {
            binding.text.text = textString
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            TestItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList[position])
    }
}