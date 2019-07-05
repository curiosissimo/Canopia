package it.shu2019.stopdesertification

import android.app.ProgressDialog.show
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_tutorial.*

class TutorialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
            addFragment(it.itemId)
            true
        }
        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        addFragment(R.id.navigation_A)
    }

    fun addFragment(id : Int){
        val tutorialFragment = TutorialFragment.newInstance(id)
        supportFragmentManager.
            beginTransaction().
            replace(R.id.container,tutorialFragment).
            commit()
    }

}
