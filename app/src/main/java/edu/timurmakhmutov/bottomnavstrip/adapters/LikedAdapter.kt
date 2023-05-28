package edu.timurmakhmutov.bottomnavstrip.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDB
import edu.timurmakhmutov.bottomnavstrip.R
import edu.timurmakhmutov.bottomnavstrip.databinding.LikedItemBinding


//adapter for elected places
internal class LikedAdapter(private val listener: DBListener): ListAdapter<TableForDB, LikedAdapter.LikedViewHolder>(
    Comparator()) {
    class LikedViewHolder(view:View): RecyclerView.ViewHolder(view){
        val binding = LikedItemBinding.bind(view)
        fun bind(item: TableForDB, listener: DBListener){
            binding.likedTv.text = item.title
            binding.likedTv.setOnClickListener{
                listener.dbOnClick(item)

            }
            binding.likedTv.setOnLongClickListener {
                listener.longClick(item, it)
                true
            }
        }
    }
    class Comparator : DiffUtil.ItemCallback<TableForDB>(){
        override fun areItemsTheSame(oldItem: TableForDB, newItem: TableForDB): Boolean {
            return oldItem==newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: TableForDB, newItem: TableForDB): Boolean {
            return oldItem==newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.liked_item, parent, false)
        return LikedViewHolder(view)
    }

    override fun onBindViewHolder(holder: LikedViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    interface DBListener{
        fun dbOnClick(item: TableForDB)
        fun longClick(item: TableForDB, view:View)
    }

}