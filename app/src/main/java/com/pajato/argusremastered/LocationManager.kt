package com.pajato.argusremastered

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.pajato.argusremastered.model.Content
import com.pajato.argusremastered.model.LocationResultEvent
import com.pajato.argusremastered.model.WatchedEvent
import com.pajato.argusremastered.util.RxBus
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.util.*

class LocationManager(private val activity: Activity) : LocationCallback(), LifecycleObserver, Consumer<WatchedEvent> {
    private lateinit var subscriptions: List<Disposable>
    private lateinit var content: Content

    /** Request Location Access when accepting an event. */
    override fun accept(t: WatchedEvent) {
        requestLocationAccess()
        content = t.getData()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun subscribeToEvent() {
        subscriptions = listOf(RxBus.subscribeToEventType(WatchedEvent::class.java, this))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disposeOfSubscriptions() {
        for (disposable in subscriptions) {
            disposable.dispose()
        }
        subscriptions = emptyList()
    }

    /** If we don't have Location permission, ask for it. If we have it, then request a location update. */
    fun requestLocationAccess() {
        val p = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        if (p != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            // We always want to prompt the user to let us access their location unless they ask us
            // to never ask them again. This behavior is handled by the requestPermissions() method
            ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LocationManager.LOCATION_REQUEST_CODE)
        } else {
            val locationReq = LocationRequest.create()
            locationReq.numUpdates = 1
            locationReq.priority = LocationRequest.PRIORITY_LOW_POWER
            val client = FusedLocationProviderClient(activity)
            client.requestLocationUpdates(locationReq, this, null)
        }
    }

    /** When we receive a location, acquire the town-level location & send out a location update. */
    override fun onLocationResult(locationResult: LocationResult) {
        val gcd = Geocoder(activity, Locale.getDefault())
        val location = locationResult.locations[0]
        if (location != null) {
            val addresses = gcd.getFromLocation(location.latitude, location.longitude, 1)
            val locality = addresses[0].locality + ", " + addresses[0].adminArea
            Log.v("onLocationResult", "found locality for current location: $locality")
            content.location = locality
            RxBus.send(LocationResultEvent(content, location))
        }
    }

    companion object {
        const val LOCATION_REQUEST_CODE = 2
    }
}