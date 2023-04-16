package edu.timurmakhmutov.bottomnavstrip.home

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import edu.timurmakhmutov.bottomnavstrip.R
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentHomeBinding
import edu.timurmakhmutov.bottomnavstrip.isPermissionGranted
import org.json.JSONException
import org.json.JSONObject

class HomeFragment : Fragment() {

    private lateinit var cityTypelist:List<String>

    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var  homePlacesAdapter: HomePlacesAdapter
    private var binding: FragmentHomeBinding? = null
    private val model: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        //spinners listeners
        binding!!.citySpinnerMain.onItemSelectedListener=object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        binding!!.typeChillSpinnerMain.onItemSelectedListener=object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        homeSpinnerChoice("msk","")
        updateData()
        initTopRecycler()
    }

    private fun updateData(){
        model.liveDataHome.observe(viewLifecycleOwner){
            homePlacesAdapter.submitList(it)
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
        homePlacesAdapter = HomePlacesAdapter()
        binding?.recyclerForTopPlacesHome?.adapter = homePlacesAdapter
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
            { result -> homePlacesAdapter.submitList(parseNamesData(result))},
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
            titles.add(HomePlaceNames(name.getString("title")))
        }
        return titles
    }


}