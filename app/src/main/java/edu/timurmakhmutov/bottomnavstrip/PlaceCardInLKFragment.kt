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
            imageInLkPlaceScreen.adapter = ImagePagerAdapter(ArrayList(item.imageURLs.split(", ")))
            if (item.inPath == 1 && item.inLiked == 1){
                addToPath.text = "Удалить из маршрута"
                addToPath.setOnClickListener {
                    tableForDBRepository.updatePath(realId, 0)
                }
                addToLikePlaceScreen.text = "Удалить из избранного"
                addToLikePlaceScreen.setOnClickListener {
                    tableForDBRepository.updateLiked(realId, 0)
                }

            }
            else if (item.inPath == 1 && item.inLiked == 0){
                addToPath.text = "Удалить из маршрута"
                addToPath.setOnClickListener {
                    tableForDBRepository.updatePath(realId, 0)
                }
                addToLikePlaceScreen.text = "Добавить в избранное"
                addToLikePlaceScreen.setOnClickListener {
                    tableForDBRepository.updateLiked(realId, 1)
                }
            }
            else if (item.inPath == 0 && item.inLiked == 1){
                addToPath.text = "Добавить в маршрут"
                addToPath.setOnClickListener {
                    tableForDBRepository.updatePath(realId, 1)
                }
                addToLikePlaceScreen.text = "Удалить из избранного"
                addToLikePlaceScreen.setOnClickListener {
                    tableForDBRepository.updateLiked(realId, 0)
                }
            }
            else{
                addToPath.text = "Добавить в маршрут"
                addToPath.setOnClickListener {
                    tableForDBRepository.updatePath(realId, 1)
                }
                addToLikePlaceScreen.text = "Добавить в избранное"
                addToLikePlaceScreen.setOnClickListener {
                    tableForDBRepository.updateLiked(realId, 1)
                }
            }
        }
        })
    }

}