package it.shu2019.stopdesertification.services

import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import it.shu2019.stopdesertification.entities.MarkerData
import java.util.*
import kotlin.collections.ArrayList

class MapService(val map: GoogleMap?) {

    val markers: ArrayList<MarkerData> = arrayListOf()

    fun setCenter(coords: LatLng) {
        map?.let {
            it.moveCamera(CameraUpdateFactory.newLatLng(coords))
        }

    }

    fun getMarkerById(id: String): MarkerData? {
        return markers.find { it.id == id }
    }

    fun getLocation(): LatLng {
        if (map !== null) {
            return LatLng(map.cameraPosition.target.latitude, map.cameraPosition.target.longitude)
        }
        return LatLng(0.0, 0.0)
    }

    fun addYellowMarker(marker: MarkerData, callback: GoogleMap.OnInfoWindowClickListener) {
        val options: MarkerOptions = MarkerOptions()
            .position(marker.coords)
            .title(marker.title)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))

        markers.add(marker)
        addMarker(marker.id, options, callback)
    }

//    fun addGreenMarker(coords: LatLng, title: String) {
//        val options: MarkerOptions = MarkerOptions()
//            .position(coords)
//            .title(title)
//            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//
//        addMarker(coords, options)
//    }
//
//    fun addRedMarker(coords: LatLng, title: String) {
//        val options: MarkerOptions = MarkerOptions()
//            .position(coords)
//            .title(title)
//            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//
//        addMarker(coords, options)
//    }
//
//    fun addBlueMarker(coords: LatLng, title: String) {
//        val options: MarkerOptions = MarkerOptions()
//            .position(coords)
//            .title(title)
//            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//
//        addMarker(coords, options)
//    }

    fun addMarker(id: String, options: MarkerOptions, callback: GoogleMap.OnInfoWindowClickListener) {
        map?.let {
            it.addMarker(options).tag = id;
        }

        map?.setOnInfoWindowClickListener(callback);
    }

}