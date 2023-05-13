package edu.timurmakhmutov.bottomnavstrip

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDB
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDBRepository
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentFreeTripScreenBinding
import edu.timurmakhmutov.bottomnavstrip.home.HomePlaceNames
import edu.timurmakhmutov.bottomnavstrip.home.HomePlacesAdapter
import edu.timurmakhmutov.bottomnavstrip.home.HomeViewModel
import edu.timurmakhmutov.bottomnavstrip.lk.LKViewModel
import edu.timurmakhmutov.bottomnavstrip.lk.LikedAdapter
import org.json.JSONObject

class FreeTripScreenFragment : Fragment(), LikedAdapter.DBListener {

    private lateinit var fragmentFreeTripScreenBinding: FragmentFreeTripScreenBinding
    private lateinit var types: String
    private lateinit var lat: String
    private lateinit var lon: String
    private val tableForDBRepository = TableForDBRepository(Application())
    private lateinit var tourAdapter: LikedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentFreeTripScreenBinding =
            FragmentFreeTripScreenBinding.inflate(inflater, container, false)
        return fragmentFreeTripScreenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun workWithApi(types: String, lat: String, lon: String){
        val url ="https://kudago.com/public-api/v1.4/places/?lang=" +
                "&fields=&page_size=100&expand=&order_by=&text_format=text&ids=&location="+
                "&has_showings=&showing_since=1444385206&showing_until=1444385206" +
                "&is_free=" +
                "&categories=" + types +
                "&lon=" + lat +
                "&lat=" + lon +
                "&radius=20000"
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result ->
                val places =  parseTour(result)
                tourAdapter.submitList(places)
                fragmentFreeTripScreenBinding.startInFreeTripScreen.setOnClickListener{
                    for (i in places){
                        i.inPath = 1
                        tableForDBRepository.getById(i.identification).observe(viewLifecycleOwner, Observer {
                            if (it==null){
                                tableForDBRepository.insert(i)
                            }
                        })
                    }
                }
            },
            { error -> error.printStackTrace() }
        )
        queue.add(request)
    }

    private fun parseTour(result: String):ArrayList<TableForDB>{
        val tour = ArrayList<TableForDB>()
        val namesMainObject = JSONObject(result)
        val names = namesMainObject.getJSONArray("results")
        for (i in 0 until names.length()) {
            val name = names[i] as JSONObject
        }
        return tour
    }
    private fun initRecycler(){
        fragmentFreeTripScreenBinding.recyclerTour.layoutManager = LinearLayoutManager(context)
        tourAdapter = LikedAdapter(this)
        fragmentFreeTripScreenBinding.recyclerTour.adapter = tourAdapter
    }

    override fun dbOnClick(item: TableForDB) {
        TODO("Not yet implemented")
    }
}