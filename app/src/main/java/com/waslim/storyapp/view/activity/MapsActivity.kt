package com.waslim.storyapp.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.waslim.storyapp.R
import com.waslim.storyapp.databinding.ActivityMapsBinding
import com.waslim.storyapp.model.Constants
import com.waslim.storyapp.model.Result
import com.waslim.storyapp.model.response.story.Story
import com.waslim.storyapp.model.showToast
import com.waslim.storyapp.viewmodel.MapsViewModel
import com.waslim.storyapp.viewmodel.UserTokenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val boundBuilder = LatLngBounds.Builder()
    private val mapsViewModel by viewModels<MapsViewModel>()
    private val userTokenViewModel by viewModels<UserTokenViewModel>()

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setupStory()
        setMapStyle()
    }

    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.maps_style))
            if (!success) {
                showToast(getString(R.string.style_error))
            }
        } catch (exception: Resources.NotFoundException) {
            showToast(exception.message.toString())
        }
    }

    private fun setupStory() = userTokenViewModel.getToken().observe(this) { token ->
        when {
            token != "" -> {
                mapsViewModel.maps.observe(this) { status ->
                    when (status) {
                        is Result.Loading -> {

                        }
                        is Result.Failure -> {
                            showToast(status.message)
                        }
                        is Result.Success -> {
                            showMarker(status.data.listStory!!)
                        }
                    }
                }
                mapsViewModel.getStoryWithLocation(Constants.BEARER + token, Constants.LOCATION)
            } else -> showToast(getString(R.string.un_authorization))
        }
    }

    private fun showMarker(story: List<Story>) = story.forEach {
        val location = LatLng(it.lat, it.lon)

        val marker = mMap.addMarker(
            MarkerOptions()
                .position(location)
                .title(it.name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .alpha(0.7f)
                .snippet(it.description)
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        boundBuilder.include(location)
        marker?.tag = it
        mMap.setOnInfoWindowClickListener {
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.DATA_STORY, it.tag as Story)
            }
            startActivity(intent)
            finish()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) getMyLocation()
        }

    private fun getMyLocation() {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) mMap.isMyLocationEnabled = true else requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

}