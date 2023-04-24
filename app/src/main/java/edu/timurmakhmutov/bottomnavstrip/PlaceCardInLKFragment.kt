package edu.timurmakhmutov.bottomnavstrip

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDBRepository
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentPlaceCardInLKBinding
import edu.timurmakhmutov.bottomnavstrip.place_screen.ImagePagerAdapter

class PlaceCardInLKFragment : Fragment() {
    private lateinit var binding: FragmentPlaceCardInLKBinding
    private val tableForDBRepository = TableForDBRepository(Application())
    private var clickCounter = 0;
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
    private fun setUI(realId: String){
        tableForDBRepository.getById(realId).observe(viewLifecycleOwner, Observer { item ->with(binding){
            address.text = item.address
            title.text = item.title
            location.text = item.location
            bodyText.text = item.description
            imageInLkPlaceScreen.adapter = ImagePagerAdapter(item.imageURLs.split(", ") as ArrayList<String>)
            addToLikePlaceScreen.setOnClickListener{
                    findNavController().navigate(R.id.action_placeCardInLKFragment_to_LKFragment)
                    tableForDBRepository.delete(item)
            }
        }
        })
    }

}