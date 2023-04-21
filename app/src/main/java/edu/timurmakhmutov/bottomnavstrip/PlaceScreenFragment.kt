package edu.timurmakhmutov.bottomnavstrip

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentPlaceScreenBinding
import edu.timurmakhmutov.bottomnavstrip.home.ImagePagerAdapter
import org.json.JSONObject

class PlaceScreenFragment : Fragment(){

    private lateinit var ImagesURL:ArrayList<String>
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
        val trueId: String? = arguments?.getString("1")
        if (trueId != null) {
            parsePlace(trueId)
        }
//        val adapter = ImagePagerAdapter(ImagesURL)
//        fragmentPlaceScreenBinding?.image?.adapter = adapter
    }

    private fun parsePlace(identification: String){

        val url = "https://kudago.com/public-api/v1.4/places/$identification/?lang=&fields=&expand="
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result -> ImagesURL = setMyData(result)},
            { error -> Log.d("MyTaggg", "error $error") }
        )
        queue.add(request)
    }
    private fun setMyData(result: String):ArrayList<String>{
        val list = ArrayList<String>()
        val mainObj = JSONObject(result)
        val images = mainObj.getJSONArray("images")
        with(fragmentPlaceScreenBinding){
            this?.bodyText?.text = mainObj.getString("description").
            removePrefix("<p>").removeSuffix("</p>").removePrefix("<p>").removeSuffix("</p>")
            this?.title?.text = mainObj.getString("title")
            for (i in 0 until images.length()){
                val image = images[i] as JSONObject
                list.add(image.getString("image"))
            }
        }
        return list
    }
}