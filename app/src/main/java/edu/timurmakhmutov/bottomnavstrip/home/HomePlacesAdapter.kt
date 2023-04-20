package edu.timurmakhmutov.bottomnavstrip.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.timurmakhmutov.bottomnavstrip.R
import edu.timurmakhmutov.bottomnavstrip.databinding.HomeTopForRecyclerBinding
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
class HomePlacesAdapter() : ListAdapter<HomePlaceNames, HomePlacesAdapter.HomeNamesHolder>(Comparator()) {

    //класс для создания метода заполнения вью информацией с сервера
    class HomeNamesHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = HomeTopForRecyclerBinding.bind(view)
        fun bind(item: HomePlaceNames) = with(binding){
            itemHomeTop.text = item.placeName
            itemHomeTop.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_homeFragment_to_placeScreenFragment)
            }
        }
    }
    //сравнение новыой информации с сервера с новой
    class Comparator : DiffUtil.ItemCallback<HomePlaceNames>(){
        override fun areItemsTheSame(oldItem: HomePlaceNames, newItem: HomePlaceNames): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: HomePlaceNames, newItem: HomePlaceNames): Boolean {
            return oldItem==newItem
        }

    }
    //создание вью
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeNamesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_top_for_recycler, parent,false)
        return HomeNamesHolder(view)
    }
    //заполнение инфы во вью
    override fun onBindViewHolder(holder: HomeNamesHolder, position: Int) {
        holder.bind(getItem(position))
    }

}