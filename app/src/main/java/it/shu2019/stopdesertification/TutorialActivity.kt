package it.shu2019.stopdesertification

import android.app.ProgressDialog.show
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_tutorial.*
import kotlinx.android.synthetic.main.fragment_tutorial.*

class TutorialActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        viewpager.adapter = TutorialFragmentAdapter(supportFragmentManager)
        firstCircle.setCardBackgroundColor(Color.RED)
        viewpager.addOnPageChangeListener(this)
        firstCircle.setOnClickListener(this::changeFragment)
        secondCircle.setOnClickListener(this::changeFragment)
        thirdCircle.setOnClickListener(this::changeFragment)
    }

    fun changeFragment(view: View){
        var position = 0
        when(view.id){
            R.id.firstCircle-> position = 0
            R.id.secondCircle-> position = 1
            R.id.thirdCircle-> position = 2
        }
        viewpager.currentItem = position
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        changeColor(position)
    }

    fun changeColor(position: Int){
        firstCircle.setCardBackgroundColor(Color.BLACK)
        secondCircle.setCardBackgroundColor(Color.BLACK)
        thirdCircle.setCardBackgroundColor(Color.BLACK)
        when(position){
            0->firstCircle.setCardBackgroundColor(Color.RED)
            1->secondCircle.setCardBackgroundColor(Color.RED)
            2->thirdCircle.setCardBackgroundColor(Color.RED)
        }
    }

    inner class TutorialFragmentAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return TutorialFragment.newInstance(position)
        }

        override fun getCount(): Int {
            return 3
        }
    }
}
