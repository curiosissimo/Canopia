package it.shu2019.stopdesertification

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import it.shu2019.stopdesertification.entities.MarkerData
import it.shu2019.stopdesertification.services.MapService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cardview.view.*
import java.util.*

val list = ArrayList<String>()
var context: Context? = null
var adapter: DrawerActivity.Adapter? = null

class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
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
        setContentView(R.layout.activity_drawer)



        context = this
        adapter = Adapter(list)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val currentUser = mAuth.currentUser
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




        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.tutorial-> startActivity(Intent(this,TutorialActivity::class.java))
            R.id.logout-> {
                finishAndRemoveTask()
                System.exit(0)
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mapService = MapService(googleMap, this)
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
