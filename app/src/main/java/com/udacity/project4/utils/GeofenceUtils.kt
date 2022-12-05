package com.udacity.project4.utils

import android.content.Context
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.R
import com.google.android.gms.maps.model.LatLng
import java.util.concurrent.TimeUnit


internal object GeofencingConstants {


    const val GEOFENCE_RADIUS_IN_METERS = 100f
    const val ACTION_GEOFENCE_EVENT = "LocationFinder.Geofancesutils.ACTION_GEOFENCE_EVENT"
}

fun errorMessage(context: Context, errorCode: Int): String {
    val resources = context.resources

    return when (errorCode) {

        GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> resources.getString(com.udacity.project4.R.string.geofence_not_available)
        GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> resources.getString(com.udacity.project4.R.string.geofence_too_many_geofences)
        GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> resources.getString(com.udacity.project4.R.string.geofence_too_many_pending_intents)
        else -> resources.getString(com.udacity.project4.R.string.error_adding_geofence)
    }
}


