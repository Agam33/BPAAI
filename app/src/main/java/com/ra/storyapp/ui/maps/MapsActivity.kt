package com.ra.storyapp.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.ra.storyapp.R
import com.ra.storyapp.databinding.ActivityMapsBinding
import com.ra.storyapp.domain.model.Story
import com.ra.storyapp.utils.MapStyleOption
import com.ra.storyapp.utils.Resources
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val viewModel: MapsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getMapLocation()
        observer()
        mapStyle()
        mMap.moveCamera(CameraUpdateFactory.newLatLng(DEFAULT_POSITION))
    }

    private fun observer() {
        viewModel.getAllStoriesWithLocation().observe(this) { resources ->
            when(resources) {
                is Resources.Loading -> {}
                is Resources.Success -> {
                    resources.data?.let {
                        setStoriesOnMap(it)
                    }
                }
                is Resources.Error -> {}
            }
        }
    }

    private fun setStoriesOnMap(stories: List<Story>) {
        for(story in stories) {
            val pos = LatLng(story.lat, story.lon)
            mMap.addMarker(
                MarkerOptions()
                    .position(pos)
                    .title(story.name)
                    .snippet(story.description)
            )
        }
    }

    private val requestMapPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if(isGranted) {
            getMapLocation()
        }
    }

    private fun getMapLocation() {
        if(ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else  {
            requestMapPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_maps_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when(item.itemId) {
            R.id.standard_style -> {
                viewModel.setMapStyle(MapStyleOption.STANDARD)
                true
            }
            R.id.silver_style -> {
                viewModel.setMapStyle(MapStyleOption.SILVER)
                true
            }
            R.id.retro_style -> {
                viewModel.setMapStyle(MapStyleOption.RETRO)
                true
            }
            R.id.dark_style -> {
                viewModel.setMapStyle(MapStyleOption.DARK)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun mapStyle() {
        viewModel.getMapStyle.observe(this) { style ->
            try {
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, style))
            } catch (e: android.content.res.Resources.NotFoundException) { }
        }
    }

    companion object {
        private val DEFAULT_POSITION = LatLng(-6.8957643, 107.6338462)
    }
}