package it.shu2019.stopdesertification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_main)
        val currentUser = mAuth.currentUser
        currentUser?:signIn()
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
