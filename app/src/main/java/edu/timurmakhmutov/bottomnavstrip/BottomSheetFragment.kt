package edu.timurmakhmutov.bottomnavstrip

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDB
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDBRepository
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentBottomSheetBinding
import edu.timurmakhmutov.bottomnavstrip.lk.LKViewModel
import edu.timurmakhmutov.bottomnavstrip.lk.LikedAdapter

class BottomSheetFragment : BottomSheetDialogFragment(), LikedAdapter.DBListener {

    private val tableForDBRepository = TableForDBRepository(Application())
    private lateinit var pathAdapter: LikedAdapter
    private val model: LKViewModel by activityViewModels()
    private lateinit var binding: FragmentBottomSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBottomSheetBinding.inflate(inflater,container,false)
        tableForDBRepository.allPaths.observe(viewLifecycleOwner, Observer {
            model.liveDataPath.value = it
            initRecyclerPath()
            updatePath()
        })
        return binding.root
    }
    private fun initRecyclerPath(){
        pathAdapter = LikedAdapter(this)
        binding.bottomSheet.layoutManager = LinearLayoutManager(context)
        binding.bottomSheet.adapter = pathAdapter
    }
    private fun updatePath(){
        model.liveDataPath.observe(viewLifecycleOwner){
            pathAdapter.submitList(it)
        }
    }

    override fun dbOnClick(item: TableForDB) {
        val bundle = Bundle()
        bundle.putString("1",item.identification)
        findNavController().navigate(R.id.action_bottomSheetFragment_to_placeCardInLKFragment,bundle)

    }

}