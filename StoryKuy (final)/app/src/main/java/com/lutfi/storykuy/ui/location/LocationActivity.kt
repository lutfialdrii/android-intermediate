package com.lutfi.storykuy.ui.location

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.lutfi.storykuy.R
import com.lutfi.storykuy.data.ResultState
import com.lutfi.storykuy.databinding.ActivityLocationBinding
import com.lutfi.storykuy.ui.ViewModelFactory

class LocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLocationBinding
    private lateinit var token: String

    private val viewModel by viewModels<LocationViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = intent.getStringExtra(EXTRA_TOKEN)!!


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        viewModel.getStories(token).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        Toast.makeText(this, "Sedang mengambil data", Toast.LENGTH_SHORT).show()
                    }

                    is ResultState.Error -> {
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }

                    is ResultState.Success -> {
                        Log.d(TAG, "onCreate: ${result.data.listStory}")
                        result.data.listStory?.forEach { data ->
                            val latLng = LatLng(data?.lat!!, data.lon!!)
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(latLng)
                                    .title(data.name)
                                    .snippet(data.description)
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_TOKEN = "EXTRA_TOKEN"
        private val TAG = LocationActivity::class.java.simpleName

    }
}