package it.shu2019.stopdesertification

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

private const val ARG_PARAM1 = "param1"

class TutorialFragment : Fragment() {

    private var idMenu: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idMenu = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var idLayout = 0
        when(idMenu){
            R.id.navigation_A-> idLayout = R.layout.fragment_tutorial
            R.id.navigation_B-> idLayout = R.layout.fragment_tutorial
            R.id.navigation_C-> idLayout = R.layout.fragment_tutorial_2
        }
        return inflater.inflate(idLayout, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(idMenu: Int) =
            TutorialFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, idMenu)
                }
            }
    }
}
