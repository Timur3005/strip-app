package edu.timurmakhmutov.bottomnavstrip.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.timurmakhmutov.bottomnavstrip.R
import edu.timurmakhmutov.bottomnavstrip.databinding.HomeTopForRecyclerBinding
import edu.timurmakhmutov.bottomnavstrip.home.HomePlaceNames

class FindAdapter(private val listener: FindListener): ListAdapter<HomePlaceNames, FindAdapter.FindViewHolder>(
    Comparator()) {

    class FindViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = HomeTopForRecyclerBinding.bind(view)
        fun bind(item: HomePlaceNames, listener: FindListener){
            binding.itemHomeTop.text = item.placeName
            binding.itemHomeTop.setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<HomePlaceNames>(){
        override fun areItemsTheSame(oldItem: HomePlaceNames, newItem: HomePlaceNames): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: HomePlaceNames, newItem: HomePlaceNames): Boolean {
            return oldItem==newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindViewHolder {
        return FindViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.home_top_for_recycler, parent, false))
    }

    override fun onBindViewHolder(holder: FindViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
    interface FindListener{
        fun onClick(item: HomePlaceNames)
    }
}