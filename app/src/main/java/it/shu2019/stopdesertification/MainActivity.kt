package it.shu2019.stopdesertification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import it.shu2019.stopdesertification.services.MapService


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private var mapService: MapService? = null

    val mAuth = FirebaseAuth.getInstance()
    val actionCodeSettings = ActionCodeSettings.newBuilder()
        .setUrl("https://stopdesertification.page.link/4v3Q")
        .setHandleCodeInApp(true)
        .setAndroidPackageName(
            "it.shu2019.stopdesertification",
            true,
            "21")
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)?.let {
//            it.getMapAsync(this)
//        }
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val currentUser = mAuth.currentUser
        currentUser?:signIn()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mapService = MapService(googleMap)

        mapService?.addBlueMarker(LatLng(22.777170, 90.399452), "Test")
        mapService?.addGreenMarker(LatLng(23.777176, 92.399458), "Test");
        mapService?.addRedMarker(LatLng(21.777176, 91.399444), "Test");
        mapService?.addYellowMarker(LatLng(26.777168, 93.399452), "Test");

    }


    fun signIn(){

        /*mAuth.createUserWithEmailAndPassword("alestesta50@gmail.com","AAAAAAAAAAAAAA").addOnCompleteListener { task->
            if(task.isSuccessful){
                Log.e("isSuccessful","isSuccessful")
            }
            else{
                Log.e("Failed",task.exception.toString())
            }
        }*/

        mAuth.sendSignInLinkToEmail("alestesta50@gmail.com", actionCodeSettings) //EMAIL PROVA
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e("SENT","SENT")
                }
                else
                    Log.e("Error",task.exception.toString())
            }
    }
}
