package it.shu2019.stopdesertification.entities

import com.google.android.gms.maps.model.LatLng
import java.util.*

class MarkerData(
    var id: String,
    var title: String,
    var description: String,
    var imageUri: String,
    var coords: LatLng
) {
}