package it.shu2019.stopdesertification

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import android.view.Window.FEATURE_NO_TITLE
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import kotlinx.android.synthetic.main.dialog.*


class MyDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        checkedTextView.setChecked(load("0"))
        checkedTextView1.setChecked(load("1"))
        checkedTextView2.setChecked(load("2"))
        checkedTextView3.setChecked(load("3"))
        checkedTextView4.setChecked(load("4"))
    }

    override fun onStop() {
        super.onStop()
        save(checkedTextView.isChecked,"0")
        save(checkedTextView1.isChecked,"1")
        save(checkedTextView2.isChecked,"2")
        save(checkedTextView3.isChecked,"3")
        save(checkedTextView4.isChecked,"4")
    }

    private fun save(isChecked: Boolean,key: String) {
        val sharedPreferences = context?.getSharedPreferences("AAAAA",Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putBoolean(key, isChecked)
        editor?.commit()
    }

    private fun load(key: String): Boolean {
        val sharedPreferences = context?.getSharedPreferences("AAAAA",Context.MODE_PRIVATE)
        return sharedPreferences?.getBoolean(key, true)!!
    }
}