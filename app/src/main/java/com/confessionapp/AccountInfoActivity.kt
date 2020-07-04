package com.confessionapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_account_info.*

class AccountInfoActivity : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    var accountEmailTextView: TextView? = null
    var accountNameTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_info)


        //Set up Account Information
        accountEmailTextView = findViewById(R.id.accountEmailTextView)
        accountEmailTextView?.text = mAuth.currentUser?.email.toString()
        accountNameTextView = findViewById(R.id.accountNameTextView)
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid.toString()).child("name").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                accountNameTextView?.text = snapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) { }
        })
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainFeedActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun updateInfoClicked(view: View){

    }
}