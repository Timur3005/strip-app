package edu.timurmakhmutov.bottomnavstrip

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentPlaceScreenBinding
import edu.timurmakhmutov.bottomnavstrip.home.HomePlacesAdapter
import edu.timurmakhmutov.bottomnavstrip.home.HomeToPlaceLive
import edu.timurmakhmutov.bottomnavstrip.home.HomeViewModel

class PlaceScreenFragment : Fragment() {

    var fragmentPlaceScreenBinding: FragmentPlaceScreenBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentPlaceScreenBinding = FragmentPlaceScreenBinding.inflate(inflater, container, false)
        return fragmentPlaceScreenBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun parsePlace(identification: String){

        val url = "https://kudago.com/public-api/v1.4/places/$identification/?lang=&fields=&expand="
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result -> Log.d("MyTaggg","result: $result")},
            { error -> error.printStackTrace() }
        )
        queue.add(request)
    }
}