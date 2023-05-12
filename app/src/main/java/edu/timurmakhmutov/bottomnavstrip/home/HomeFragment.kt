package edu.timurmakhmutov.bottomnavstrip.home

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import edu.timurmakhmutov.bottomnavstrip.place_screen.PlaceScreenFragment
import edu.timurmakhmutov.bottomnavstrip.R
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentHomeBinding
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment(), HomePlacesAdapter.Listener {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var lat: String
    private lateinit var lon: String

    private val cityTypelist:List<String> = listOf("","msk","spb","nsk",
        "ekb","nnv","kzn","vbg","smr","krd","sochi","ufa","krasnoyarsk")
    private val chillTypelist:List<String> = listOf("", "cinema","comedy-club,concert-hall","amusement,cinema,bar,brewery,comedy-club,culture","prirodnyj-zapovednik,park,stable","museums","photo-places"
        ,"bridge,church,fountain,palace,homesteads","attractions,culture",
    "questroom","park,recreation,suburb,dance-studio",
        "art-centers,art-space,museums,workshops,theatre","other")

    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var  homePlacesAdapter: HomePlacesAdapter
    private lateinit var binding: FragmentHomeBinding
    private val model: HomeViewModel by activityViewModels()

    private var city:String =""
    private var categories: String=""



    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            lat = it.latitude.toString()
            lon = it.longitude.toString()
        }

        binding = FragmentHomeBinding.inflate(inflater, container, false)

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
        initTopRecycler()

        binding.museums.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("1", "art-centers,art-space,museums,workshops,theatre")
            bundle.putString("2","Искусство")
            bundle.putString("3",lat)
            bundle.putString("4", lon)
            findNavController().navigate(R.id.action_homeFragment_to_freeTripScreenFragment, bundle)
        }
        binding.entertainments.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("1", "amusement,cinema,bar,brewery,comedy-club,culture")
            bundle.putString("2","Развлектальный")
            bundle.putString("3",lat)
            bundle.putString("4", lon)
            findNavController().navigate(R.id.action_homeFragment_to_freeTripScreenFragment, bundle)
        }
        binding.attractions.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("1", "attractions,culture")
            bundle.putString("2","Достопримечательности")
            bundle.putString("3",lat)
            bundle.putString("4", lon)
            findNavController().navigate(R.id.action_homeFragment_to_freeTripScreenFragment, bundle)
        }

        binding?.citySpinnerMain?.onItemSelectedListener =object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                city = cityTypelist[p2]
                homeSpinnerChoice(city,categories)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        binding!!.typeChillSpinnerMain.onItemSelectedListener=object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                categories = chillTypelist[p2]
                homeSpinnerChoice(city,categories)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        updateData()
    }

    private fun updateData(){
        model.liveDataHome.observe(viewLifecycleOwner){ places ->
            homePlacesAdapter.submitList(places)
        }
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
        binding?.recyclerForTopPlacesHome?.layoutManager = LinearLayoutManager(context)
        homePlacesAdapter = HomePlacesAdapter(this)
        binding?.recyclerForTopPlacesHome?.adapter = homePlacesAdapter
    }

    private fun homeSpinnerChoice(location: String, categories: String) {
        val url = "https://kudago.com/public-api/v1.4/places/?lang=" +
                "&fields=&page_size=100&expand=&order_by=&text_format=text&ids=&location=" + location +
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
            { result -> val places = parseNamesData(result)
                model.liveDataHome.value = places},
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

    override fun onClick(item: HomePlaceNames) {
        val bundle = Bundle()
        bundle.putString("1",item.placeId)
        findNavController(binding.root).navigate(R.id.action_homeFragment_to_placeScreenFragment, bundle)
    }
}