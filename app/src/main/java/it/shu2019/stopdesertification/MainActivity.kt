package it.shu2019.stopdesertification

import android.content.Context
import android.content.Intent
import android.media.ExifInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import it.shu2019.stopdesertification.entities.MarkerData
import it.shu2019.stopdesertification.services.MapService
import java.util.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cardview.view.*

val list = ArrayList<String>()
var context: Context? = null
var adapter: MainActivity.Adapter? = null
class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    var mapService: MapService? = null
    val mAuth = FirebaseAuth.getInstance()
    val actionCodeSettings = ActionCodeSettings.newBuilder()
        .setUrl("https://stopdesertification.page.link/4v3Q")
        .setHandleCodeInApp(true)
        .setAndroidPackageName(
            "it.shu2019.stopdesertification",
            true,
            "21"
        )
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)?.let {
//            it.getMapAsync(this)
//        }
        context = this
        adapter = Adapter(list)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val currentUser = mAuth.currentUser
        currentUser ?: signIn()
        FABClickManager()

//        val listUsers = arrayOf(
//            "Google",
//            "Apple",
//            "Microsoft",
//            "Asus",
//            "Zenpone",
//            "Acer"
//        )

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerView.adapter = adapter

//        for (i in 0 until listUsers.size){
//
//            list.add(listUsers.get(i))
//
//            if(listUsers.size - 1 == i){
//                // init adapter yang telah dibuat tadi
//                val adapter = Adapter(list)
//
//                //tampilkan data dalam recycler view
//            }
//
//        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mapService = MapService(googleMap,this)

//        mapService?.addBlueMarker(LatLng(22.777170, 90.399452), "Test")
//        mapService?.addGreenMarker(LatLng(23.777176, 92.399458), "Test");
//        mapService?.addRedMarker(LatLng(21.777176, 91.399444), "Test");
//        mapService?.addYellowMarker(LatLng(26.777168, 93.399452), "Test");


//        val cards = findViewById(R.id.cards) as LinearLayout
//
//        val view1: CardView = LayoutInflater.from(this).inflate(R.layout.cardview, cards) as CardView
//        val view2: CardView = LayoutInflater.from(this).inflate(R.layout.cardview, cards) as CardView


//        view1.test.text = "Card1"
//        view1.width
//        view2.test.text = "Card2"

//        cards.addView(view1)
//        cards.addView(view2);

    }

    fun FABClickManager() {
        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { _ ->
            val intent = Intent(this, CreateActivity::class.java).apply {
                //                putExtra(EXTRA_MESSAGE, message)
            }
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == 1 && mapService != null) {
            val marker = MarkerData(
                UUID.randomUUID().toString(),
                data?.extras?.get("title").toString(),
                data?.extras?.get("description").toString(),
                data?.extras?.get("imageUri").toString(),
                mapService?.getLocation()!!,
                data?.extras?.get("category") as Int
            )

            addPicture(data?.extras?.get("imageUri").toString());

            mapService?.addMyMarker(marker, GoogleMap.OnInfoWindowClickListener {
                var marker: MarkerData? = mapService?.getMarkerById(it.tag.toString())
                if (marker != null)
                    startActivity(marker)
            })
        }
    }

    fun startActivity(marker: MarkerData){
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("title", marker.title)
            putExtra("description", marker.description)
            putExtra("imageUri", marker.imageUri)
            putExtra("category", marker.category)
        }
        startActivity(intent)
    }

    fun addPicture(imageUri: String) {
        list.add(imageUri)
        adapter?.notifyDataSetChanged()
    }

    fun signIn() {

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
                    Log.e("SENT", "SENT")
                } else
                    Log.e("Error", task.exception.toString())
            }
    }

    inner class Adapter(val list: ArrayList<String>) : RecyclerView.Adapter<Holder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val holder = Holder(LayoutInflater.from(parent.context).inflate(R.layout.cardview, parent, false))
            return holder
        }

        override fun getItemCount(): Int = list?.size

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.view.image.setImageURI(Uri.parse(list.get(position)))
            holder.view.title.text = mapService?.markers?.get(position)?.title
            //holder.view.icon.setImageBitmap(mapService?.getBitmap(mapService?.markers?.get(position)!!,84,84))
            Glide.with(context!!).load(mapService?.getBitmap(mapService?.markers?.get(position)!!,84,84)).into(holder.view.icon)
        }

    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener(this::clickOnCardview)

        }

        fun clickOnCardview(view: View) {
            val itemPosition = recyclerView.getChildLayoutPosition(view)
            val marker = mapService?.markers?.find { it.imageUri == list[itemPosition] }
            if(marker!=null)
                startActivity(marker)
        }
    }

}
