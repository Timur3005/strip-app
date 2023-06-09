package edu.timurmakhmutov.bottomnavstrip.ui

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDB
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDBRepository
import edu.timurmakhmutov.bottomnavstrip.R
import edu.timurmakhmutov.bottomnavstrip.adapters.LikedAdapter
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentLKBinding
import edu.timurmakhmutov.bottomnavstrip.view_model.LKViewModel

class LKFragment : Fragment(), LikedAdapter.DBListener {

    private val tableForDBRepository = TableForDBRepository(Application())
    private lateinit var lkBinding: FragmentLKBinding
    private lateinit var likedAdapter: LikedAdapter
    private lateinit var pathAdapter: LikedAdapter
    private val model: LKViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lkBinding = FragmentLKBinding.inflate(inflater, container, false)
        return lkBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tableForDBRepository = TableForDBRepository(Application())

        //selected showing
        tableForDBRepository.allLiked.observe(viewLifecycleOwner, Observer { tableForDBS ->
            model.liveDataLiked.value = tableForDBS
            initRecyclerLiked()
            updateLiked()
        })

        //path showing
        tableForDBRepository.allPaths.observe(viewLifecycleOwner, Observer {
            model.liveDataPath.value = it
            initRecyclerPath()
            updatePath()
        })

        // local db cleaning
        tableForDBRepository.allTables.observe(viewLifecycleOwner, Observer {
            for (item in it){
                if (item.inLiked==0 && item.inPath==0){
                    tableForDBRepository.delete(item)
                }
            }
        })


    }

    //selected and path recyclers initialization
    private fun initRecyclerLiked(){
        likedAdapter = LikedAdapter(this)
        lkBinding.recyclerForLikedInLk.layoutManager = LinearLayoutManager(context)
        lkBinding.recyclerForLikedInLk.adapter = likedAdapter
    }
    private fun updateLiked(){
        model.liveDataLiked.observe(viewLifecycleOwner){
            likedAdapter.submitList(it)
        }
    }
    private fun initRecyclerPath(){
        pathAdapter = LikedAdapter(this)
        lkBinding.recyclerForTripInLk.layoutManager = LinearLayoutManager(context)
        lkBinding.recyclerForTripInLk.adapter = pathAdapter
    }
    private fun updatePath(){
        model.liveDataPath.observe(viewLifecycleOwner){
            pathAdapter.submitList(it)
        }
    }


    //click listeners for recycler`s items
    //short click
    override fun dbOnClick(item: TableForDB) {
        val bundle = Bundle()
        bundle.putString("1",item.identification)
        findNavController(lkBinding.root).navigate(R.id.action_LKFragment_to_placeCardInLKFragment, bundle)
    }
    //long click
    override fun longClick(item: TableForDB, view: View) {
        val popupMenu = PopupMenu(context, view)
        if (item.inLiked==0 && item.inPath==1){
            popupMenu.inflate(R.menu.set_delete_menu)
            popupMenu.setOnMenuItemClickListener{
                when(it.itemId){
                    R.id.action_set_in_like->{
                        tableForDBRepository.updateLiked(item.identification,1)
                        true
                    }
                    R.id.action_delete_from_path->{
                        tableForDBRepository.updatePath(item.identification,0)
                        true
                    }
                    else -> false
                }
            }
        }
        else if (item.inLiked==1 && item.inPath == 0){
            popupMenu.inflate(R.menu.delete_set_menu)
            popupMenu.setOnMenuItemClickListener{
                when(it.itemId){
                    R.id.action_set_in_like->{
                        tableForDBRepository.updateLiked(item.identification,0)
                        true
                    }
                    R.id.action_delete_from_path->{
                        tableForDBRepository.updatePath(item.identification,1)
                        true
                    }
                    else -> false
                }
            }
        }
        else if (item.inLiked==1 && item.inPath == 1){
            popupMenu.inflate(R.menu.delete_delete_menu)
            popupMenu.setOnMenuItemClickListener{
                when(it.itemId){
                    R.id.action_set_in_like->{
                        tableForDBRepository.updateLiked(item.identification,0)
                        true
                    }
                    R.id.action_delete_from_path->{
                        tableForDBRepository.updatePath(item.identification,0)
                        true
                    }
                    else -> false
                }
            }
        }
        popupMenu.show()
    }
}