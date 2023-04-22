package edu.timurmakhmutov.bottomnavstrip.place_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import edu.timurmakhmutov.bottomnavstrip.R
import edu.timurmakhmutov.bottomnavstrip.databinding.CommentsBinding

class CommentsAdapter():androidx.recyclerview.widget.ListAdapter<PlaceCommentsFields, CommentsAdapter.PlaceViewHolder>(Comparator()) {

    class PlaceViewHolder(view: View):RecyclerView.ViewHolder(view){
        private val binding = CommentsBinding.bind(view)
        fun bind(item: PlaceCommentsFields) = with(binding){
            commentName.text = item.commenterName
            commentBody.text = item.commentBody
        }
    }
    class Comparator : DiffUtil.ItemCallback<PlaceCommentsFields>(){
        override fun areItemsTheSame(oldItem: PlaceCommentsFields, newItem: PlaceCommentsFields): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: PlaceCommentsFields, newItem: PlaceCommentsFields): Boolean {
            return oldItem==newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comments, parent,false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}