package edu.timurmakhmutov.bottomnavstrip.ui

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDB
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDBRepository
import edu.timurmakhmutov.bottomnavstrip.R
import edu.timurmakhmutov.bottomnavstrip.adapters.CommentsAdapter
import edu.timurmakhmutov.bottomnavstrip.adapters.ImagePagerAdapter
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentPlaceScreenBinding
import edu.timurmakhmutov.bottomnavstrip.data_classes.PlaceCommentsFields
import edu.timurmakhmutov.bottomnavstrip.view_model.PlaceViewModel
import kotlinx.android.synthetic.main.fragment_selections.*
import org.json.JSONObject

class PlaceScreenFragment : Fragment(){

    private lateinit var lat:String
    private lateinit var lon:String
    private lateinit var commentsAdapter: CommentsAdapter
    private val model: PlaceViewModel by activityViewModels()
    private lateinit var ImagesURL:ArrayList<String>
    private val tableForDBRepository = TableForDBRepository(Application())
    private lateinit var fragmentPlaceScreenBinding: FragmentPlaceScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentPlaceScreenBinding = FragmentPlaceScreenBinding.inflate(inflater, container, false)
        return fragmentPlaceScreenBinding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val trueId: String? = arguments?.getString("1")
        if (trueId != null) {
            parsePlace(trueId)
            parseComments(trueId)
        }
        initComments()
        updateData()

        //user's work with elected
        tableForDBRepository.getById(trueId).observe(viewLifecycleOwner, Observer {item->
            if (item == null) {
                var liked = 0
                var path = 0
                fragmentPlaceScreenBinding.addToLikePlaceScreen.setOnClickListener {
                    liked = 1
                    tableForDBRepository.insert(
                            TableForDB(
                                trueId.toString(),
                                fragmentPlaceScreenBinding.title.text.toString(),
                                fragmentPlaceScreenBinding.address.text.toString(),
                                fragmentPlaceScreenBinding.bodyText.text.toString(),
                                fragmentPlaceScreenBinding.location.text.toString(),
                                ImagesURL.toString().replace("[", "").replace("]", ""),
                                lat,
                                lon,
                                liked,
                                path
                            )
                    )
                    fragmentPlaceScreenBinding.addToLikePlaceScreen.setBackgroundResource(R.drawable.ic_baseline_star_24)
                }
                fragmentPlaceScreenBinding.addToPath.setOnClickListener {
                    path = 1
                    tableForDBRepository.insert(
                        TableForDB(
                            trueId.toString(),
                            fragmentPlaceScreenBinding.title.text.toString(),
                            fragmentPlaceScreenBinding.address.text.toString(),
                            fragmentPlaceScreenBinding.bodyText.text.toString(),
                            fragmentPlaceScreenBinding.location.text.toString(),
                            ImagesURL.toString().replace("[", "").replace("]", ""),
                            lat,
                            lon,
                            liked,
                            path
                        )
                    )
                    fragmentPlaceScreenBinding.addToPath.setBackgroundResource(R.drawable.ic_baseline_location_off_24)
                }
            }
            else{
                if (item.inLiked == 0 && item.inPath == 1){
                    fragmentPlaceScreenBinding.addToLikePlaceScreen.setBackgroundResource(R.drawable.ic_baseline_star_border_24)
                    fragmentPlaceScreenBinding.addToPath.setBackgroundResource(R.drawable.ic_baseline_location_off_24)
                    fragmentPlaceScreenBinding.addToLikePlaceScreen.setOnClickListener {
                        tableForDBRepository.updateLiked(trueId, 1)
                        fragmentPlaceScreenBinding.addToLikePlaceScreen.setBackgroundResource(R.drawable.ic_baseline_star_24)
                    }
                    fragmentPlaceScreenBinding.addToPath.setOnClickListener {
                        tableForDBRepository.updatePath(trueId,0)
                        fragmentPlaceScreenBinding.addToPath.setBackgroundResource(R.drawable.ic_baseline_add_location_alt_24)
                    }
                }
                else if(item.inPath == 0 && item.inLiked == 1){
                    fragmentPlaceScreenBinding.addToLikePlaceScreen.setBackgroundResource(R.drawable.ic_baseline_star_24)
                    fragmentPlaceScreenBinding.addToPath.setBackgroundResource(R.drawable.ic_baseline_add_location_alt_24)
                    fragmentPlaceScreenBinding.addToPath.setOnClickListener {
                        tableForDBRepository.updatePath(trueId, 1)
                        fragmentPlaceScreenBinding.addToPath.setBackgroundResource(R.drawable.ic_baseline_location_off_24)
                    }
                    fragmentPlaceScreenBinding.addToLikePlaceScreen.setOnClickListener {
                        tableForDBRepository.updateLiked(trueId, 0)
                        fragmentPlaceScreenBinding.addToLikePlaceScreen.setBackgroundResource(R.drawable.ic_baseline_star_border_24)
                    }
                }
                else if (item.inPath == 1 && item.inLiked == 1){
                    fragmentPlaceScreenBinding.addToLikePlaceScreen.setBackgroundResource(R.drawable.ic_baseline_star_24)
                    fragmentPlaceScreenBinding.addToPath.setBackgroundResource(R.drawable.ic_baseline_location_off_24)
                    fragmentPlaceScreenBinding.addToPath.setOnClickListener {
                        tableForDBRepository.updatePath(trueId, 0)
                        fragmentPlaceScreenBinding.addToPath.setBackgroundResource(R.drawable.ic_baseline_add_location_alt_24)
                    }
                    fragmentPlaceScreenBinding.addToLikePlaceScreen.setOnClickListener {
                        tableForDBRepository.updateLiked(trueId, 0)
                        fragmentPlaceScreenBinding.addToLikePlaceScreen.setBackgroundResource(R.drawable.ic_baseline_star_border_24)
                    }
                }
                else{
                    fragmentPlaceScreenBinding.addToLikePlaceScreen.setBackgroundResource(R.drawable.ic_baseline_star_border_24)
                    fragmentPlaceScreenBinding.addToPath.setBackgroundResource(R.drawable.ic_baseline_add_location_alt_24)
                    fragmentPlaceScreenBinding.addToPath.setOnClickListener {
                        tableForDBRepository.updatePath(trueId, 1)
                        fragmentPlaceScreenBinding.addToPath.setBackgroundResource(R.drawable.ic_baseline_location_off_24)
                    }
                    fragmentPlaceScreenBinding.addToLikePlaceScreen.setOnClickListener {
                        tableForDBRepository.updateLiked(trueId, 1)
                        fragmentPlaceScreenBinding.addToLikePlaceScreen.setBackgroundResource(R.drawable.ic_baseline_star_24)
                    }
                }
            }
        })
    }

    //recycler filling
    private fun updateData(){
        model.liveDataCommentsFields.observe(viewLifecycleOwner){
            commentsAdapter.submitList(it)
        }
    }
    private fun initComments(){
        fragmentPlaceScreenBinding.recyclerForComments.layoutManager = LinearLayoutManager(context)
        commentsAdapter = CommentsAdapter()
        fragmentPlaceScreenBinding.recyclerForComments.adapter = commentsAdapter
    }

    //methods for loading data from api
    private fun parsePlace(identification: String){

        val url = "https://kudago.com/public-api/v1.4/places/$identification/?lang=&fields=&expand="
        val queue = Volley.newRequestQueue(requireContext())
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result -> ImagesURL = setMyData(result)
                fragmentPlaceScreenBinding.image.adapter = ImagePagerAdapter(ImagesURL)
            },
            { error -> Log.d("MyTaggg", "error $error") }
        )
        queue.add(request)
    }
    private fun setMyData(result: String):ArrayList<String>{
        val list = ArrayList<String>()
        val mainObj = JSONObject(result)
        val images = mainObj.getJSONArray("images")
        with(fragmentPlaceScreenBinding){
            bodyText.text = mainObj.getString("description").replace("<p>","").replace("</p>","")
            title.text = mainObj.getString("title")
            address.text = mainObj.getString("address")
            location.text = locationConvert(mainObj.getString("location"))
            lat = mainObj.getJSONObject("coords").getString("lat")
            lon = mainObj.getJSONObject("coords").getString("lon")
            for (i in 0 until images.length()){
                val image = images[i] as JSONObject
                list.add(image.getString("image"))
            }
        }
        return list
    }
    private fun parseComments(identification: String){

        val url = "https://kudago.com/public-api/v1.4/places/$identification/comments/?lang=&fields=&order_by=&ids="
        val newQueue = Volley.newRequestQueue(requireContext())
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result -> val comms = setComments(result)
                model.liveDataCommentsFields.value = comms
            },
            { error -> Log.d("MyTaggg", "error $error") }
        )
        newQueue.add(request)
    }
    private fun setComments(result: String):ArrayList<PlaceCommentsFields>{
        val list = ArrayList<PlaceCommentsFields>()
        val mainObj = JSONObject(result)
        val commObjs = mainObj.getJSONArray("results")
        for (i in 0 until commObjs.length()){
            val comm = commObjs[i] as JSONObject
            if (comm.getString("text").isNotEmpty()){
                list.add(PlaceCommentsFields(comm.getJSONObject("user")
                .getString("name"), ""+comm.getString("text")+""))
            }
        }
        return list
    }
    private fun locationConvert(str:String): String{
        when(str){
            "spb" -> return "Санкт-Петербург"
            "msk" -> return "Москва"
            "nsk" -> return "Новосибирск"
            "ekb" -> return "Екатеринбург"
            "nnv" -> return "Нижний Новгород"
            "kzn" -> return "Казань"
            "vbg" -> return "Выборг"
            "smr" -> return "Самара"
            "krd" -> return "Краснодар"
            "sochi" -> return "Сочи"
            "ufa" -> return "Уфа"
            "krasnoyarsk" -> return "Красноярск"
        }
        return ""
    }
}