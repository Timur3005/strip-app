package edu.timurmakhmutov.bottomnavstrip.ui

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDBRepository
import edu.timurmakhmutov.bottomnavstrip.R
import edu.timurmakhmutov.bottomnavstrip.adapters.ImagePagerAdapter
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentPlaceCardInLKBinding

class PlaceCardInLKFragment : Fragment() {

    private lateinit var binding: FragmentPlaceCardInLKBinding
    private val tableForDBRepository = TableForDBRepository(Application())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPlaceCardInLKBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val realId = arguments?.getString("1")
        if (realId!=null){
            setUI(realId)
        }
    }

    //setting values in the place card
    private fun setUI(realId: String){
        tableForDBRepository.getById(realId).observe(viewLifecycleOwner) { item ->
            with(binding) {
                address.text = item.address
                title.text = item.title
                location.text = item.location
                bodyText.text = item.description
                imageInLkPlaceScreen.adapter =
                    ImagePagerAdapter(ArrayList(item.imageURLs.split(", ")))
                if (item.inPath == 1 && item.inLiked == 1) {
                    addToPath.setBackgroundResource(R.drawable.ic_baseline_location_off_24)
                    addToPath.setOnClickListener {
                        tableForDBRepository.updatePath(realId, 0)
                    }
                    addToLikePlaceScreen.setBackgroundResource(R.drawable.ic_baseline_star_24)
                    addToLikePlaceScreen.setOnClickListener {
                        tableForDBRepository.updateLiked(realId, 0)
                    }

                } else if (item.inPath == 1 && item.inLiked == 0) {
                    addToPath.setBackgroundResource(R.drawable.ic_baseline_location_off_24)
                    addToPath.setOnClickListener {
                        tableForDBRepository.updatePath(realId, 0)
                    }
                    addToLikePlaceScreen.setBackgroundResource(R.drawable.ic_baseline_star_border_24)
                    addToLikePlaceScreen.setOnClickListener {
                        tableForDBRepository.updateLiked(realId, 1)
                    }
                } else if (item.inPath == 0 && item.inLiked == 1) {
                    addToPath.setBackgroundResource(R.drawable.ic_baseline_add_location_alt_24)
                    addToPath.setOnClickListener {
                        tableForDBRepository.updatePath(realId, 1)
                    }
                    addToLikePlaceScreen.setBackgroundResource(R.drawable.ic_baseline_star_24)
                    addToLikePlaceScreen.setOnClickListener {
                        tableForDBRepository.updateLiked(realId, 0)
                    }
                } else {
                    addToPath.setBackgroundResource(R.drawable.ic_baseline_add_location_alt_24)
                    addToPath.setOnClickListener {
                        tableForDBRepository.updatePath(realId, 1)
                    }
                    addToLikePlaceScreen.setBackgroundResource(R.drawable.ic_baseline_star_border_24)
                    addToLikePlaceScreen.setOnClickListener {
                        tableForDBRepository.updateLiked(realId, 1)
                    }
                }
            }
        }
    }

}