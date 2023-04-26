package edu.timurmakhmutov.bottomnavstrip

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentStateTripBinding

class StateTripFragment : Fragment() {
    private lateinit var fragmentStateTripBinding: FragmentStateTripBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapKitFactory.initialize(context)
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
        fragmentStateTripBinding.mapview.map.move(
            CameraPosition(Point(55.751999, 37.617734), 14.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0f),
            null)


    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
    }
}