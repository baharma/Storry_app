package com.example.submission1bahar.maps

import android.Manifest
import android.content.Context

import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.submission1bahar.R
import com.example.submission1bahar.databinding.ActivityMapsBinding
import com.example.submission1bahar.preferences.UserPreference
import com.example.submission1bahar.viewmodel.ViewModelFactory
import com.example.submission1bahar.viewmodel.ViewModelMaps

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.io.IOException
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModelMaps: ViewModelMaps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
//        val dicodingSpace = LatLng(-6.8957643, 107.6338462)
//        mMap.addMarker(
//            MarkerOptions()
//                .position(dicodingSpace)
//                .title("Dicoding Space")
//                .snippet("Batik Kumeli No.50")
//        )
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpace, 15f))
//
//        mMap.setOnMapLongClickListener { latLng ->
//            mMap.addMarker(
//                MarkerOptions()
//                    .position(latLng)
//                    .title("New Marker")
//                    .snippet("Lat: ${latLng.latitude} Long: ${latLng.longitude}")
//                    .icon(vectorToBitmap(R.drawable.ic_android_black_24dp, Color.parseColor("#3DDC84")))
//            )
//        }
//        mMap.setOnPoiClickListener { pointOfInterest ->
//            val poiMarker = mMap.addMarker(
//                MarkerOptions()
//                    .position(pointOfInterest.latLng)
//                    .title(pointOfInterest.name)
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
//            )
//            poiMarker?.showInfoWindow()
//        }
        getMyLocation()
        setMapStyle()
        setViewmodel()
//        addManyMarker()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

//    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
//        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
//        if (vectorDrawable == null) {
//            Log.e("BitmapHelper", "Resource not found")
//            return BitmapDescriptorFactory.defaultMarker()
//        }
//        val bitmap = Bitmap.createBitmap(
//            vectorDrawable.intrinsicWidth,
//            vectorDrawable.intrinsicHeight,
//            Bitmap.Config.ARGB_8888
//        )
//        val canvas = Canvas(bitmap)
//        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
//        DrawableCompat.setTint(vectorDrawable, color)
//        vectorDrawable.draw(canvas)
//        return BitmapDescriptorFactory.fromBitmap(bitmap)
//    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun setViewmodel(){
        val pref = UserPreference.getInstance(dataStore)
        viewModelMaps = ViewModelProvider(this,ViewModelFactory(pref,this))[ViewModelMaps::class.java]
        viewModelMaps.getToken().observe(this){token:String ->
            viewModelMaps.getListMapsAll("Bearer $token")
            viewModelMaps.getallmaps().observe(this){
                val build = LatLngBounds.builder()
                it.forEachIndexed{index, listStoryItem ->
                    val latlang = LatLng(listStoryItem.lat,listStoryItem.lon)

                    mMap.addMarker(
                        MarkerOptions().position(latlang).title(listStoryItem.name)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    )
                    build.include(latlang)
                    val bouns : LatLngBounds = build.build()
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bouns,50))
                }
            }
        }


    }
//
//    data class TourismPlace(
//        val name: String,
//        val latitude: Double,
//        val longitude: Double
//    )
//
//    private val boundsBuilder = LatLngBounds.Builder()

//    private fun addManyMarker() {
//        val tourismPlace = listOf(
//            TourismPlace("Alam Tirta Outbound", -8.4567978,115.25192),
//            TourismPlace("Sangeh Uma Dong Loka Villa", -8.4640996,115.2188611),
//            TourismPlace("WARUNG SISIN PANGKUNG", -8.4620286,115.2210837),
//            TourismPlace("Warung Beten Biu (jempeng)", -8.4620286,115.2210837),
//            TourismPlace("Pengempu Waterfall", -8.4760087, 115.2462615),
//        )
//        tourismPlace.forEach { tourism ->
//            val latLng = LatLng(tourism.latitude, tourism.longitude)
//            val addressName = getAddressName(tourism.latitude, tourism.longitude)
//            mMap.addMarker(MarkerOptions().position(latLng).title(tourism.name).snippet(addressName))
//            boundsBuilder.include(latLng)
//        }
//
//        val bounds: LatLngBounds = boundsBuilder.build()
//        mMap.animateCamera(
//            CameraUpdateFactory.newLatLngBounds(
//                bounds,
//                resources.displayMetrics.widthPixels,
//                resources.displayMetrics.heightPixels,
//                300
//            )
//        )
//    }

//    private fun getAddressName(lat: Double, lon: Double): String? {
//        var addressName: String? = null
//        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
//        try {
//            val list = geocoder.getFromLocation(lat, lon, 1)
//            if (list != null && list.size != 0) {
//                addressName = list[0].getAddressLine(0)
//                Log.d(TAG, "getAddressName: $addressName")
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return addressName
//    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}