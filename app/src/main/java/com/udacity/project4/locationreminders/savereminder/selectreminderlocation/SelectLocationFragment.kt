package com.udacity.project4.locationreminders.savereminder.selectreminderlocation


import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.udacity.project4.R
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.databinding.FragmentSelectLocationBinding
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.utils.setDisplayHomeAsUpEnabled
import kotlinx.android.synthetic.main.it_reminder.*
import org.koin.android.ext.android.inject
import java.util.*


class SelectLocationFragment : BaseFragment(), OnMapReadyCallback {



    private lateinit var namepoi: String

     private var _longitude: Double = 3243.9678

    private var _latitude: Double = 291.1531556

    lateinit var callback: LocationCallback

    private lateinit var map: GoogleMap

    override val _myViewModel: SaveReminderViewModel by inject()

    private lateinit var binding: FragmentSelectLocationBinding

    private val TAG = SelectLocationFragment::class.java.simpleName

    private var longitudepoi: Double = 0.0

    private var latitudepoi: Double = 0.0




    private lateinit var locationRequest: LocationRequest

    private lateinit var fusedLocationClient: FusedLocationProviderClient



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_select_location, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = _myViewModel

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setHasOptionsMenu(true)

        setDisplayHomeAsUpEnabled(true)
        return binding.root
    }





    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(callback)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
         R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }

        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }

        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }

        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }

        else -> super.onOptionsItemSelected(item)
    }
    private fun onLocationSelected() {
        binding.floatingActionButton.setOnClickListener {
            if (latitudepoi != 0.0 || longitudepoi != 0.0)
            {
                _myViewModel.latitude.value = latitudepoi
                _myViewModel.longitude.value = longitudepoi
                _myViewModel.reminderSelectedLocationStr.value = namepoi
                _myViewModel.navCommand.value =
                    NavigationCommand.To(SelectLocationFragmentDirections.actionSelectLocationFragmentToSaveReminderFragment())
            } else {
                Toast.makeText(
                    requireContext(),
                    "please choose point of interest",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    @RequiresApi(33)
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        locationRequest = LocationRequest.create()
        locationRequest.interval = 120000


        locationRequest.fastestInterval = 120000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        val yourplace = LatLng(_latitude, _longitude)
        val zoomLevel = 15f

        map.addMarker(MarkerOptions().position(yourplace).title("your place"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(yourplace, zoomLevel))
        setPoiClick(map)
        setMapLongClick(map)
        setMapStyle(map)
        enableMyLocation()
        onLocationSelected()
        callback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult
                for (location in locationResult.locations) {
                    _latitude = location.latitude
                    _longitude = location.longitude
                    val yourplace = LatLng(_latitude, _longitude)
                    val zoomLevel = 15f
                    map.addMarker(MarkerOptions().position(yourplace).title("your place"))
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(yourplace, zoomLevel))
                }
            }
        }

    }

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            latitudepoi = latLng.latitude
            longitudepoi = latLng.longitude
            namepoi = "Dropped pin"
            // A Snippet is Additional text that's displayed below the title.
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.dropped_pin))
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )

        }
    }


    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            latitudepoi = poi.latLng.latitude
            longitudepoi = poi.latLng.longitude
            namepoi = poi.name

            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            poiMarker!!.showInfoWindow()
        }
    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_style
                )
            )

            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) === PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(33)
    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            map.isMyLocationEnabled = true
        } else {
            Toast.makeText(
                requireContext(),
                "please need location to add your reminder",
                Toast.LENGTH_SHORT
            ).show()
            requestPermissions(
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { task ->
            if (task != null) {
                _latitude = task.latitude
                _longitude = task.longitude
                val yourplace = LatLng(_latitude, _longitude)
                val zoomLevel = 15f//from 1 world to 20 specific
                map.addMarker(MarkerOptions().position(yourplace).title("your place"))
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(yourplace, zoomLevel))
            } else {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    callback,
                    Looper.getMainLooper()
                )
            }
        }

    }

    @RequiresApi(33)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Check if location permissions are granted and if so enable the
        // location data layer.
        Log.d("testt", "enter")
        if (requestCode == 1) {
            if (grantResults.size > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            } else {
                Toast.makeText(
                    requireContext(),
                    "please let access location to know your place!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}
