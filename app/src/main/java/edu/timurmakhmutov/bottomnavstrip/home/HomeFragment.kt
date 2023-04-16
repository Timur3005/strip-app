package edu.timurmakhmutov.bottomnavstrip.home

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import edu.timurmakhmutov.bottomnavstrip.R
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentHomeBinding
import edu.timurmakhmutov.bottomnavstrip.isPermissionGranted
import org.json.JSONException
import org.json.JSONObject

class HomeFragment : Fragment() {
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var  homePlacesAdapter: HomePlacesAdapter
    private var binding: FragmentHomeBinding? = null
    private lateinit var  model: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        binding!!.homeStartButton.setOnClickListener { findNavController(binding!!.root).navigate(R.id.action_homeFragment_to_stateTripFragment) }


        //Спиннер городов
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.cities_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.citySpinnerMain.adapter = adapter

        //Спиннер категорий
        val adapterCat = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.typeChillSpinnerMain.adapter = adapterCat
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        //initTopRecycler()
    }

    //проверка разрешений
    private fun permissionListener(){
        pLauncher = registerForActivityResult(
            ActivityResultContracts.
            RequestPermission()){}
    }
    private fun checkPermission(){
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
            permissionListener()
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    //инициализация ресайклера
    private fun initTopRecycler(){

    }

    private fun homeSpinnerChoice(location: String, categories: String) {
        val url = "https://kudago.com/public-api/v1.4/places/?lang=" +
                "&fields=&expand=&order_by=&text_format=&ids=&location=" + location +
                "&has_showings=&showing_since=1444385206&showing_until=1444385206" +
                "&is_free=" +
                "&categories=" + categories +
                "&lon=" +
                "&lat=" +
                "&radius="
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            { response -> parseNamesData(response) },
            { error -> error.printStackTrace() }
        )
        queue.add(request)
    }

    private fun parseNamesData(result: String): ArrayList<HomePlaceNames> {
        val titles = ArrayList<HomePlaceNames>()
        val namesMainObject = JSONObject(result)
        val names = namesMainObject.getJSONArray("results")
        for (i in 0 until names.length()) {
            val name = names.getJSONObject(i)
            titles.add(HomePlaceNames(name.getString("title")))
        }
        return titles
    }


}