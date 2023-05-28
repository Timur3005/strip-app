package edu.timurmakhmutov.bottomnavstrip.ui

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDB
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDBRepository
import edu.timurmakhmutov.bottomnavstrip.R
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentBottomSheetBinding
import edu.timurmakhmutov.bottomnavstrip.view_model.LKViewModel
import edu.timurmakhmutov.bottomnavstrip.adapters.LikedAdapter

// BottomSheetDialogFragment for show and edit path in StateTripFragment
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