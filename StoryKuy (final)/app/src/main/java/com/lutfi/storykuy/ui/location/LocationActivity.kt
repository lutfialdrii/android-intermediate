package com.lutfi.storykuy.ui.location

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.lutfi.storykuy.R
import com.lutfi.storykuy.data.models.AllStoriesResponse
import com.lutfi.storykuy.data.models.ListStoryItem
import com.lutfi.storykuy.databinding.ActivityLocationBinding
import com.lutfi.storykuy.ui.ViewModelFactory

class LocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLocationBinding
    private var data: List<ListStoryItem?>? = null

    private val viewModel by viewModels<LocationViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val responseData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(
                EXTRA_STORIES,
                AllStoriesResponse::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_STORIES)
        }
        data = responseData?.listStory

        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        data?.forEach { data ->
            val latLng = LatLng(data?.lat!!, data.lon!!)
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(data.name)
                    .snippet(data.description)
            )
        }
    }

    companion object {
        const val EXTRA_STORIES = "EXTRA_STORIES"
    }
}