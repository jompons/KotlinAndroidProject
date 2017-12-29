package com.jompon.kotlinandroidproject.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.*
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.jompon.kotlinandroidproject.MapFragment
import java.util.*

/**
 * Created by Jompon on 12/28/2017.
 */
class GoogleTrackingService : Service() , LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    companion object {
        const val KEY_LOCATION: String = "KEY_LOCATION"
        const val FILTER_ACTION = "FILTER_ACTION"
        const val START_ACTION = "START_ACTION"
        const val STOP_ACTION = "STOP_ACTION"
        private val TAG = GoogleTrackingService::class.java.simpleName
        private val LOCATION_INTERVAL = 60000L
        private val LOCATION_FAST_INTERVAL = 30000L
        private val LOCATION_DISTANCE = 1f
    }

    private val mBinder = LocalBinder()
    private var mServiceLooper: Looper? = null
    private var mServiceHandler: GoogleTrackingService.ServiceHandler? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var handler: Handler? = null

    inner class ServiceHandler (looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.

            Log.d(TAG, "handleMessage start listener")
            if (msg.obj == null) return
            if (msg.obj == START_ACTION) {
                Log.d(TAG, "ACTION = " + START_ACTION)
                if (isConnected()) {
                    destroy()
                }
                create()
            } else if (msg.obj == STOP_ACTION) {
                Log.d(TAG, "ACTION = " + STOP_ACTION)
                destroy()
                stopSelf(msg.arg1)
            }

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            //            stopSelf(msg.arg1);
            Log.d(TAG, "handleMessage")
        }
    }

    inner class LocalBinder : Binder() {

        fun getService(): GoogleTrackingService{
            return this@GoogleTrackingService
        }
    }

    override fun onBind(arg0: Intent): IBinder? {
        return mBinder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        //        super.onStartCommand(intent, flags, startId);

        try {
            mServiceHandler?.apply {
                val msg = obtainMessage()
                msg.arg1 = startId
                msg.obj = intent.action
                sendMessage(msg)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message + "")
        }

        return Service.START_STICKY
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate")
        handler = Handler(Looper.getMainLooper())
        val thread = HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND)
        thread.start()

        //        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.looper
        mServiceHandler = ServiceHandler(mServiceLooper as Looper)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
        destroy()
    }

    private fun create() {
        buildGoogleApiClient()
        connect()
    }

    private fun destroy() {
        stopLocationUpdates()
        disconnect()
    }

    @Synchronized private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest?.apply {
            interval = LOCATION_INTERVAL
            fastestInterval = LOCATION_FAST_INTERVAL
            smallestDisplacement = LOCATION_DISTANCE
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun startLocationUpdates() {

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
            Log.d(TAG, "startLocationUpdates")
        } catch (e: SecurityException) {
            Log.d(TAG, e.message+"")
        }
    }

    private fun stopLocationUpdates() {
        if (isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
            Log.d(TAG, "stopLocationUpdates")
        }
    }

    private fun connect() {
        if (mGoogleApiClient != null) {
            Log.d(TAG, "connect")
            mGoogleApiClient!!.connect()
        }
    }

    private fun disconnect() {
        if (isConnected()) {
            Log.d(TAG, "disconnect")
            mGoogleApiClient?.disconnect()
        }
    }

    private fun isConnected(): Boolean {
        return mGoogleApiClient != null && mGoogleApiClient!!.isConnected
    }

    override fun onConnected(bundle: Bundle?) {

        Log.d(TAG, "onConnected")
        try {
            createLocationRequest()
            startLocationUpdates()
        } catch (e: SecurityException) {
            Log.d(TAG, "SecurityException")
        } catch (e: Exception) {

            Log.d(TAG, e.message + "")
        }
    }

    override fun onConnectionSuspended(i: Int) {

        Log.d(TAG, "onConnectionSuspended: " + i)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

        Log.d(TAG, connectionResult.toString() + "")
    }

    override fun onLocationChanged(location: Location) {

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = location.time
        Log.d(TAG, "onLocationChanged: " + location)
        Log.d(TAG, String.format(Locale.getDefault(), "%02d:%02d:%02d.%03d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND)) + " = " + location.provider + " (" + location.latitude + ", " + location.longitude + ")")

        val intent = Intent()
        intent.action = FILTER_ACTION
        intent.putExtra(KEY_LOCATION, location)
        sendBroadcast(intent)
    }
}
