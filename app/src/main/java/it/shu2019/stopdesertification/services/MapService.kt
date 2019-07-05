package it.shu2019.stopdesertification.services

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapService(val map: GoogleMap?) {

    fun setCenter(coords: LatLng) {
        map?.let {
            it.moveCamera(CameraUpdateFactory.newLatLng(coords))
        }

    }

    fun getLocation(): LatLng {
        if (map !== null) {
            return LatLng(map.cameraPosition.target.latitude, map.cameraPosition.target.longitude)
        }
        return LatLng(0.0, 0.0)
    }

    fun addYellowMarker(coords: LatLng, title: String) {
        val options: MarkerOptions = MarkerOptions()
            .position(coords)
            .title(title)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))

        addMarker(coords, options)
    }

    fun addGreenMarker(coords: LatLng, title: String) {
        val options: MarkerOptions = MarkerOptions()
            .position(coords)
            .title(title)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

        addMarker(coords, options)
    }

    fun addRedMarker(coords: LatLng, title: String) {
        val options: MarkerOptions = MarkerOptions()
            .position(coords)
            .title(title)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

        addMarker(coords, options)
    }

    fun addBlueMarker(coords: LatLng, title: String) {
        val options: MarkerOptions = MarkerOptions()
            .position(coords)
            .title(title)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

        addMarker(coords, options)
    }

    fun addMarker(coords: LatLng, options: MarkerOptions) {
        map?.let {
            it.addMarker(options)
        }
    }

}