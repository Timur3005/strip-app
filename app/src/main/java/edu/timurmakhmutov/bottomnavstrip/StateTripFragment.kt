package edu.timurmakhmutov.bottomnavstrip

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation.findNavController
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDBRepository
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentStateTripBinding
import edu.timurmakhmutov.bottomnavstrip.lk.LKViewModel
import edu.timurmakhmutov.bottomnavstrip.lk.LikedAdapter

class StateTripFragment : Fragment(){

    private lateinit var fragmentStateTripBinding: FragmentStateTripBinding

    private lateinit var pathAdapter: LikedAdapter
    private val model: LKViewModel by activityViewModels()


    private val tableForDBRepository = TableForDBRepository(Application())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Inflate the layout for this fragment
        fragmentStateTripBinding = FragmentStateTripBinding.inflate(inflater, container, false)
        fragmentStateTripBinding.stateTripEnd.setOnClickListener {
            findNavController(
                fragmentStateTripBinding.root
            ).navigate(R.id.action_stateTripFragment_to_homeFragment)
        }


        return fragmentStateTripBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}