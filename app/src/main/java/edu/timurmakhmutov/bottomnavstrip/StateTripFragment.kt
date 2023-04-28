package edu.timurmakhmutov.bottomnavstrip

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.*
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.*
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDBRepository
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentStateTripBinding
import kotlinx.android.synthetic.main.fragment_state_trip.*

class StateTripFragment : Fragment(), DrivingSession.DrivingRouteListener, UserLocationObjectListener {
    private lateinit var fragmentStateTripBinding: FragmentStateTripBinding
    private lateinit var start:Point


    private val tableForDBRepository = TableForDBRepository(Application())

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mapObjects:MapObjectCollection
    private lateinit var drivingRouter:DrivingRouter
    private lateinit var drivingSession: DrivingSession

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Inflate the layout for this fragment
        fragmentStateTripBinding = FragmentStateTripBinding.inflate(inflater, container, false)
        fragmentStateTripBinding.stateTripEnd.setOnClickListener {
            findNavController(
                fragmentStateTripBinding.root
            ).navigate(R.id.action_stateTripFragment_to_homeFragment)
        }
        fragmentStateTripBinding.mapview
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
        mapObjects = fragmentStateTripBinding.mapview.map.mapObjects.addCollection()


        return fragmentStateTripBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MapKitFactory.initialize(context)

        setLocation()
        racoord()


    }

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

    override fun onDrivingRoutesError(p0: Error) {
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
                val drivingOptions = DrivingOptions()
                val vehicleOptions = VehicleOptions()
                val requestPoints:ArrayList<RequestPoint> = ArrayList()
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
                        val mark:PlacemarkMapObject = mapObjects.addPlacemark(Point(lat, lon))
                        mark.opacity = 10000f
                        mark.setIconStyle(IconStyle())
                        mark.isDraggable = true
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
}