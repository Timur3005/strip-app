package edu.timurmakhmutov.bottomnavstrip.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import edu.timurmakhmutov.bottomnavstrip.R
import edu.timurmakhmutov.bottomnavstrip.adapters.FindAdapter
import edu.timurmakhmutov.bottomnavstrip.data_classes.HomePlaceNames
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentFindingBinding
import edu.timurmakhmutov.bottomnavstrip.view_model.HomeViewModel
import org.json.JSONObject

class FindingFragment : Fragment(), FindAdapter.FindListener {

    private lateinit var adapter: FindAdapter
    private val model: HomeViewModel by activityViewModels()
    private lateinit var binding: FragmentFindingBinding


    // implementation of api search and displaying the results on the screen
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFindingBinding.inflate(inflater, container, false)
        if (binding.searchPlace.text!=null) {
            model.searchPlace.value = binding.searchPlace.text.toString()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.search.setOnClickListener {
                mainFindParse(binding.searchPlace.text.toString())
        }
        initRec()
        updateData()
    }

    private fun updateData(){
        model.liveDataFind.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun initRec(){
        binding.findRecycler.layoutManager = LinearLayoutManager(context)
        adapter = FindAdapter(this)
        binding.findRecycler.adapter = adapter
    }

    private fun mainFindParse(response: String) {
        val url = "https://kudago.com/public-api/v1.4/search/?q=$response&lang=&expand=&location=&ctype=place&is_free=&lat=&lon=&radius="
        val queue = Volley.newRequestQueue(requireContext())
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result ->
                val places = minorParsing(result)
                if (places!=null) {
                    model.liveDataFind.value = places
                }
            },
            { error -> error.printStackTrace() }
        )
        queue.add(request)
    }
    private fun minorParsing(result: String): ArrayList<HomePlaceNames>? {
        val titles = ArrayList<HomePlaceNames>()
        try {
            val namesMainObject = JSONObject(result)
            val names = namesMainObject.getJSONArray("results")
            if (names.length()>1) {
                for (i in 0 until names.length()) {
                    val name = names[i] as JSONObject
                    titles.add(HomePlaceNames(name.getString("title"), name.getString("id")))
                }
                return titles
            }
            return null
        }
        catch (e: Exception){
            Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
            minorParsing(result)
        }
        return null
    }

    override fun onClick(item: HomePlaceNames) {
        val bundle = Bundle()
        bundle.putString("1", item.placeId)
        findNavController().navigate(R.id.action_paymentTripFragment_to_placeScreenFragment, bundle)
    }
}