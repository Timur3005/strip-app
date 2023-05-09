package edu.timurmakhmutov.bottomnavstrip

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.PointF
import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.add
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yandex.mapkit.*
import com.yandex.mapkit.annotations.AnnotationLanguage
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.*
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.TextStyle.Placement
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDB
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDBRepository
import edu.timurmakhmutov.bottomnavstrip.databinding.BottomSheetForPathInStateTripFragmentBinding
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentStateTripBinding
import edu.timurmakhmutov.bottomnavstrip.lk.LKViewModel
import edu.timurmakhmutov.bottomnavstrip.lk.LikedAdapter
import kotlinx.android.synthetic.main.fragment_state_trip.*

class StateTripFragment : Fragment(), DrivingSession.DrivingRouteListener, UserLocationObjectListener{
    private lateinit var fragmentStateTripBinding: FragmentStateTripBinding
    private lateinit var bottomSheetBinding: BottomSheetForPathInStateTripFragmentBinding
    private lateinit var start:Point

    private val tableForDBRepository = TableForDBRepository(Application())

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mapObjects:MapObjectCollection
    private lateinit var drivingRouter:DrivingRouter
    private lateinit var drivingSession: DrivingSession

    private lateinit var bottomSheetDialog:BottomSheetDialog
    private lateinit var bottomSheetView: View
    private lateinit var pathAdapter: LikedAdapter
    private val model: LKViewModel by activityViewModels()

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Inflate the layout for this fragment
        fragmentStateTripBinding = FragmentStateTripBinding.inflate(inflater, container, false)
//        bottomSheetDialog = BottomSheetDialog(requireContext())
//        bottomSheetView= layoutInflater.inflate(R.layout.bottom_sheet_for_path_in_state_trip_fragment, null)

        fragmentStateTripBinding.mapview
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
        mapObjects = fragmentStateTripBinding.mapview.map.mapObjects
//        bottomSheetBinding = BottomSheetForPathInStateTripFragmentBinding.bind(bottomSheetView)
//        bottomSheetDialog.setContentView(bottomSheetView)
        fragmentStateTripBinding.activateSheet.setOnClickListener {
            findNavController().navigate(R.id.action_stateTripFragment_to_bottomSheetFragment)
        }

        return fragmentStateTripBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MapKitFactory.initialize(context)

        setLocation()
        racoord()
//        tableForDBRepository.allPaths.observe(viewLifecycleOwner, Observer {
//            model.liveDataPath.value = it
//            initRecyclerPath()
//            updatePath()
//        })


    }
//    private fun initRecyclerPath(){
//        pathAdapter = LikedAdapter(this)
//        bottomSheetBinding.recyclerForBottomSheetPath.layoutManager = LinearLayoutManager(context)
//        bottomSheetBinding.recyclerForBottomSheetPath.adapter = pathAdapter
//    }
//    private fun updatePath(){
//        model.liveDataPath.observe(viewLifecycleOwner){
//            pathAdapter.submitList(it)
//        }
//    }

    override fun onStart() {
        fragmentStateTripBinding.mapview.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()

    }

    override fun onStop() {
        fragmentStateTripBinding.mapview.onStart()
        MapKitFactory.getInstance().onStop()
        super.onStop()

    }
    private fun setLocation(){
        val mapKit: MapKit = MapKitFactory.getInstance()
        val locationOnMapKit = mapKit.createUserLocationLayer(
            fragmentStateTripBinding.mapview.mapWindow
        )
        locationOnMapKit.isVisible = true

    }


    override fun onDrivingRoutes(p0: MutableList<DrivingRoute>) {
        for (route in p0){
            mapObjects.addPolyline(route.geometry)
        }
    }


    override fun onDrivingRoutesError(p0: com.yandex.runtime.Error) {
        val error = "Неизвестная ошибка"
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }
    @SuppressLint("MissingPermission")
    private fun racoord(){
        tableForDBRepository.allPaths.observe(viewLifecycleOwner, Observer { result->
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                start = Point(it.latitude, it.longitude)
                fragmentStateTripBinding.mapview.map.move(
                    CameraPosition(start, 14.0f, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 0f),
                    null)
                val drivingOptions = DrivingOptions(90.0,1,true,true,true, null,AnnotationLanguage.RUSSIAN)
                val vehicleOptions = VehicleOptions()
                val requestPoints:ArrayList<RequestPoint> = ArrayList()
                val list = ArrayList<Point>()
                requestPoints.add(RequestPoint(start,RequestPointType.WAYPOINT, null))
                if (result.isNotEmpty()) {
                    for (item in result) {
                        val lat = item.lat.toDouble()
                        val lon = item.lon.toDouble()
                        requestPoints.add(
                            RequestPoint(
                                Point(lat, lon),
                                RequestPointType.WAYPOINT,
                                null
                            )
                        )
                        val placemark:PlacemarkMapObject = mapObjects.addPlacemark(Point(lat,lon))
                        placemark.setIcon(ImageProvider.fromResource(context,R.drawable.placemark),IconStyle(
                            null,RotationType.ROTATE,10F,false,true,0.08F,null))

                    }
                    drivingSession = drivingRouter.requestRoutes(
                        requestPoints,
                        drivingOptions,
                        vehicleOptions,
                        this
                    )
                    }
            }

        })
    }

    override fun onObjectAdded(p0: UserLocationView) {

    }

    override fun onObjectRemoved(p0: UserLocationView) {
        TODO("Not yet implemented")
    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
        TODO("Not yet implemented")
    }

//    override fun dbOnClick(item: TableForDB) {
//        val bundle = Bundle()
//        bundle.putString("1",item.identification)
//        findNavController(bottomSheetBinding.root).navigate(R.id.action_stateTripFragment_to_placeCardInLKFragment,bundle)
//    }
}