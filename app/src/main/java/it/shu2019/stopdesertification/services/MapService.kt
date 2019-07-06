package it.shu2019.stopdesertification.services

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import it.shu2019.stopdesertification.entities.MarkerData
import kotlin.collections.ArrayList
import android.graphics.Bitmap
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import it.shu2019.stopdesertification.R


class MapService(val map: GoogleMap?, val context: Context) {

    init {
        map?.getUiSettings()?.setMapToolbarEnabled(false)
    }

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

    fun getImage(name: String): Drawable {
        return context.resources.getDrawable(context.resources.getIdentifier(name, "drawable", context.packageName))
    }

    fun addMyMarker(marker: MarkerData, callback: GoogleMap.OnInfoWindowClickListener) {

        val smallMarker = getBitmap(marker,84,84)

        val options: MarkerOptions = MarkerOptions()
            .position(marker.coords)
            .title(marker.title)
            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

        markers.add(marker)
        addMarker(marker.id, options, callback)
    }



    fun getBitmap(marker: MarkerData, width: Int, height: Int):Bitmap{
        var id = R.drawable.ic_good
        when(marker.category){
            0-> id = R.drawable.ic_good
            1-> id = R.drawable.ic_bad
            2-> id = R.drawable.ic_expert
            3-> id = R.drawable.ic_users_alert
            4-> id = R.drawable.ic_fire
        }

        val icon = BitmapFactory.decodeResource(
            context.resources,
            id
        )
        val smallMarker = Bitmap.createScaledBitmap(icon, width, height, false)
        return smallMarker
    }

    fun addMarker(id: String, options: MarkerOptions, callback: GoogleMap.OnInfoWindowClickListener) {
        map?.let {
            it.addMarker(options).tag = id;
        }

        map?.setOnInfoWindowClickListener(callback);
    }

}