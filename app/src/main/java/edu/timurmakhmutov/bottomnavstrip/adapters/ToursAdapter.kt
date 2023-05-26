package edu.timurmakhmutov.bottomnavstrip.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.timurmakhmutov.bottomnavstrip.R
import edu.timurmakhmutov.bottomnavstrip.databinding.HomeTopForRecyclerBinding
import edu.timurmakhmutov.bottomnavstrip.data_classes.ToursNames

class ToursAdapter(val listener: ToursListener) : ListAdapter<ToursNames, ToursAdapter.ToursViewHolder>(
    Comparator()) {

    class ToursViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = HomeTopForRecyclerBinding.bind(view)
        fun bind(item: ToursNames, listener: ToursListener){
            binding.itemHomeTop.text = item.name
            binding.itemHomeTop.setOnClickListener {
                listener.onClick(item)
            }
        }
    }
    class Comparator : DiffUtil.ItemCallback<ToursNames>(){
        override fun areItemsTheSame(oldItem: ToursNames, newItem: ToursNames): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: ToursNames, newItem: ToursNames): Boolean {
            return oldItem==newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToursViewHolder {
        return ToursViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_top_for_recycler, parent,false))
    }

    override fun onBindViewHolder(holder: ToursViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
    interface ToursListener{
        fun onClick(item: ToursNames)
    }

}