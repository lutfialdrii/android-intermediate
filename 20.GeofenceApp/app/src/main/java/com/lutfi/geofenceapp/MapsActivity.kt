package com.lutfi.geofenceapp

import android.Manifest
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.lutfi.geofenceapp.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
//    private val apiKey = BuildConfig.MAP_API_KEY
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val centerLat = 0.4521294782861196
    private val centerLng = 101.38877930410928
    private val geofenceRadius = 400.0

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 33) {
            requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true

        val IPL = LatLng(centerLat, centerLng)
        mMap.addMarker(
            MarkerOptions()
                .position(IPL)
                .title("Perum IPL")
        )

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(IPL, 15f))

        mMap.addCircle(
            CircleOptions()
                .center(IPL)
                .radius(geofenceRadius)
                .fillColor(0x22ff0000)
                .strokeColor(Color.RED)
                .strokeWidth(3f)
        )

        getMyLocation()
    }

    private fun getMyLocation() {

    }
}