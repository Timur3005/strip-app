package edu.timurmakhmutov.bottomnavstrip.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentFreeTripScreenBinding
import org.json.JSONObject

class FreeTripScreenFragment : BottomSheetDialogFragment() {

    private lateinit var fragmentFreeTripScreenBinding: FragmentFreeTripScreenBinding
    private var id: String? = null
    private var tourLink: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentFreeTripScreenBinding =
            FragmentFreeTripScreenBinding.inflate(inflater, container, false)

        id = arguments?.getString("1")
        return fragmentFreeTripScreenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id?.let { workWithApi(it) }
    }

    private fun workWithApi(id: String){
        val url ="https://kudago.com/public-api/v1.4/lists/" + id +
                "/?lang=&fields=&expand="
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result ->
                parseTour(result)
            },
            { error -> error.printStackTrace() }
        )
        queue.add(request)
    }

    private fun parseTour(result: String){
        val namesMainObject = JSONObject(result)
        val description: String? = namesMainObject.getString("description")
        val url: String? = namesMainObject.getString("site_url")
        if (description!=null && url!=null) {
            fragmentFreeTripScreenBinding.description.text =
                description.replace("<p>", "").replace("</p>", "")
            fragmentFreeTripScreenBinding.startInFreeTripScreen.setOnClickListener{
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
        else {
            fragmentFreeTripScreenBinding.startInFreeTripScreen.isVisible = false
            fragmentFreeTripScreenBinding.description.text = "Ошибка сервиса"
        }
    }
}