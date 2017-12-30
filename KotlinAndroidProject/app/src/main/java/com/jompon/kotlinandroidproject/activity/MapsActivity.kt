package com.jompon.kotlinandroidproject.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jompon.kotlinandroidproject.R
import com.jompon.kotlinandroidproject.base.BaseActivity
import com.jompon.kotlinandroidproject.service.GoogleTrackingService
import kotlinx.android.synthetic.main.activity_maps.*

/**
 * Created by Jompon on 12/30/2017.
 */
class MapsActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var latlng: LatLng? = null
    private var receiver: LocationReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        setSupportActionBar(toolbar)
        setBindingData()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun setBindingData() {

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            title = getString(R.string.maps_title)
        }
    }

    override fun onStart() {
        super.onStart()
        startTracking()
    }

    override fun onResume() {
        super.onResume()
        if( receiver == null ){
            receiver = LocationReceiver()
            val filter = IntentFilter()
            filter.addAction(GoogleTrackingService.FILTER_ACTION)
            registerReceiver(receiver, filter)
        }
    }

    override fun onPause() {
        if( receiver != null ){
            unregisterReceiver(receiver)
            receiver = null
        }
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        stopTracking()
    }

    private fun startTracking( )
    {
        val intent = Intent(this, GoogleTrackingService::class.java)
        intent.action = GoogleTrackingService.START_ACTION
        startService(intent)
    }

    private fun stopTracking( )
    {
        val intent = Intent(this, GoogleTrackingService::class.java)
        intent.action = GoogleTrackingService.STOP_ACTION
        stopService(intent)
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

        mMap.apply {
            try {
                isMyLocationEnabled = true
            } catch (e: SecurityException) {
                Toast.makeText(this@MapsActivity, e.message + "", Toast.LENGTH_LONG).show()
            }
            uiSettings?.apply {
                isCompassEnabled = true
                isMyLocationButtonEnabled = true
                isZoomGesturesEnabled = true
                setAllGesturesEnabled(true)
            }
            setOnMyLocationButtonClickListener {

                if (latlng != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12.0f))
                }
                true
            }
            // Add a marker in Sydney and move the camera
            val victoryMonument = LatLng(13.765064, 100.538224)
            addMarker(MarkerOptions().position(victoryMonument).title("Marker in Victory Monument"))
            moveCamera(CameraUpdateFactory.newLatLngZoom(victoryMonument, 12.0f))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    inner class LocationReceiver : BroadcastReceiver() {

        override fun onReceive(p0: Context?, p1: Intent?) {

            when(p1?.action){
                GoogleTrackingService.FILTER_ACTION -> {
                    val location = p1.getParcelableExtra(GoogleTrackingService.KEY_LOCATION) as Location
                    if( latlng == null ){
                        //First location is found
                        latlng = LatLng(location.latitude, location.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12.0f))
                    }
                    latlng = LatLng(location.latitude, location.longitude)
                }
            }
        }
    }
}