package com.jompon.kotlinandroidproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jompon.kotlinandroidproject.base.BaseFragment
import com.jompon.kotlinandroidproject.service.GoogleTrackingService
import kotlinx.android.synthetic.main.fragment_map.*


/**
 * Created by Jompon on 12/28/2017.
 */
class MapFragment : BaseFragment(), OnMapReadyCallback{

    private var mContext: Context? = null
    private var googleMap: GoogleMap? = null
    private var latlng: LatLng? = null
    private var receiver: LocationReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        try {
            MapsInitializer.initialize(mContext)
            mapView.getMapAsync(this)
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
            Toast.makeText(mContext, e.message+"", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()
        startTracking()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        if( receiver == null ){
            receiver = LocationReceiver()
            val filter = IntentFilter()
            filter.addAction(GoogleTrackingService.FILTER_ACTION)
            mContext?.registerReceiver(receiver, filter)
        }
    }

    override fun onPause() {
        if( receiver != null ){
            mContext?.unregisterReceiver(receiver)
            receiver = null
        }
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        stopTracking()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun startTracking( )
    {
        val intent = Intent(mContext, GoogleTrackingService::class.java)
        intent.action = GoogleTrackingService.START_ACTION
        mContext?.startService(intent)
    }

    private fun stopTracking( )
    {
        val intent = Intent(mContext, GoogleTrackingService::class.java)
        intent.action = GoogleTrackingService.STOP_ACTION
        mContext?.stopService(intent)
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0
        googleMap?.apply {
            try{
                isMyLocationEnabled = true
            }catch (e: SecurityException){
                Toast.makeText(mContext, e.message+"", Toast.LENGTH_LONG).show()
            }
            uiSettings?.apply {
                isCompassEnabled = true
                isMyLocationButtonEnabled = true
                isZoomGesturesEnabled = true
                setAllGesturesEnabled(true)
            }
            setOnMyLocationButtonClickListener {

                if( latlng != null ){
                    googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12.0f))
                }
                true
            }
            setOnInfoWindowClickListener { marker ->
                val intent = Intent(mContext, MainActivity::class.java)
                startActivity(intent)
            }

            val victoryMonument = LatLng(13.765064, 100.538224)
            addMarker(MarkerOptions().position(victoryMonument).title("Marker in Victory Monument"))
            moveCamera(CameraUpdateFactory.newLatLng(victoryMonument))
        }
    }

    inner class LocationReceiver : BroadcastReceiver() {

        override fun onReceive(p0: Context?, p1: Intent?) {

            when(p1?.action){
                GoogleTrackingService.FILTER_ACTION -> {
                    val location = p1.getParcelableExtra(GoogleTrackingService.KEY_LOCATION) as Location
                    if( latlng == null ){
                        //First location is found
                        latlng = LatLng(location.latitude, location.longitude)
                        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12.0f))
                    }
                    latlng = LatLng(location.latitude, location.longitude)
                }
            }
        }
    }
}