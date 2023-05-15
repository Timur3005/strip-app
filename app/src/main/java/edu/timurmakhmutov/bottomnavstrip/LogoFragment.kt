package edu.timurmakhmutov.bottomnavstrip

import android.Manifest
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDBRepository
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentLogoBinding
import edu.timurmakhmutov.bottomnavstrip.home.HomePlaceNames
import edu.timurmakhmutov.bottomnavstrip.home.HomeViewModel
import edu.timurmakhmutov.bottomnavstrip.home.ToursNames
import edu.timurmakhmutov.bottomnavstrip.home.isPermissionGranted
import org.json.JSONObject

class LogoFragment : Fragment() {

    private val model: HomeViewModel by activityViewModels()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentLogoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLogoBinding.inflate(inflater,container, false)
        mAuth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeSpinnerChoice()
        buildTourRequest()
        model.toursLoaded.observe(viewLifecycleOwner){tours->
            model.placesLoaded.observe(viewLifecycleOwner){places->
                if (tours && places && isPermissionGranted((Manifest.permission.ACCESS_FINE_LOCATION))){
                    model.uId.value = mAuth.currentUser?.uid.toString()
                    findNavController().navigate(R.id.action_logoFragment_to_homeFragment)
                }
                else if (tours && places){
                    findNavController().navigate(R.id.action_logoFragment_to_enterFragment)
                }
            }

        }
    }
    private fun buildTourRequest(){
        val url = "https://kudago.com/public-api/v1.4/lists/?lang=" +
                "&fields=&expand=&order_by=&text_format=&ids=" +
                "&location="
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result -> model.liveDataToursNames.value = parseTours(result)
                model.toursLoaded.value = true
            },
            { error -> error.printStackTrace() }
        )
        queue.add(request)
    }

    private fun parseTours(result: String):ArrayList<ToursNames>{
        val list = kotlin.collections.ArrayList<ToursNames>()
        val namesMainObject = JSONObject(result)
        val names = namesMainObject.getJSONArray("results")
        for (i in 0 until names.length()) {
            val name = names[i] as JSONObject
            list.add(ToursNames(name.getString("title"),name.getString("id")))
        }
        return list
    }
    private fun homeSpinnerChoice() {
        val url = "https://kudago.com/public-api/v1.4/places/?lang=" +
                "&fields=&page_size=100&expand=&order_by=&text_format=text&ids=&location="+
                "&has_showings=&showing_since=1444385206&showing_until=1444385206" +
                "&is_free=" +
                "&categories=" +
                "&lon=" +
                "&lat=" +
                "&radius="
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result -> val places = parseNamesData(result)
                model.liveDataHome.value = places
                model.placesLoaded.value = true
            },
            { error -> error.printStackTrace() }
        )
        queue.add(request)
    }

    private fun parseNamesData(result: String): ArrayList<HomePlaceNames> {
        val titles = ArrayList<HomePlaceNames>()
        val namesMainObject = JSONObject(result)
        val names = namesMainObject.getJSONArray("results")
        for (i in 0 until names.length()) {
            val name = names[i] as JSONObject
            titles.add(HomePlaceNames(name.getString("title"),name.getString("id")))
        }
        return titles
    }
}