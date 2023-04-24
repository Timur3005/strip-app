package edu.timurmakhmutov.bottomnavstrip.place_screen

import android.annotation.SuppressLint
import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
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
import edu.timurmakhmutov.bottomnavstrip.R
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentPlaceScreenBinding
import edu.timurmakhmutov.bottomnavstrip.home.HomeViewModel
import org.json.JSONObject

class PlaceScreenFragment : Fragment(){

    private lateinit var commentsAdapter: CommentsAdapter
    private val model: PlaceViewModel by activityViewModels()
    private lateinit var ImagesURL:ArrayList<String>
    private val tableForDBRepository = TableForDBRepository(Application())
    private var fragmentPlaceScreenBinding: FragmentPlaceScreenBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentPlaceScreenBinding = FragmentPlaceScreenBinding.inflate(inflater, container, false)
        return fragmentPlaceScreenBinding!!.root
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
        tableForDBRepository.getById(trueId).observe(viewLifecycleOwner, Observer {
            if (it == null){
                fragmentPlaceScreenBinding?.addToLikePlaceScreen?.setOnClickListener {
                    tableForDBRepository.insert(TableForDB(trueId.toString(),
                        fragmentPlaceScreenBinding?.title?.text.toString(),
                        fragmentPlaceScreenBinding?.address?.text.toString(),
                        fragmentPlaceScreenBinding?.bodyText?.text.toString(),
                        fragmentPlaceScreenBinding?.location?.text.toString(), ImagesURL.toString().replace("[","").replace("]", "")))
                    fragmentPlaceScreenBinding?.addToLikePlaceScreen?.text = "Удалить из избранного"
                    fragmentPlaceScreenBinding?.addToLikePlaceScreen?.setBackgroundColor(R.color.red)
                }
            }
            else {
                fragmentPlaceScreenBinding?.addToLikePlaceScreen?.text = "Удалить из избранного"
                fragmentPlaceScreenBinding?.addToLikePlaceScreen?.setBackgroundColor(R.color.red)
                fragmentPlaceScreenBinding?.addToLikePlaceScreen?.setOnClickListener {
                    tableForDBRepository.delete(
                        TableForDB(
                            trueId.toString(),
                            fragmentPlaceScreenBinding?.title?.text.toString(),
                            fragmentPlaceScreenBinding?.address?.text.toString(),
                            fragmentPlaceScreenBinding?.bodyText?.text.toString(),
                            fragmentPlaceScreenBinding?.address?.text.toString(),
                            ImagesURL.toString().replace("[", "").replace("]", "")
                        )
                    )
                    fragmentPlaceScreenBinding?.addToLikePlaceScreen?.text = "Добавить в избранное"
                    fragmentPlaceScreenBinding?.addToLikePlaceScreen?.setBackgroundColor(R.color.button_back)

                }
            }
        })
    }
    private fun updateData(){
        model.liveDataCommentsFields.observe(viewLifecycleOwner){
            commentsAdapter.submitList(it)
        }
    }

    private fun initComments(){
        fragmentPlaceScreenBinding?.recyclerForComments?.layoutManager = LinearLayoutManager(context)
        commentsAdapter = CommentsAdapter()
        fragmentPlaceScreenBinding?.recyclerForComments?.adapter = commentsAdapter
    }

    private fun parsePlace(identification: String){

        val url = "https://kudago.com/public-api/v1.4/places/$identification/?lang=&fields=&expand="
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result -> ImagesURL = setMyData(result)
                fragmentPlaceScreenBinding?.image?.adapter = ImagePagerAdapter(ImagesURL)
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
            this?.bodyText?.text = mainObj.getString("description").replace("<p>","").replace("</p>","")
            this?.title?.text = mainObj.getString("title")
            this?.address?.text = mainObj.getString("address")
            this?.location?.text = locationConvert(mainObj.getString("location"))
            for (i in 0 until images.length()){
                val image = images[i] as JSONObject
                list.add(image.getString("image"))
            }
        }
        return list
    }
    private fun parseComments(identification: String){

        val url = "https://kudago.com/public-api/v1.4/places/$identification/comments/?lang=&fields=&order_by=&ids="
        val newQueue = Volley.newRequestQueue(context)
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