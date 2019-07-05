package it.shu2019.stopdesertification

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import kotlinx.android.synthetic.main.fragment_tutorial.*

private const val ARG_PARAM1 = "param1"

class TutorialFragment : Fragment() {

    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var idLayout =  R.layout.fragment_tutorial
        when(position){
            0-> idLayout = R.layout.fragment_tutorial
            1-> idLayout = R.layout.fragment_tutorial_2
            2-> idLayout = R.layout.fragment_tutorial_3
        }
        return inflater.inflate(idLayout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.setDuration(500)
        anim.setStartOffset(100)
        anim.setRepeatMode(Animation.REVERSE)
        anim.setRepeatCount(Animation.INFINITE)
        textView?.startAnimation(anim)
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int) =
            TutorialFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, position)
                }
            }
    }
}
