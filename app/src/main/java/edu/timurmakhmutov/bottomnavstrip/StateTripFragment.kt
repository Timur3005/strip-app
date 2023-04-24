package edu.timurmakhmutov.bottomnavstrip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentStateTripBinding

class StateTripFragment : Fragment() {
    private lateinit var fragmentStateTripBinding: FragmentStateTripBinding
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
}