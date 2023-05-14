package edu.timurmakhmutov.bottomnavstrip

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.whenResumed
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDB
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDBRepository
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentFreeTripScreenBinding
import edu.timurmakhmutov.bottomnavstrip.home.HomePlaceNames
import edu.timurmakhmutov.bottomnavstrip.home.HomePlacesAdapter
import edu.timurmakhmutov.bottomnavstrip.home.HomeViewModel
import edu.timurmakhmutov.bottomnavstrip.lk.LKViewModel
import edu.timurmakhmutov.bottomnavstrip.lk.LikedAdapter
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
        if (description!=null) {
            fragmentFreeTripScreenBinding.description.text =
                description.replace("<p>", "").replace("</p>", "")
        } else {
            fragmentFreeTripScreenBinding.startInFreeTripScreen.isVisible = false
            fragmentFreeTripScreenBinding.description.text = "Ошибка сервиса"
        }
    }
}