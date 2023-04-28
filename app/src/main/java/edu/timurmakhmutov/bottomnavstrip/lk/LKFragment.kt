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
            initRecycler()
            update()
        })


    }
    private fun initRecycler(){
        likedAdapter = LikedAdapter(this)
        lkBinding.recyclerForLikedInLk.layoutManager = LinearLayoutManager(context)
        lkBinding.recyclerForLikedInLk.adapter = likedAdapter
    }
    private fun update(){
        model.liveDataLiked.observe(viewLifecycleOwner){
            likedAdapter.submitList(it)
        }
    }

    override fun dbOnClick(item: TableForDB) {
        val bundle = Bundle()
        bundle.putString("1",item.identification)
        findNavController(lkBinding.root).navigate(R.id.action_LKFragment_to_placeCardInLKFragment, bundle)
    }
}