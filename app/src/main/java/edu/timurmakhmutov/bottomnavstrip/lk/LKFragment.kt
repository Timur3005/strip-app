package edu.timurmakhmutov.bottomnavstrip.lk

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDB
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDBRepository
import edu.timurmakhmutov.bottomnavstrip.R
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentLKBinding

class LKFragment : Fragment(),LikedAdapter.DBListener {
    private lateinit var lkBinding: FragmentLKBinding
    private lateinit var likedAdapter: LikedAdapter
    private lateinit var pathAdapter: LikedAdapter
    private val model: LKViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lkBinding = FragmentLKBinding.inflate(inflater, container, false)
        lkBinding.startInLk.setOnClickListener { findNavController(lkBinding.root).navigate(R.id.action_LKFragment_to_stateTripFragment) }
        return lkBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tableForDBRepository = TableForDBRepository(Application())
        tableForDBRepository.allLiked.observe(viewLifecycleOwner, Observer { tableForDBS ->
            model.liveDataLiked.value = tableForDBS
            initRecyclerLiked()
            updateLiked()
        })
        tableForDBRepository.allPaths.observe(viewLifecycleOwner, Observer {
            model.liveDataPath.value = it
            initRecyclerPath()
            updatePath()
        })
        tableForDBRepository.allTables.observe(viewLifecycleOwner, Observer {
            for (item in it){
                if (item.inLiked==0 && item.inPath==0){
                    tableForDBRepository.delete(item)
                }
            }
        })


    }
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

    override fun dbOnClick(item: TableForDB) {
        val bundle = Bundle()
        bundle.putString("1",item.identification)
        findNavController(lkBinding.root).navigate(R.id.action_LKFragment_to_placeCardInLKFragment, bundle)
    }
}